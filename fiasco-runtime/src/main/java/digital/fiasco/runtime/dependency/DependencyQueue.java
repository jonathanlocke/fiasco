package digital.fiasco.runtime.dependency;

import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;

/**
 * A queue of dependencies. The {@link #next()} method returns the next unresolved dependency that has no unresolved
 * dependencies of its own. When the dependency has been resolved, the method {@link #resolve(Dependency)} marks it as
 * resolved. In essence, the unresolved leaves of the dependency tree are being processed first. Each time a leaf is
 * processed, it is marked as resolved, which effectively prunes it from the tree of unresolved dependencies.
 *
 * @param <T> The dependency type
 */
public class DependencyQueue<T extends Dependency>
{
    /** The resolved dependencies */
    private DependencyList<T> resolved = dependencyList();

    /** The unresolved dependencies */
    private final DependencyList<T> unresolved;

    /** The dependency type */
    private final Class<T> type;

    /**
     * Creates a dependency queue of the given type with the given initial elements
     *
     * @param dependencies The dependencies to enqueue
     * @param type The type of dependnecy
     */
    public DependencyQueue(DependencyList<T> dependencies, Class<T> type)
    {
        this.unresolved = dependencies;
        this.type = type;
    }

    /**
     * Returns the next unresolved dependency with no unresolved dependencies of its own
     */
    public synchronized T next()
    {
        var first = unresolved.matching(this::hasNoIncompleteDependencies).first();
        if (first != null)
        {
            resolved = resolved.without(first);
        }
        return first;
    }

    /**
     * Marks the given dependency as completed
     *
     * @param dependency The dependency
     */
    public synchronized void resolve(T dependency)
    {
        resolved = resolved.with(dependency);
    }

    private boolean hasNoIncompleteDependencies(T dependency)
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
}
