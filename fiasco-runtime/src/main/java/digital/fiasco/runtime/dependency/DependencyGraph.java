package digital.fiasco.runtime.dependency;

import java.util.Collections;

import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * Graph of dependencies created by traversing dependencies from a root. If the dependency graph is cyclic, terminal
 * failure will be reported via validation.
 *
 * @author shibo
 */
@SuppressWarnings("unused")
public class DependencyGraph<D extends Dependency<D>>
{
    /**
     * @return The dependency graph formed by traversing dependencies starting at the given root
     */
    public static <T extends Dependency<T>> DependencyGraph<T> dependencyGraph(T root)
    {
        return new DependencyGraph<>(root);
    }

    /** The root of this dependency graph */
    private final Dependency<D> root;

    /** The dependencies of this graph in depth-first-order */
    private final DependencyList<D> depthFirst;

    private DependencyGraph(D root)
    {
        this.root = root;
        depthFirst = depthFirst(root);
    }

    /**
     * @return The dependencies in this graph in depth-first order
     */
    public DependencyList<D> depthFirst()
    {
        return depthFirst;
    }

    /**
     * @return The root node of this dependency graph
     */
    public Dependency<D> root()
    {
        return root;
    }

    /**
     * @return List of dependencies in depth-first order
     */
    private DependencyList<D> depthFirst(D root)
    {
        DependencyList<D> explored = new DependencyList<>();

        // Go through each child of the root
        for (D child : root.dependencies())
        {
            // and explore it (in a depth-first traversal)
            var descendants = depthFirst(child);

            // and if none of the explored values has already been explored
            if (Collections.disjoint(explored.asSet(), descendants.asSet()))
            {
                // then add the explored descendants to the list of dependencies
                explored = explored.with(descendants);
            }
            else
            {
                // otherwise, if the explored list intersects the descendants, there is a cyclic dependency graph.
                fail("The dependency graph '$' is cyclic.", root);
            }
        }

        // Finally, return the explored children with the root.
        return explored.with(root);
    }
}
