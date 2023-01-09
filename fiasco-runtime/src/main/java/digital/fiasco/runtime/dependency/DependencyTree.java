package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.messages.MessageException;

import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static java.util.Collections.disjoint;

/**
 * Tree of dependencies created by traversing dependencies in depth-first order from the root, resulting in a list of
 * dependencies where the leaves are first and the root is last. If the dependency graph is cyclic, a
 * {@link MessageException} will be thrown.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class DependencyTree<T extends Dependency>
{
    /**
     * @return The dependency graph formed by traversing dependencies starting at the given root
     */
    public static <T extends Dependency> DependencyTree<T> dependencyTree(Dependency root, Class<T> type)
    {
        return new DependencyTree<>(root, type);
    }

    /** The root of this dependency graph */
    private final Dependency root;

    /** The type of dependencies in this tree */
    private final Class<T> type;

    /** The dependencies of this graph in depth-first-order */
    private final DependencyList<T> depthFirst;

    private DependencyTree(Dependency root, Class<T> type)
    {
        this.root = root;
        this.type = type;
        depthFirst = depthFirst(root);
    }

    /**
     * @return The dependencies in this graph in depth-first order
     */
    public DependencyList<T> depthFirst()
    {
        return depthFirst;
    }

    /**
     * Returns the elements of this dependency tree organized into groups for parallel execution
     *
     * @return A list of dependency groups
     */
    public ObjectList<DependencyList<T>> grouped()
    {
        return new DependencyGrouper<T>().group(this, type);
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
    private DependencyList<T> depthFirst(Dependency root)
    {
        DependencyList<T> explored = dependencyList();

        // Go through each child of the root
        for (var child : root.dependencies(type))
        {
            // and explore it (in a depth-first traversal)
            var descendants = depthFirst(child);

            // and if none of the explored values has already been explored
            if (disjoint(explored.asSet(), descendants.asSet()))
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
        if (type.isAssignableFrom(root.getClass()))
        {
            //noinspection unchecked
            return explored.with((T) root);
        }
        return explored;
    }
}
