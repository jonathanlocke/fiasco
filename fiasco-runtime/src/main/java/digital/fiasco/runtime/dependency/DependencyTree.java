package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.messages.MessageException;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static digital.fiasco.runtime.dependency.DependencyList.dependencies;

/**
 * Tree of dependencies created by traversing dependencies in depth-first order from the root, resulting in a list of
 * dependencies where the leaves are first and the root is last. If the dependency graph is cyclic, a
 * {@link MessageException} will be thrown.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #DependencyTree(Dependency)}</li>
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
@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class DependencyTree
{
    /** The root of this dependency graph */
    private final Dependency root;

    /** The dependencies of this graph in depth-first-order */
    private final DependencyList depthFirst;

    public DependencyTree(Dependency root)
    {
        this.root = root;

        depthFirst = depthFirst(root, dependencies()).with(root);
    }

    /**
     * Returns this dependency tree as a queue
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyQueue asQueue()
    {
        return new DependencyQueue(depthFirst());
    }

    /**
     * @return The dependencies in this graph in depth-first order
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList depthFirst()
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
    private DependencyList depthFirst(Dependency root, DependencyList explored)
    {
        // Go through each child of the root,
        for (var child : root.dependencies())
        {
            // check for cycles (which should not be possible in our functional api),
            ensure(!explored.contains(child), "The dependency tree is cyclic:", root);

            // and explore the child (in a depth-first traversal)
            explored = depthFirst(child, explored).with(child);
        }

        return explored;
    }
}
