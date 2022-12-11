package digital.fiasco.runtime.repository.cache;

import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import digital.fiasco.runtime.repository.artifact.ArtifactSignature;

/**
 * Holds the metadata for a single artifact resource attachment (a class, javadoc, or source jar)
 *
 * @param signature The resource signature
 * @param offset The offset of the resource in the binary resources file
 * @param lastModified The time of last modification of the resource
 * @param size The size of the resource
 */
@SuppressWarnings("unused")
public record ContentMetadata
        (
                ArtifactSignature signature,
                long offset,
                Time lastModified,
                Bytes size
        )
{
    public ContentMetadata withLastModified(Time lastModified)
    {
        return new ContentMetadata(signature, offset, lastModified, size);
    }

    public ContentMetadata withOffset(long offset)
    {
        return new ContentMetadata(signature, offset, lastModified, size);
    }

    public ContentMetadata withSize(long offset)
    {
        return new ContentMetadata(signature, offset, lastModified, size);
    }
}
