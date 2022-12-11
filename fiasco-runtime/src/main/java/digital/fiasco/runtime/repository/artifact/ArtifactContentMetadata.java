package digital.fiasco.runtime.repository.artifact;

import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;

/**
 * Holds the metadata for a single artifact resource attachment (a class, javadoc, or source jar)
 *
 * @param signatures The resource signatures
 * @param offset The offset of the resource in the binary resources file
 * @param lastModified The time of last modification of the resource
 * @param size The size of the resource
 */
@SuppressWarnings("unused")
public record ArtifactContentMetadata
        (
                ArtifactSignatures signatures,
                long offset,
                Time lastModified,
                Bytes size
        )
{
    public ArtifactContentMetadata withLastModified(Time lastModified)
    {
        return new ArtifactContentMetadata(signatures, offset, lastModified, size);
    }

    public ArtifactContentMetadata withOffset(long offset)
    {
        return new ArtifactContentMetadata(signatures, offset, lastModified, size);
    }

    public ArtifactContentMetadata withSize(long offset)
    {
        return new ArtifactContentMetadata(signatures, offset, lastModified, size);
    }
}
