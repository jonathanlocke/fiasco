package digital.fiasco.runtime.dependency.artifact;

import org.jetbrains.annotations.NotNull;

public record ArtifactAttachment(@NotNull Artifact<?> artifact,
                                 @NotNull String suffix,
                                 ArtifactContent content)
{
    /** Suffix for javadoc attachment */
    public static final String JAVADOC_SUFFIX = "-javadoc.jar";

    /** Suffix for jar attachment */
    public static final String CONTENT_SUFFIX = ".jar";

    /** Suffix for sources attachment */
    public static final String SOURCES_SUFFIX = "-sources.jar";

    public ArtifactAttachment(Artifact<?> artifact, String suffix)
    {
        this(artifact, suffix, null);
    }

    public ArtifactAttachment withContent(ArtifactContent content)
    {
        return new ArtifactAttachment(artifact, suffix, content);
    }

    public ArtifactAttachment withSuffix(String suffix)
    {
        return new ArtifactAttachment(artifact, suffix, content);
    }
}
