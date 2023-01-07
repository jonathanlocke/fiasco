package digital.fiasco.runtime.dependency;

import java.util.Collections;

import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;

/**
 * Graph of dependencies created by traversing dependencies from a root. If the dependency graph is cyclic, terminal
 * failure will be reported via validation.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class DependencyGraph
{
    /**
     * @return The dependency graph formed by traversing dependencies starting at the given root
     */
    public static DependencyGraph dependencyGraph(Dependency root)
    {
        return new DependencyGraph(root);
    }

    /** The root of this dependency graph */
    private final Dependency root;

    /** The dependencies of this graph in depth-first-order */
    private final DependencyList depthFirst;

    private DependencyGraph(Dependency root)
    {
        this.root = root;
        depthFirst = depthFirst(root);
    }

    /**
     * @return The dependencies in this graph in depth-first order
     */
    public DependencyList depthFirst()
    {
        return depthFirst;
    }

    /**
     * @return The root node of this dependency graph
     */
    public Dependency root()
    {
        return root;
    }

    /**
     * @return List of dependencies in depth-first order
     */
    private DependencyList depthFirst(Dependency root)
    {
        DependencyList explored = dependencyList();

        // Go through each child of the root
        for (var child : root.dependencies())
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
