package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.interfaces.naming.Named;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.librarian.MultiRepositoryLibrarian;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;
import digital.fiasco.runtime.dependency.collections.DependencyTree;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;
import digital.fiasco.runtime.dependency.collections.lists.AssetList;
import digital.fiasco.runtime.dependency.collections.lists.BaseDependencyList;
import digital.fiasco.runtime.dependency.collections.lists.BuilderList;
import digital.fiasco.runtime.dependency.collections.lists.DependencyList;
import digital.fiasco.runtime.dependency.collections.lists.LibraryList;
import digital.fiasco.runtime.repository.Repository;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;

/**
 * A dependency is either a {@link Builder}, or an {@link Artifact} with an associated {@link #repository()}. An
 * {@link Artifact} can be either an {@link Asset} or a {@link Library}. Each dependency can have its own list of
 * {@link #artifactDependencies()} and/or {@link #builderDependencies()}, but circular dependencies are not allowed.
 * Specific kinds of dependencies can be retrieved with {@link #assets()}, {@link #libraries()}.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #descriptor()}</li>
 *     <li>{@link #repository()}</li>
 *     <li>{@link #artifactDependencies()}</li>
 *     <li>{@link #builderDependencies()} ()}</li>
 * </ul>
 *
 * <p><b>Filtering</b></p>
 *
 * <ul>
 *     <li>{@link #assets()}</li>
 *     <li>{@link #libraries()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see BaseDependencyList
 * @see DependencyTree
 * @see Builder
 * @see Artifact
 * @see Asset
 * @see Library
 * @see MultiRepositoryLibrarian
 */
@SuppressWarnings("unused")
public interface Dependency extends
    Named,
    Comparable<Dependency>
{
    /**
     * Returns all dependencies, both artifacts and builders of this dependency
     *
     * @return The dependencies
     */
    default DependencyList allDependencies()
    {
        return artifactDependencies().asDependencyList()
            .with(builderDependencies().asDependencyList());
    }

    /**
     * Returns all {@link Artifact} dependencies of this dependency
     *
     * @return The dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    ArtifactList artifactDependencies();

    /**
     * Gets all asset dependencies
     *
     * @return The dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default AssetList assets()
    {
        var assets = new AssetList();
        for (var at : artifactDependencies())
        {
            if (at instanceof Asset asset)
            {
                assets = assets.with(asset);
            }
        }
        return assets;
    }

    /**
     * Returns all {@link Builder} dependencies of this dependency
     *
     * @return The dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    BuilderList builderDependencies();

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
     * The artifact descriptor for this dependency
     */
    ArtifactDescriptor descriptor();

    /**
     * Gets all library dependencies
     *
     * @return The dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default LibraryList libraries()
    {
        var libraries = new LibraryList();
        for (var at : artifactDependencies())
        {
            if (at instanceof Library asset)
            {
                libraries = libraries.with(asset);
            }
        }
        return libraries;
    }

    /**
     * Returns the repository where this dependency was found. If this dependency is a {@link Builder}, the returned
     * repository will be null.
     *
     * @return The repository hosting this dependency, or null if the dependency is a {@link Builder}
     */
    Repository repository();

    /**
     * Returns the JSON representation for this dependency
     *
     * @return JSON text
     */
    String toJson();
}
