package digital.fiasco.runtime.dependency.artifact.attachment;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContent;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.language.Hash.hashMany;
import static com.telenav.kivakit.core.language.Objects.areEqualPairs;

/**
 * Represents an artifact content attachment
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #attachment(ArtifactAttachmentType)}</li>
 *     <li>{@link #attachment(ArtifactAttachmentType, ArtifactContent)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #artifact()}</li>
 *     <li>{@link #attachmentType()}</li>
 *     <li>{@link #content()}</li>
 *     <li>{@link #withArtifact(Artifact)}</li>
 *     <li>{@link #withContent(ArtifactContent)}</li>
 *     <li>{@link #withType(ArtifactAttachmentType)}</li>
 * </ul>
 *
 * @param artifact The artifact that owns this attachment
 * @param attachmentType The attachment name type
 * @param content The attached content
 * @see ArtifactAttachment
 */
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public record ArtifactAttachment
    (
        Artifact<?> artifact,
        @Expose @FormatProperty @NotNull ArtifactAttachmentType attachmentType,
        @Expose @FormatProperty ArtifactContent content
    )
{
    /**
     * Creates a new attachment for the given artifact, having the given attachment type and content
     *
     * @param type The type of attachment
     * @param content The content*
     * @return The attachment
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static ArtifactAttachment attachment(ArtifactAttachmentType type, ArtifactContent content)
    {
        return new ArtifactAttachment(null, type, content);
    }

    /**
     * Creates a new attachment for the given artifact, having the given attachment type
     *
     * @param type The type of attachment
     * @return The attachment
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static ArtifactAttachment attachment(ArtifactAttachmentType type)
    {
        return attachment(type, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean equals(Object object)
    {
        if (object instanceof ArtifactAttachment that)
        {
            return areEqualPairs(attachmentType, that.attachmentType, content, that.content);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public int hashCode()
    {
        return hashMany(attachmentType, content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * Returns a copy of this attachment with the given artifact
     *
     * @param artifact The artifact that this attachment belongs to
     * @return The attachment
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactAttachment withArtifact(Artifact<?> artifact)
    {
        return new ArtifactAttachment(artifact, attachmentType, content);
    }

    /**
     * Returns a copy of this attachment with the given content
     *
     * @param content The content for this attachment
     * @return The attachment
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactAttachment withContent(ArtifactContent content)
    {
        return new ArtifactAttachment(artifact, attachmentType, content);
    }

    /**
     * Returns a copy of this attachment with the given attachment type
     *
     * @param type The type of this attachment
     * @return The attachment
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactAttachment withType(ArtifactAttachmentType type)
    {
        return new ArtifactAttachment(artifact, type, content);
    }
}
