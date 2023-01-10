package digital.fiasco.runtime.dependency.artifact;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an artifact content attachment
 *
 * @param artifact The artifact that owns this attachment
 * @param suffix The attachment name suffix
 * @param content The attached content
 */
public record ArtifactAttachment(@NotNull Artifact<?> artifact,
                                 @NotNull ArtifactAttachment.AttachmentSuffix suffix,
                                 ArtifactContent content)
{
    public enum AttachmentSuffix
    {
        NO_SUFFIX(""),
        JAVADOC_SUFFIX("-javadoc.jar"),
        CONTENT_SUFFIX(".jar"),
        POM_SUFFIX(".pom"),
        SOURCES_SUFFIX("-sources.jar");

        private final String suffix;

        AttachmentSuffix(String suffix)
        {

            this.suffix = suffix;
        }

        public String suffix()
        {
            return suffix;
        }
    }

    public ArtifactAttachment(Artifact<?> artifact, AttachmentSuffix suffix)
    {
        this(artifact, suffix, null);
    }

    public ArtifactAttachment withContent(ArtifactContent content)
    {
        return new ArtifactAttachment(artifact, suffix, content);
    }

    public ArtifactAttachment withSuffix(AttachmentSuffix suffix)
    {
        return new ArtifactAttachment(artifact, suffix, content);
    }
}
