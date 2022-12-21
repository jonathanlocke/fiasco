package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import digital.fiasco.runtime.repository.download.DownloadRepository;

/**
 * Holds the metadata for a single artifact resource attachment (a class, javadoc, or source jar)
 *
 * @param signatures The signatures for this content
 * @param offset The offset of this content in the {@link DownloadRepository}
 * @param lastModified The time of last modification of the resource
 * @param size The size of the content
 */
@SuppressWarnings("unused")
public record ArtifactContent
        (
                ArtifactSignatures signatures,
                long offset,
                Time lastModified,
                Bytes size
        )
{
    /**
     * Returns a copy of this artifact content with the given last modified time
     *
     * @param lastModified The new last modified time
     * @return The new artifact content
     */
    public ArtifactContent withLastModified(Time lastModified)
    {
        return new ArtifactContent(signatures, offset, lastModified, size);
    }

    /**
     * Returns a copy of this artifact content with the given offset
     *
     * @param offset The new content offset
     * @return The new artifact content
     */
    public ArtifactContent withOffset(long offset)
    {
        return new ArtifactContent(signatures, offset, lastModified, size);
    }

    /**
     * Returns a copy of this artifact content with the given size
     *
     * @param size The new size
     * @return The new artifact content
     */
    public ArtifactContent withSize(Bytes size)
    {
        return new ArtifactContent(signatures, offset, lastModified, size);
    }
}
