package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.interfaces.naming.Named;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.Asset;
import digital.fiasco.runtime.dependency.artifact.Library;
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
     * Gets all artifact dependencies
     *
     * @return The dependencies
     */
    default DependencyList<Artifact<?>> artifactDependencies()
    {
        var dependencies = new DependencyList<Artifact<?>>();
        for (var at : dependencies())
        {
            if (at instanceof Artifact<?> asset)
            {
                dependencies.add(asset);
            }
        }
        return dependencies;
    }

    /**
     * The artifact descriptor for this dependency
     */
    ArtifactDescriptor artifactDescriptor();

    /**
     * Gets all asset dependencies
     *
     * @return The dependencies
     */
    default DependencyList<Asset> assetDependencies()
    {
        var dependencies = new DependencyList<Asset>();
        for (var at : dependencies())
        {
            if (at instanceof Asset asset)
            {
                dependencies.add(asset);
            }
        }
        return dependencies;
    }

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
     * @return The objects that this depends on
     */
    DependencyList<?> dependencies();

    /**
     * Gets all library dependencies
     *
     * @return The dependencies
     */
    default DependencyList<Library> libraryDependencies()
    {
        var dependencies = new DependencyList<Library>();
        for (var at : dependencies())
        {
            if (at instanceof Library asset)
            {
                dependencies.add(asset);
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
