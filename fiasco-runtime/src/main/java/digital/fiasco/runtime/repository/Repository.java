package digital.fiasco.runtime.repository;

import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.resource.Resource;
import digital.fiasco.runtime.repository.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.artifact.ArtifactMetadata;
import digital.fiasco.runtime.repository.artifact.ArtifactContentMetadata;

/**
 * Interface to a repository that stores libraries
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface Repository extends Repeater
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
     * @param artifact The artifact metadata, including its offset and size
     * @return The resource section for the artifact
     */
    Resource content(ArtifactContentMetadata artifact);

    /**
     * Gets the cache entry for the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The cache entry for the descriptor
     */
    ArtifactMetadata metadata(ArtifactDescriptor descriptor);
}
