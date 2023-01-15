package digital.fiasco.runtime.dependency.artifact;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_PUBLIC;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Holds the content for a single artifact attachment, for example the main JAR, Javadoc, or source code.
 *
 * @param name The name of the artifact resource
 * @param signatures The signatures for this content
 * @param resourceIdentifier The resource containing the content
 * @param offset The offset of this content in a cache file (if any)
 * @param lastModified The time of last modification of the resource
 * @param size The size of the content
 */
@SuppressWarnings("unused")
@TypeQuality
    (
        documentation = DOCUMENTED,
        testing = TESTED,
        stability = STABLE
    )
public record ArtifactContent
    (
        @FormatProperty @Expose String name,
        @FormatProperty @Expose ArtifactContentSignatures signatures,
        @FormatProperty @Expose ResourceIdentifier resourceIdentifier,
        @FormatProperty @Expose long offset,
        @FormatProperty @Expose LocalTime lastModified,
        @FormatProperty @Expose Bytes size
    )
{
    public static ArtifactContent content()
    {
        return new ArtifactContent(null, null, null, -1, null, null);
    }

    public Resource resource()
    {
        return resourceIdentifier.resolve(throwingListener());
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * Returns a copy of this artifact content with the given last modified time
     *
     * @param lastModified The new last modified time
     * @return The new artifact content
     */
    public ArtifactContent withLastModified(LocalTime lastModified)
    {
        return new ArtifactContent(name, signatures, resourceIdentifier, offset, lastModified, size);
    }

    /**
     * Returns a copy of this artifact content with the given offset
     *
     * @param name The content's name
     * @return The new artifact content
     */
    public ArtifactContent withName(String name)
    {
        return new ArtifactContent(name, signatures, resourceIdentifier, offset, lastModified, size);
    }

    /**
     * Returns a copy of this artifact content with the given offset
     *
     * @param offset The new content offset
     * @return The new artifact content
     */
    public ArtifactContent withOffset(long offset)
    {
        return new ArtifactContent(name, signatures, resourceIdentifier, offset, lastModified, size);
    }

    /**
     * Returns a copy of this artifact content with the given offset
     *
     * @param resource The content
     * @return The new artifact content
     */
    public ArtifactContent withResource(Resource resource)
    {
        return new ArtifactContent(name, signatures, resource.identifier(), offset, lastModified, size);
    }

    /**
     * Returns a copy of this artifact content with the given offset
     *
     * @param signatures The content's hash signatures
     * @return The new artifact content
     */
    public ArtifactContent withSignatures(ArtifactContentSignatures signatures)
    {
        return new ArtifactContent(name, signatures, resourceIdentifier, offset, lastModified, size);
    }

    /**
     * Returns a copy of this artifact content with the given size
     *
     * @param size The new size
     * @return The new artifact content
     */
    public ArtifactContent withSize(Bytes size)
    {
        return new ArtifactContent(name, signatures, resourceIdentifier, offset, lastModified, size);
    }
}
