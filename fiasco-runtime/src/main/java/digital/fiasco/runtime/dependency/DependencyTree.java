package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.messages.MessageException;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * Tree of dependencies created by traversing dependencies in depth-first order from the root, resulting in a list of
 * dependencies where the leaves are first and the root is last. If the dependency graph is cyclic, a
 * {@link MessageException} will be thrown.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #dependencyTree(Dependency, Class)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #root()}</li>
 * </ul>
 *
 * <p><b>Traversal</b></p>
 *
 * <ul>
 *     <li>{@link #depthFirst()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "unchecked" })
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class DependencyTree<T extends Dependency>
{
    /**
     * @return The dependency graph formed by traversing dependencies starting at the given root
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
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

        depthFirst = depthFirst(root, DependencyList.dependencies()).with((T) root);
    }

    /**
     * Returns this dependency tree as a queue
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyResolutionQueue<T> asQueue()
    {
        return new DependencyResolutionQueue<>(depthFirst(), type);
    }

    /**
     * @return The dependencies in this graph in depth-first order
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> depthFirst()
    {
        return depthFirst;
    }

    /**
     * @return The root node of this dependency graph
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public Dependency root()
    {
        return root;
    }

    /**
     * @return List of dependencies in depth-first order
     */
    private DependencyList<T> depthFirst(Dependency root, DependencyList<T> explored)
    {
        // Go through each child of the root,
        for (var child : root.dependencies(type))
        {
            // check for cycles (which should not be possible in our functional api),
            ensure(!explored.contains(child), "The dependency tree is cyclic:", root);

            // and explore the child (in a depth-first traversal)
            explored = depthFirst(child, explored).with(child);
        }

        return explored;
    }
}
