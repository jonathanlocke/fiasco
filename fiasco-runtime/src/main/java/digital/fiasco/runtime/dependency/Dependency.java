package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.interfaces.naming.Named;
import digital.fiasco.runtime.repository.Repository;

import static digital.fiasco.runtime.dependency.DependencyGraph.dependencyGraph;

/**
 * A dependency has a list of {@link #dependencies()}, which must be resolved for it to function. A graph of
 * dependencies in depth-first order, where the leaves first and the root is last, can be created with {@link #graph()}.
 * This structure of a dependency graph is similar to typical project structure where the root module has a set of child
 * modules that must be built and those projects have their own child projects, etc.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface Dependency<D extends Dependency<D>> extends Named
{
    /**
     * @return The objects that this depends on
     */
    DependencyList dependencies();

    /**
     * Returns a dependency graph with this dependency at the root
     */
    default DependencyGraph graph()
    {
        return dependencyGraph(this);
    }

    /**
     * Returns the repository where this dependency was found
     *
     * @return The hosting repository
     */
    Repository repository();
}
