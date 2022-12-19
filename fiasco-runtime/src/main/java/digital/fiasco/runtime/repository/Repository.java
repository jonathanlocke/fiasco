package digital.fiasco.runtime.repository;

import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.resource.Resource;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactResources;

/**
 * Interface to a repository that stores libraries
 *
 * <p><b>Retrieving Artifacts and Content</b></p>
 *
 * <ul>
 *     <li>{@link #resolve(ArtifactDescriptor)} - Gets the {@link Artifact} for the given descriptor</li>
 *     <li>{@link #content(Artifact, ArtifactContent, String)} - Gets the cached resource for the given artifact and content metadata</li>
 * </ul>
 *
 * <p><b>Adding and Removing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #add(Artifact, ArtifactResources)} - Adds the given artifact with the given attached resources</li>
 *     <li>{@link #clear()} - Removes all data from this repository</li>
 * </ul>
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface Repository extends Repeater, Named
{
    /**
     * Adds the given content {@link Resource}s to content.bin, and the {@link Artifact} metadata to metadata.txt in
     * JSON format.
     *
     * @param metadata The cache entry metadata to append to metadata.txt in JSON format
     * @param resources The resources to add to content.bin
     */
    boolean add(Artifact metadata, ArtifactResources resources);

    /**
     * Removes all data from this repository
     */
    void clear();

    /**
     * Returns the section of the binary resources file containing the given artifact
     *
     * @param metadata The artifact metadata
     * @param content The content metadata, including its offset and size
     * @param suffix A suffix to add to the resource path
     * @return The resource section for the artifact
     */
    Resource content(Artifact metadata, ArtifactContent content, String suffix);

    /**
     * Gets the cache entry for the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The cache entry for the descriptor
     */
    Artifact resolve(ArtifactDescriptor descriptor);
}
