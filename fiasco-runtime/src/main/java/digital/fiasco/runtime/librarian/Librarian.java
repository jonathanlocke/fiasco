package digital.fiasco.runtime.librarian;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.string.Described;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;

import static com.telenav.kivakit.core.version.Version.version;

/**
 * Resolves artifacts.
 *
 * <p><b>Artifact Pinning</b></p>
 *
 * <p>
 * Artifacts can be globally 'pinned' to a particular version using
 * {@link #withPinnedVersion(ArtifactDescriptor, Version)} or one of the convenient overloads of that method.
 * </p>
 *
 * @author Jonathan Locke
 */
public interface Librarian extends
    Repeater,
    Described
{
    /**
     * Returns a copy of this librarian
     *
     * @return The copy
     */
    Librarian copy();

    /**
     * Resolves the given artifact descriptors
     *
     * @param descriptors The descriptors
     * @return The library
     */
    ArtifactList resolve(ObjectList<ArtifactDescriptor> descriptors);

    /**
     * Resolves the given artifact, returning a list of the artifact and all of its transitive dependencies
     *
     * @param artifact The artifact
     * @return The artifact and all of its dependencies
     */
    ArtifactList resolve(Artifact<?> artifact);

    /**
     * Globally pins all artifacts matching the given artifact descriptor to the specified version. If the descriptor is
     * missing its version component, it will match all artifacts with the descriptor's type, group and name. For
     * example, "library:com.telenav.kivakit:kivakit-core:" will match all kivakit-core library dependencies. If the
     * type and artifact name are omitted as well, the descriptor will match all members of the given group, as in
     * ":com.telenav.kivakit::". This makes it easy to ensure that several dependencies from a provider are all pinned
     * to the same version. For example, withPinnedVersion(":org.apache.wicket::", "9.0") would pin all Apache Wicket
     * libraries and assets to the version 9.0.
     *
     * @param descriptor The group and artifact (but no version) to be used in matching artifacts
     * @param version The version to enforce for the descriptor
     */
    Librarian withPinnedVersion(ArtifactDescriptor descriptor, Version version);

    default Librarian withPinnedVersion(Artifact<?> artifact, Version version)
    {
        return withPinnedVersion(artifact.descriptor(), version);
    }

    default Librarian withPinnedVersion(Artifact<?> artifact, String version)
    {
        return withPinnedVersion(artifact.descriptor(), version(version));
    }

    default Librarian withPinnedVersion(String descriptor, String version)
    {
        return withPinnedVersion(ArtifactDescriptor.descriptor(descriptor), version(version));
    }
}
