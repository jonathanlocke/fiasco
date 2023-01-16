package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.thread.Monitor;
import digital.fiasco.runtime.repository.fiasco.RemoteRepository;
import digital.fiasco.runtime.repository.fiasco.server.FiascoClient;
import digital.fiasco.runtime.repository.fiasco.server.FiascoServer;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;

/**
 * A queue that tracks the resolution state of dependencies.
 *
 * <p>
 * The {@link #nextReady()} and {@link #nextReadyGroup()} methods return dependencies that are ready for processing
 * (meaning that they have no unresolved dependencies). When processing completes, the methods
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
 *
 * @param <T> The dependency type
 */
@SuppressWarnings("unused")
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class DependencyResolutionQueue<T extends Dependency>
{
    /** The resolved dependencies */
    private DependencyList<T> resolved = DependencyList.dependencies();

    /** The unresolved dependencies */
    private DependencyList<T> unresolved;

    /** The dependencies being processed */
    private DependencyList<T> processing = DependencyList.dependencies();

    /** The dependency type */
    private final Class<T> type;

    /** Monitor to signal the resolution of a dependency */
    private final Monitor resolution = new Monitor();

    /**
     * Creates a dependency queue of the given type with the given initial elements
     *
     * @param dependencies The dependencies to enqueue
     * @param type The type of dependnecy
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyResolutionQueue(DependencyList<T> dependencies, Class<T> type)
    {
        this.unresolved = dependencies;
        this.type = type;
    }

    /**
     * Returns the next unresolved dependency with no unresolved dependencies of its own.
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public T nextReady()
    {
        synchronized (resolution)
        {
            var next = ready().first();
            if (next != null)
            {
                unresolved = unresolved.without(next);
                processing = processing.with(next);
            }
            return next;
        }
    }

    /**
     * Returns a list of all ready dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> nextReadyGroup()
    {
        synchronized (resolution)
        {
            // Get the next group,
            var group = ready();

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
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public void resolve(T dependency)
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
    public void resolveGroup(DependencyList<T> dependencies)
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
    private boolean isReady(T dependency)
    {
        for (var at : dependency.dependencies(type))
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
    private DependencyList<T> ready()
    {
        synchronized (resolution)
        {
            DependencyList<T> ready;
            do
            {
                ready = unresolved.matching(this::isReady);
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
