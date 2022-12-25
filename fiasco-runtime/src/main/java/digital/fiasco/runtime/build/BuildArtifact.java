package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.version.Version;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.version.Version.version;

/**
 * Provides access to information about a build artifact.
 *
 * <p><b>Artifact Descriptors</b></p>
 *
 * <ul>
 *     <li>{@link #artifactDescriptor()}</li>
 *     <li>{@link #artifactName()}</li>
 *     <li>{@link #artifactVersion()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withArtifactDescriptor(String)}</li>
 *     <li>{@link #withArtifactDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withArtifactVersion(String)}</li>
 *     <li>{@link #withArtifactVersion(Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface BuildArtifact
{
    /**
     * Returns the artifact descriptor for this build
     */
    ArtifactDescriptor artifactDescriptor();

    /**
     * Returns the name of this artifact
     *
     * @return The artifact name
     */
    @NotNull
    default String artifactName()
    {
        return artifactDescriptor().name();
    }

    /**
     * Returns the artifact descriptor for this build
     */
    default Version artifactVersion()
    {
        return artifactDescriptor().version();
    }

    /**
     * Returns a copy of this build with the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    Build withArtifactDescriptor(ArtifactDescriptor descriptor);

    /**
     * Returns a copy of this build with the given main artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    default Build withArtifactDescriptor(String descriptor)
    {
        return withArtifactDescriptor(ArtifactDescriptor.artifactDescriptor(descriptor));
    }

    /**
     * Returns a copy of this build with the given artifact identifier
     *
     * @param identifier The artifact identifier
     * @return The copy
     */
    default Build withArtifactIdentifier(String identifier)
    {
        return withArtifactDescriptor(artifactDescriptor().withIdentifier(identifier));
    }

    /**
     * Returns a copy of this build with the given main artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    default Build withArtifactVersion(Version version)
    {
        return withArtifactDescriptor(artifactDescriptor().withVersion(version));
    }

    /**
     * Returns a copy of this build with the given main artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    default Build withArtifactVersion(String version)
    {
        return withArtifactVersion(version(version));
    }
}
