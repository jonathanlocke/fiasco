package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.interfaces.naming.Named;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.dependency.artifact.Asset;
import digital.fiasco.runtime.dependency.artifact.Library;
import digital.fiasco.runtime.repository.Repository;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;

/**
 * A dependency is either a {@link Builder}, or an {@link Artifact} with an associated {@link #repository()}. An
 * {@link Artifact} can be either an {@link Asset} or a {@link Library}. Each dependency can have its own list of
 * {@link #dependencies()}, but circular dependencies are not allowed. Specific kinds of dependencies can be retrieved
 * with the filter methods {@link #assetDependencies()}, {@link #libraryDependencies()}, and
 * {@link #dependencies(Class)}.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #descriptor()}</li>
 *     <li>{@link #repository()}</li>
 *     <li>{@link #dependencies()}</li>
 * </ul>
 *
 * <p><b>Filtering</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies(Class)}</li>
 *     <li>{@link #assetDependencies()}</li>
 *     <li>{@link #libraryDependencies()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see DependencyList
 * @see DependencyTree
 * @see Builder
 * @see Artifact
 * @see Asset
 * @see Library
 * @see Librarian
 */
@SuppressWarnings("unused")
public interface Dependency extends
    Named,
    Comparable<Dependency>
{
    /**
     * Gets all artifact dependencies
     *
     * @return The dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default ArtifactList artifactDependencies()
    {
        return dependencies().asArtifactList();
    }

    /**
     * Gets all asset dependencies
     *
     * @return The dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default DependencyList<Asset> assetDependencies()
    {
        var dependencies = new DependencyList<Asset>();
        for (var at : dependencies())
        {
            if (at instanceof Asset asset)
            {
                dependencies = dependencies.with(asset);
            }
        }
        return dependencies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default int compareTo(@NotNull Dependency that)
    {
        return name().compareTo(that.name());
    }

    /**
     * Gets all dependencies of the given type
     *
     * @return The dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default <T extends Dependency> DependencyList<T> dependencies(Class<T> type)
    {
        var dependencies = new DependencyList<T>();
        for (var at : dependencies())
        {
            if (type.isAssignableFrom(at.getClass()))
            {
                //noinspection unchecked
                dependencies = dependencies.with((T) at);
            }
        }
        return dependencies;
    }

    /**
     * @return The objects that this depends on
     */
    DependencyList<?> dependencies();

    /**
     * The artifact descriptor for this dependency
     */
    ArtifactDescriptor descriptor();

    /**
     * Gets all library dependencies
     *
     * @return The dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default DependencyList<Library> libraryDependencies()
    {
        var dependencies = new DependencyList<Library>();
        for (var at : dependencies())
        {
            if (at instanceof Library asset)
            {
                dependencies = dependencies.with(asset);
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
