package digital.fiasco.runtime.dependency.collections;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.os.ConsoleTrait;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.thread.locks.Lock;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.time.WakeState;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.collections.lists.DependencyList;
import digital.fiasco.runtime.repository.remote.RemoteRepository;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;
import digital.fiasco.runtime.repository.remote.server.FiascoServer;

import java.util.concurrent.locks.Condition;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.string.ObjectFormatter.ObjectFormat.MULTILINE;
import static com.telenav.kivakit.core.time.Duration.FOREVER;
import static com.telenav.kivakit.core.time.Time.now;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;
import static com.telenav.kivakit.core.value.count.Maximum._1;
import static digital.fiasco.runtime.dependency.collections.lists.DependencyList.dependencies;

/**
 * A queue that tracks the processing of dependencies by one or more processors.
 *
 * <p><b>Queue Flow</b></p>
 *
 * <ol>
 *     <li>All dependencies passed to the constructor are added to the "available" set.</li>
 *     <li>The {@link #takeOne(Class)} or {@link #takeAll(Class)} method is called by a processor
 *         thread to retrieve dependencies that are ready for processing (meaning that they are
 *         "available", and have no unprocessed transitive dependencies). The returned dependencies
 *         are moved from the "available" set to the "taken" set.</li>
 *     <li>When processing of one or more dependencies completes, a processor thread calls
 *         {@link #processed(Dependency)} or {@link #processed(DependencyList)} to move them from the "taken"
 *         set to the "processed" set.</li>
 *     <li>While the above steps run, a thread can wait for all processing to finish by called
 *         {@link #awaitProcessingCompletion(Duration)}</li>
 * </ol>
 *
 * <p><b>Taking Dependencies for Processing</b></p>
 *
 * <ul>
 *     <li>{@link #isWorkAvailable()}</li>
 *     <li>{@link #takeOne(Class)}</li>
 *     <li>{@link #takeAll(Class)}</li>
 * </ul>
 *
 * <p><b>Marking Dependencies as Processed</b></p>
 *
 * <ul>
 *     <li>{@link #processed(Dependency)}</li>
 *     <li>{@link #processed(DependencyList)}</li>
 * </ul>
 *
 * <p><b>Waiting for Processing to Complete</b></p>
 *
 * <ul>
 *     <li>{@link #awaitProcessingCompletion(Duration)}</li>
 * </ul>
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 * var queue = dependencyTree.asQueue();
 * KivaKitThread.run(this, "processor", () ->
 * {
 *     while (queue.isWorkAvailable())
 *     {
 *         var next = queue.takeAll(Library.class);
 *         process(next);
 *         queue.processed(next);
 *     }
 * });
 *
 * queue.awaitProcessingCompletion();
 * </pre>
 *
 * <p><b>Performance</b></p>
 *
 * <p>
 * The design of this queue allows groups of dependencies to be processed together. This is important because the Fiasco
 * repository system ({@link RemoteRepository}, {@link FiascoClient}, {@link FiascoServer}) is capable of resolving
 * multiple dependencies in a single request, which reduces the number of requests that are required for artifact
 * resolution, which in turn speeds up the system.
 * </p>
 *
 * @author Jonathan locke
 */
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class DependencyQueue extends BaseComponent implements ConsoleTrait
{
    /** The dependencies that are available to be processed (when all their transitive dependencies have been processed) */
    @FormatProperty
    private DependencyList available;

    /** Any dependencies that are currently being processed */
    @FormatProperty
    private DependencyList taken;

    /** Any dependencies that have completed processing */
    @FormatProperty
    private DependencyList processed;

    /** Read/write lock for accessing available, taken and processed lists */
    private final Lock lock = new Lock();

    /** Condition to signal/await that indicates more dependencies have been processed */
    private final Condition processedMore = lock.newCondition();

    /**
     * Creates a dependency queue with the given initial elements
     *
     * @param initial The dependencies to enqueue, in priority order, where the first elements will be processed frist
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyQueue(DependencyList initial)
    {
        ensure(initial.isNonEmpty(), "Cannot create a queue for an empty list");

        trace("Created queue with dependencies: $", initial);
        available = initial;
        taken = dependencies();
        processed = dependencies();
    }

    /**
     * Waits until all dependency processing has completed
     *
     * @param maximum The maximum amount of time to wait
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public WakeState awaitProcessingCompletion(Duration maximum)
    {
        var started = now();
        trace("Waiting for dependency processing to complete");

        // Grab the queue lock,
        return lock.whileLocked(() ->
        {
            // and while we're not done processing, and we haven't timed out,
            WakeState wake = null;
            for (var remaining = maximum; !isProcessingComplete(); remaining = maximum.minus(started.elapsedSince()))
            {
                // wait for up to the maximum time remaining for more processing to complete,
                wake = remaining.await(processedMore);
            }

            trace("Dependency processing complete");
            return wake;
        });
    }

    /**
     * Returns true if there is work available to process
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean isWorkAvailable()
    {
        return lock.whileLocked(() -> available.isNonEmpty());
    }

    /**
     * Returns the list of dependencies that have been processed.
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList processed()
    {
        return lock.whileLocked(() -> processed.copy());
    }

    /**
     * Marks the given dependencies as processed
     *
     * @param group The dependencies that were processed
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public void processed(DependencyList group)
    {
        lock.whileLocked(() ->
        {
            // Move the given dependencies from processing to processed,
            this.taken = this.taken.without(group);
            this.processed = this.processed.with(group);
            trace("Processed $", group);
            processedMore.signalAll();
        });
    }

    /**
     * Marks the given dependency as processed
     *
     * @param dependency The dependency
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public void processed(Dependency dependency)
    {
        processed(dependencies(dependency));
    }

    /**
     * Returns a list of all ready dependencies, or an empty list if the queue is empty
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList takeAll(Class<?> type)
    {
        return take(type, MAXIMUM);
    }

    /**
     * Returns the next dependency that is ready (meaning that all of its dependencies have already been processed), or
     * null if the queue is empty.
     */
    @SuppressWarnings("unchecked")
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public <D extends Dependency> D takeOne(Class<D> type)
    {
        var taken = take(type, _1);
        return taken == null ? null : (D) taken.first();
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).asString(MULTILINE);
    }

    /**
     * Returns true if all dependencies are now in the list returned by {@link #processed()}
     */
    private boolean isProcessingComplete()
    {
        return available.isEmpty() && taken.isEmpty();
    }

    /**
     * Returns true if the given dependency has no dependencies that are not already processed, meaning that the
     * dependency is ready for processing.
     *
     * @param candidate The candidate dependency
     * @return True if the dependency is ready for processing
     */
    private boolean isReady(Dependency candidate)
    {
        // If any dependency of the candidate
        for (var at : candidate.allDependencies())
        {
            // is unprocessed,
            if (!processed.contains(at))
            {
                // then it is not ready,
                return false;
            }
        }

        // otherwise it is.
        return true;
    }

    /**
     * Returns the list of dependencies that have yet to be fully processed
     *
     * @return The dependencies
     */
    private DependencyList remaining()
    {
        return available.with(taken).without(processed);
    }

    /**
     * Returns a list of dependencies matching the given type that are ready for processing. The list will be empty if
     * the queue is empty.
     */
    private DependencyList take(Class<?> type, Maximum maximum)
    {
        trace("Taking ${class} dependency", type);

        return lock.whileLocked(() ->
        {
            // While the queue is not empty,
            while (remaining().isNonEmpty())
            {
                // take any available dependencies of the given type,
                var group = available
                    .matching(at -> isReady(at) && type.isAssignableFrom(at.getClass()))
                    .first(maximum);

                // and if there are none ready but there are still some remaining,
                if (group.isEmpty() && remaining().isNonEmpty())
                {
                    // wait for some to be available,
                    trace("Waiting for dependencies to be processed");
                    FOREVER.await(processedMore);
                }
                else
                {
                    // otherwise, move the group from the available list to the taken list.
                    available = available.without(group);
                    taken = taken.with(group);
                    trace("Took $", group);
                    return group;
                }
            }
            return null;
        });
    }
}
