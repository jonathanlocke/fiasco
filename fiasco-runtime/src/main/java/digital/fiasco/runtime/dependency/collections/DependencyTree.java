package digital.fiasco.runtime.dependency.collections;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;
import digital.fiasco.runtime.dependency.collections.lists.BaseDependencyList;
import digital.fiasco.runtime.dependency.collections.lists.DependencyList;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static digital.fiasco.runtime.dependency.collections.lists.DependencyList.dependencies;

/**
 * A tree of {@link Dependency}s with the root passed to the constructor.
 *
 * <p>
 * The {@link #asDepthFirstList()} method returns the dependencies in the tree as a list, in depth-first order. The
 * {@link #asQueue(Class)} method creates a {@link DependencyQueue} from this list. If the dependency tree is cyclic (if
 * it's actually a graph), a {@link RuntimeException} will be thrown.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #DependencyTree(Dependency)}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asQueue(Class)}</li>
 * </ul>
 *
 * <p><b>Traversal</b></p>
 *
 * <ul>
 *     <li>{@link #asDepthFirstList()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see Dependency
 * @see BaseDependencyList
 * @see DependencyQueue
 */
@SuppressWarnings({ "unused" })
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class DependencyTree
{
    /** The dependencies of this graph in depth-first-order */
    private final DependencyList depthFirst;

    /** The root dependency for this tree */
    private final Dependency root;

    /**
     * Creates a dependency tree from the given root dependency
     *
     * @param root The root
     * @throws RuntimeException Thrown if the root dependency is transitively cyclic (not a tree)
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyTree(Dependency root)
    {
        this.root = root;
        this.depthFirst = depthFirst(root, dependencies());
    }

    public DependencyTree(DependencyTree that)
    {
        this.depthFirst = that.depthFirst.copy();
        this.root = that.root;
    }

    /**
     * Returns the dependencies in this tree in depth-first order
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList asDepthFirstList()
    {
        return depthFirst;
    }

    /**
     * Returns this dependency tree as a {@link DependencyQueue} that can be used for tracking dependency processing
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyQueue asQueue(Class<? extends Dependency> type)
    {
        return new DependencyQueue(asDepthFirstList().deduplicated(), type);
    }

    /**
     * Returns the root of this tree
     *
     * @return The root dependency
     */
    public Dependency root()
    {
        return root;
    }

    /**
     * Visits the dependencies in depth-first order, calling the given visitor for each dependency
     *
     * @param visitor The visitor
     */
    public void visit(DependencyTreeVisitor visitor)
    {
        for (var at : asDepthFirstList())
        {
            if (at instanceof Asset asset)
            {
                visitor.onAsset(asset);
            }
            if (at instanceof Library library)
            {
                visitor.onLibrary(library);
            }
            if (at instanceof Artifact<?> artifact)
            {
                visitor.onArtifact(artifact);
            }
            if (at instanceof Builder builder)
            {
                visitor.onBuilder(builder);
            }
            visitor.onDependency(at);
        }
    }

    /**
     * Returns a list of dependencies in this tree in depth-first order
     *
     * @param at The dependency to explore
     * @param explored The list of dependencies already explored (for cycle detection)
     * @throws RuntimeException Thrown if a cycle is detected in the given root
     */
    private DependencyList depthFirst(Dependency at, DependencyList explored)
    {
        // Check for cycles (which should not be possible in our functional api)
        ensure(!(at instanceof Builder) || !explored.containsAny(at.builderDependencies().asDependencyList()), "The build dependency tree is cyclic: $ was already explored", at);

        // Add the given dependency as explored.
        explored = explored.with(at);

        // Go through each child of the root,
        for (var child : at.allDependencies())
        {
            // and explore the child (in a depth-first traversal)
            explored = depthFirst(child, explored);
        }

        return explored;
    }
}
