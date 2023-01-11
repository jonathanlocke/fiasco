package digital.fiasco.runtime.dependency.artifact;

public enum ArtifactAttachmentType
{
    NO_ATTACHMENT(""),
    JAVADOC_ATTACHMENT("-javadoc.jar"),
    JAR_ATTACHMENT(".jar"),
    POM_ATTACHMENT(".pom"),
    SOURCES_ATTACHMENT("-sources.jar");

    private final String suffix;

    ArtifactAttachmentType(String suffix)
    {

        this.suffix = suffix;
    }

    public String suffix()
    {
        return suffix;
    }
}
