package digital.fiasco.runtime.repository;

import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.resource.Resource;
import digital.fiasco.runtime.repository.artifact.ArtifactContentMetadata;
import digital.fiasco.runtime.repository.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.artifact.ArtifactMetadata;

/**
 * Interface to a repository that stores libraries
 *
 * <p><b>Retrieving</b></p>
 *
 * <ul>
 *     <li>{@link #metadata(ArtifactDescriptor)} - Gets the {@link ArtifactMetadata} for the given descriptor</li>
 *     <li>{@link #content(ArtifactMetadata, ArtifactContentMetadata, String)} - Gets the cached resource for the given artifact and content metadata</li>
 * </ul>
 *
 * <p><b>Adding and Removing</b></p>
 *
 * <ul>
 *     <li>{@link #clear()} - Removes all data from this repository</li>
 *     <li>{@link #metadata(ArtifactDescriptor)} - Gets the {@link ArtifactMetadata} for the given descriptor</li>
 *     <li>{@link #content(ArtifactMetadata, ArtifactContentMetadata, String)} - Gets the cached resource for the given content metadata</li>
 * </ul>
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface Repository extends Repeater, Named
{
    /**
     * Adds the given content {@link Resource}s to content.bin, and the {@link ArtifactMetadata} metadata to
     * metadata.txt in JSON format.
     *
     * @param metadata The cache entry metadata to append to metadata.txt in JSON format
     * @param jar The jar resource to add to content.bin
     * @param javadoc The javadoc resource to add to content.bin
     * @param source The source resource to add to content.bin
     */
    boolean add(ArtifactMetadata metadata, Resource jar, Resource javadoc, Resource source);

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
    Resource content(ArtifactMetadata metadata, ArtifactContentMetadata content, String suffix);

    /**
     * Gets the cache entry for the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The cache entry for the descriptor
     */
    ArtifactMetadata metadata(ArtifactDescriptor descriptor);
}
