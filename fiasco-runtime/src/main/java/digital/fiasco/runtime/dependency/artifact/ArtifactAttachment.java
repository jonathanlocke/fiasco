package digital.fiasco.runtime.dependency.artifact;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an artifact content attachment
 *
 * @param artifact The artifact that owns this attachment
 * @param type The attachment name type
 * @param content The attached content
 */
public record ArtifactAttachment(@NotNull Artifact<?> artifact,
                                 @NotNull ArtifactAttachmentType type,
                                 ArtifactContent content)
{

    public ArtifactAttachment(Artifact<?> artifact, ArtifactAttachmentType type)
    {
        this(artifact, type, null);
    }

    public ArtifactAttachment withContent(ArtifactContent content)
    {
        return new ArtifactAttachment(artifact, type, content);
    }

    public ArtifactAttachment withType(ArtifactAttachmentType type)
    {
        return new ArtifactAttachment(artifact, type, content);
    }
}
