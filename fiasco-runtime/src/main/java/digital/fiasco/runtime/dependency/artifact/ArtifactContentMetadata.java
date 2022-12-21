package digital.fiasco.runtime.dependency.artifact;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import digital.fiasco.runtime.repository.fiasco.FiascoRepository;

/**
 * Holds the metadata for a single artifact resource attachment (a class, javadoc, or source jar)
 *
 * @param signatures The signatures for this content
 * @param offset The offset of this content in the {@link FiascoRepository}
 * @param lastModified The time of last modification of the resource
 * @param size The size of the content
 */
@SuppressWarnings("unused")
public record ArtifactContentMetadata
        (
                @Expose ArtifactSignatures signatures,
                @Expose long offset,
                @Expose Time lastModified,
                @Expose Bytes size
        )
{
    /**
     * Returns a copy of this artifact content with the given last modified time
     *
     * @param lastModified The new last modified time
     * @return The new artifact content
     */
    public ArtifactContentMetadata withLastModified(Time lastModified)
    {
        return new ArtifactContentMetadata(signatures, offset, lastModified, size);
    }

    /**
     * Returns a copy of this artifact content with the given offset
     *
     * @param offset The new content offset
     * @return The new artifact content
     */
    public ArtifactContentMetadata withOffset(long offset)
    {
        return new ArtifactContentMetadata(signatures, offset, lastModified, size);
    }

    /**
     * Returns a copy of this artifact content with the given size
     *
     * @param size The new size
     * @return The new artifact content
     */
    public ArtifactContentMetadata withSize(Bytes size)
    {
        return new ArtifactContentMetadata(signatures, offset, lastModified, size);
    }
}
