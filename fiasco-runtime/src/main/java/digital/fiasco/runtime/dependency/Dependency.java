package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.interfaces.naming.Named;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.Repository;

/**
 * A dependency is an artifact (optionally in some repository) that has a list of {@link #dependencies()}, which must be
 * resolved for it to function.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface Dependency extends Named
{
    /**
     * The artifact descriptor for this dependency
     */
    ArtifactDescriptor artifactDescriptor();

    /**
     * @return The objects that this depends on
     */
    DependencyList<?> dependencies();

    /**
     * Gets all dependencies of the given type
     *
     * @return The dependencies
     */
    default <T extends Dependency> DependencyList<T> dependencies(Class<T> type)
    {
        var dependencies = new DependencyList<T>();
        for (var at : dependencies())
        {
            if (type.isAssignableFrom(at.getClass()))
            {
                //noinspection unchecked
                dependencies.add((T) at);
            }
        }
        return dependencies;
    }

    /**
     * Returns the repository where this dependency was found. If this dependency is a {@link Builder}, the returned
     * repository will be null.
     *
     * @return The repository hosting this dependency, or null if the dependency is a {@link Builder}
     */
    Repository repository();
}
