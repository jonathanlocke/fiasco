package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.interfaces.naming.Named;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.lists.ArtifactList;
import digital.fiasco.runtime.dependency.artifact.artifacts.Asset;
import digital.fiasco.runtime.dependency.artifact.lists.AssetList;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;
import digital.fiasco.runtime.dependency.artifact.lists.LibraryList;
import digital.fiasco.runtime.dependency.queue.DependencyTree;
import digital.fiasco.runtime.repository.Repository;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;

/**
 * A dependency is either a {@link Builder}, or an {@link Artifact} with an associated {@link #repository()}. An
 * {@link Artifact} can be either an {@link Asset} or a {@link Library}. Each dependency can have its own list of
 * {@link #dependencies()}, but circular dependencies are not allowed. Specific kinds of dependencies can be retrieved
 * with the filter methods {@link #assets()}, {@link #libraries()}, and {@link #dependencies(Class)}.
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
 *     <li>{@link #assets()}</li>
 *     <li>{@link #libraries()}</li>
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
    default ArtifactList artifacts()
    {
        return dependencies().asArtifactList();
    }

    /**
     * Gets all asset dependencies
     *
     * @return The dependencies
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default AssetList assets()
    {
        var dependencies = new AssetList();
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
    @SuppressWarnings("unchecked")
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default <D extends Dependency, L extends DependencyList<D, L>> L dependencies(Class<D> type)
    {
        var dependencies = new DependencyList<D, L>();
        for (var at : dependencies())
        {
            if (type.isAssignableFrom(at.getClass()))
            {
                //noinspection unchecked
                dependencies = dependencies.with((D) at);
            }
        }
        return (L) dependencies;
    }

    /**
     * @return The objects that this depends on
     */
    DependencyList<?, ?> dependencies();

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
        var dependencies = new LibraryList();
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
