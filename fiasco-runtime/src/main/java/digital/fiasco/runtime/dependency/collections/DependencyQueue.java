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
import com.telenav.kivakit.interfaces.object.Copyable;
import com.telenav.kivakit.interfaces.time.WakeState;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.repository.remote.RemoteRepository;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;
import digital.fiasco.runtime.repository.remote.server.FiascoServer;

import java.util.concurrent.locks.Condition;
import java.util.function.BiFunction;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.string.ObjectFormatter.ObjectFormat.MULTILINE;
import static com.telenav.kivakit.core.time.Duration.milliseconds;
import static com.telenav.kivakit.core.time.Time.now;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;
import static com.telenav.kivakit.core.value.count.Maximum._1;
import static digital.fiasco.runtime.dependency.collections.DependencyList.dependencies;

/**
 * A queue that tracks the processing of dependencies by one or more processor threads.
 *
 * <p><b>Queue Flow</b></p>
 *
 * <ol>
 *     <li>All dependencies passed to the constructor are added to the "available" set.</li>
 *     <li>The {@link #takeNextReady()} or {@link #takeAllReady()} method is called by a processor
 *         thread to retrieve dependencies that are ready for processing, as determined by the
 *         <i>isReady</i> predicate function defined by {@link #withIsReady(BiFunction)}. The
 *         ready dependencies are moved from the "available" set to the "taken" set, and returned
 *         to the caller.</li>
 *     <li>When processing of one or more dependencies completes, a processor thread calls
 *         {@link #completed(Dependency)} or {@link #completed(DependencyList)} to move them from
 *         the "taken" set to the "completed" set.</li>
 *     <li>While the above steps run, a thread can wait for all processing to finish by calling
 *         {@link #awaitCompletion(Duration)}</li>
 * </ol>
 *
 * <p><b>Taking Dependencies for Processing</b></p>
 *
 * <ul>
 *     <li>{@link #hasAvailable()}</li>
 *     <li>{@link #takeNextReady()}</li>
 *     <li>{@link #takeAllReady()}</li>
 * </ul>
 *
 * <p><b>Marking Dependencies as Processed</b></p>
 *
 * <ul>
 *     <li>{@link #completed(Dependency)}</li>
 *     <li>{@link #completed(DependencyList)}</li>
 * </ul>
 *
 * <p><b>Waiting for Processing to Complete</b></p>
 *
 * <ul>
 *     <li>{@link #awaitCompletion(Duration)}</li>
 * </ul>
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 * var queue = dependencyTree.asQueue(Artifact.class);
 * KivaKitThread.run(this, "processor", () ->
 * {
 *     while (queue.hasAvailable())
 *     {
 *         var next = queue.takeAllReady();
 *         process(next);
 *         queue.processed(next);
 *     }
 * });
 *
 * queue.awaitProcessingFinished(); </pre>
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
public class DependencyQueue extends BaseComponent implements
    ConsoleTrait,
    Copyable<DependencyQueue>
{
    /** The dependencies that are available to be processed (when ready) */
    @FormatProperty
    private DependencyList available;

    /** Any dependencies that are currently being processed */
    @FormatProperty
    private DependencyList taken;

    /** Any dependencies that have completed processing */
    @FormatProperty
    private DependencyList completed;

    /** Read/write lock for accessing available, taken and completed lists */
    private final Lock lock = new Lock();

    /** Condition to signal/await that indicates more dependencies have been processed */
    private final Condition completedMore = lock.newCondition();

    /** The type of dependency in this queue */
    private final Class<? extends Dependency> type;

    /** Matcher to determine when a dependency is ready for processing */
    private BiFunction<DependencyQueue, Dependency, Boolean> isReady;

    /**
     * Creates a dependency queue with the given initial elements, filtered by the given dependency type
     *
     * @param initial The dependencies to enqueue, in priority order, where the first elements will be processed first
     * @param type The type of dependency to add to this queue from the initial list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyQueue(DependencyList initial, Class<? extends Dependency> type)
    {
        this.type = type;
        this.available = initial.deduplicated().matching(type);
        this.taken = dependencies();
        this.completed = dependencies();

        trace("Created queue with dependencies: $", available);
    }

    private DependencyQueue(DependencyQueue that)
    {
        this.type = that.type;
        this.available = that.available.copy();
        this.taken = that.taken.copy();
        this.completed = that.completed.copy();
        this.isReady = that.isReady;
    }

    /**
     * Waits until all dependency processing has completed
     *
     * @param maximum The maximum amount of time to wait
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public WakeState awaitCompletion(Duration maximum)
    {
        var started = now();
        trace("Waiting for dependency processing to complete");

        // Grab the queue lock,
        return lock.whileLocked(() ->
        {
            // and while we're not done processing, and we haven't timed out,
            WakeState wake = null;
            for (var remaining = maximum; !isCompleted(); remaining = maximum.minus(started.elapsedSince()))
            {
                // wait for up to the maximum time remaining for more processing to complete,
                trace("Waiting for processed dependencies");
                wake = remaining.await(completedMore);
            }

            trace("Dependency processing complete");
            return wake;
        });
    }

    /**
     * Marks the given dependencies as having completed processing
     *
     * @param completed The dependencies that were processed
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public void completed(DependencyList completed)
    {
        lock.whileLocked(() ->
        {
            // Move the given dependencies from 'taken' to 'completed'
            this.taken = this.taken.without(completed);
            this.completed = this.completed.with(completed);
            trace("Completed $", completed);

            // and alert any threads waiting for work in take*() methods.
            completedMore.signalAll();
        });
    }

    /**
     * Marks the given dependency as having completed processing
     *
     * @param completed The dependency
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public void completed(Dependency completed)
    {
        completed(dependencies(completed));
    }

    /**
     * Returns the list of dependencies that have completed processing
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList completed()
    {
        return lock.whileLocked(() -> completed.copy());
    }

    @Override
    public DependencyQueue copy()
    {
        return new DependencyQueue(this);
    }

    /**
     * Returns true if there is more work available to process
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean hasAvailable()
    {
        ensureHasIsReadyFunction();
        return lock.whileLocked(() -> available.isNonEmpty());
    }

    /**
     * Returns true if all dependencies in the given list have completed processing
     *
     * @param list The dependencies to check
     * @return True if the dependencies have been processed
     */
    public boolean hasCompleted(BaseDependencyList<?, ?> list)
    {
        ensureHasIsReadyFunction();
        return completed.containsAll(list.asDependencyList());
    }

    /**
     * Returns true if the given dependency has completed processing
     *
     * @param dependency The dependency to check
     * @return True if the dependency has been processed
     */
    public boolean hasCompleted(Dependency dependency)
    {
        ensureHasIsReadyFunction();
        return completed.contains(dependency);
    }

    /**
     * Returns true if all dependencies are now in the list returned by {@link #completed()}
     */
    public boolean isCompleted()
    {
        ensureHasIsReadyFunction();
        return remaining().isEmpty();
    }

    /**
     * Returns a list of all ready dependencies, or an empty list if the queue is empty
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList takeAllReady()
    {
        ensureHasIsReadyFunction();
        return takeAll(MAXIMUM);
    }

    /**
     * Returns the next dependency that is ready, or null if the queue is empty.
     */
    @SuppressWarnings("unchecked")
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public <D extends Dependency> D takeNextReady()
    {
        ensureHasIsReadyFunction();
        var ready = takeAll(_1);
        return ready.isEmpty() ? null : (D) ready.first();
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).asString(MULTILINE);
    }

    /**
     * Returns a copy of this queue with the given <i>isReady</i> predicate function defined. This function is used to
     * determine when a given dependency is ready for processing.
     *
     * @param isReady The predicate function used to determine if a dependency is ready for processing
     * @return The copy
     */
    public DependencyQueue withIsReady(BiFunction<DependencyQueue, Dependency, Boolean> isReady)
    {
        return mutated(it -> it.isReady = ensureNotNull(isReady));
    }

    private void ensureHasIsReadyFunction()
    {
        ensure(isReady != null, "Must supply isReady function");
    }

    /**
     * Returns the list of dependencies that have yet to be completed
     *
     * @return The remaining dependencies, either available or taken
     */
    private DependencyList remaining()
    {
        return available.with(taken);
    }

    /**
     * Blocks until a list of dependencies ready to be processed is available. If the queue is empty the list will be
     * empty.
     */
    private DependencyList takeAll(Maximum maximum)
    {
        ensureHasIsReadyFunction();

        return lock.whileLocked(() ->
        {
            // While the queue has incomplete work,
            while (hasAvailable())
            {
                // collect any dependencies that are ready to be processed,
                var ready = available
                    .matching(it -> isReady.apply(this, it))
                    .first(maximum);

                // and if there are none ready now,
                if (ready.isEmpty())
                {
                    // wait for more dependencies to complete processing,
                    milliseconds(250).await(completedMore);
                }
                else
                {
                    // otherwise, move the group from the available list to the taken list, and return
                    // the ready dependencies.
                    available = available.without(ready);
                    taken = taken.with(ready);
                    trace("Took dependencies: $\nAvailable dependencies: $", ready, available);
                    return ready;
                }
            }

            trace("Dependency queue is empty");
            return dependencies();
        });
    }
}
