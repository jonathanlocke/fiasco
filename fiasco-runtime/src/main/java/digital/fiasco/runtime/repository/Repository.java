package digital.fiasco.runtime.repository;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.resource.Resource;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;

import java.net.URI;
import java.util.Collection;

/**
 * Interface to a repository that stores and resolves artifacts and their content attachments.
 *
 * <p><b>Retrieving Artifacts and Content</b></p>
 *
 * <ul>
 *     <li>{@link #resolveArtifacts(Collection)} - Resolves a list of descriptors into a list of {@link Artifact}s, including content attachments</li>
 * </ul>
 *
 * <p><b>Adding and Removing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #installArtifact(Artifact)} - Adds the given artifact and its content</li>
 *     <li>{@link #clearArtifacts()} - Removes all data from this repository</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface Repository extends Repeater, Named
{
    /**
     * Removes all artifacts from this repository
     *
     * @throws IllegalArgumentException Thrown if the repository cannot be cleared
     */
    void clearArtifacts();

    /**
     * Adds the given content {@link Resource}s to content.bin, and the {@link Artifact} metadata to metadata.txt in
     * JSON format.
     *
     * @param artifact The cache entry metadata to append to metadata.txt in JSON format
     * @throws IllegalStateException Thrown if the artifact cannot be installed in this repository
     */
    void installArtifact(Artifact<?> artifact);

    /**
     * Returns true if this is a remote repository
     */
    boolean isRemote();

    /**
     * Resolves each artifact descriptor to an {@link Artifact} complete with content attachments
     *
     * @param descriptors The artifact descriptors
     * @return The resolved artifacts
     * @throws IllegalArgumentException Thrown if any descriptor cannot be resolved
     */
    ObjectList<Artifact<?>> resolveArtifacts(Collection<ArtifactDescriptor> descriptors);

    /**
     * Returns the URI of this repository
     *
     * @return The repository URI
     */
    URI uri();
}
