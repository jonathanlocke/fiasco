package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.thread.Monitor;
import digital.fiasco.runtime.repository.remote.FiascoClient;
import digital.fiasco.runtime.repository.remote.FiascoServer;
import digital.fiasco.runtime.repository.remote.RemoteRepository;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static digital.fiasco.runtime.dependency.DependencyList.dependencies;

/**
 * A queue that tracks the resolution state of dependencies.
 *
 * <p>
 * The {@link #nextReady(Class)} and {@link #nextReadyGroup(Class)} methods return dependencies that are ready for
 * processing (meaning that they have no unresolved dependencies). When processing completes, the methods
 * {@link #resolve(Dependency)} {@link #resolveGroup(DependencyList)} marks the processed dependencies as complete. In
 * essence, the unresolved leaves of the dependency tree are being processed first. Each time a leaf is processed, it is
 * effectively pruned from the tree of unresolved dependencies.
 * </p>
 *
 * <p><b>Performance</b></p>
 *
 * <p>
 * The design of this queue allows groups of dependencies to be processed together. This is important because the Fiasco
 * repository system ({@link RemoteRepository}, {@link FiascoClient}, {@link FiascoServer}) is capable of resolving
 * multiple dependencies in a single request, which reduces the number of requests that are required for artifact
 * resolution, which speeds up the system.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class DependencyResolutionQueue
{
    /** The resolved dependencies */
    private DependencyList resolved = dependencies();

    /** The unresolved dependencies */
    private DependencyList unresolved;

    /** The dependencies being processed */
    private DependencyList processing = dependencies();

    /** Monitor to signal the resolution of a dependency */
    private final Monitor resolution = new Monitor();

    /**
     * Creates a dependency queue of the given type with the given initial elements
     *
     * @param dependencies The dependencies to enqueue
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyResolutionQueue(DependencyList dependencies)
    {
        this.unresolved = dependencies;
    }

    /**
     * Returns the next unresolved dependency with no unresolved dependencies of its own.
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public <D extends Dependency> D nextReady(Class<D> type)
    {
        synchronized (resolution)
        {
            var next = ready(type).first();
            if (next != null)
            {
                unresolved = unresolved.without(next);
                processing = processing.with(next);
            }
            return (D) next;
        }
    }

    /**
     * Returns a list of all ready dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public <D extends Dependency> DependencyList nextReadyGroup(Class<D> type)
    {
        synchronized (resolution)
        {
            // Get the next group,
            var group = ready(type);

            // and move it from unresolved to processing.
            unresolved = unresolved.without(group);
            processing = processing.with(group);

            return group;
        }
    }

    /**
     * Marks the given dependency as resolved
     *
     * @param dependency The dependency
     */
    @SuppressWarnings("unchecked")
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public void resolve(Dependency dependency)
    {
        synchronized (resolution)
        {
            // Move the given dependency from processing to resolved
            processing = processing.without(dependency);
            resolved = resolved.with(dependency);

            // and notify anyone waiting for dependencies to resolve.
            resolution.signal();
        }
    }

    /**
     * Marks the given dependencies as resolved
     *
     * @param dependencies The dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public void resolveGroup(DependencyList dependencies)
    {
        synchronized (resolution)
        {
            // Move the given dependencies from processing to resolved
            processing = processing.without(dependencies);
            resolved = resolved.with(dependencies);

            // and notify anyone waiting for dependencies to resolve.
            resolution.signal();
        }
    }

    /**
     * Returns true if the given dependency has no dependencies that are not already resolved, meaning that the
     * dependency is ready for processing.
     *
     * @param dependency The dependency
     * @return True if the dependency is ready for processing
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    private boolean isReady(Dependency dependency)
    {
        for (var at : dependency.dependencies())
        {
            if (!resolved.contains(at))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a list of dependencies that are ready for processing
     */
    private <D extends Dependency> DependencyList ready(Class<D> type)
    {
        synchronized (resolution)
        {
            DependencyList ready;
            do
            {
                ready = unresolved.matching(at -> isReady((D) at) && type.isAssignableFrom(at.getClass()));
                if (ready.isEmpty())
                {
                    resolution.await();
                }
            }
            while (ready.isEmpty());
            return ready;
        }
    }
}
