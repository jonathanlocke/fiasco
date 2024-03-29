package digital.fiasco.runtime.dependency.artifact.content;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;

/**
 * The type of artifact attachment, and its associated suffix. The suffix is what is added to the base name of an
 * artifact to get the filename for a particular kind of content. For example, an artifact attachment of type
 * {@link #JAVADOC_ATTACHMENT} would yield a filename of "kivakit-core-1.6.2-javadoc.jar".
 *
 * @author jonathanl (shibo)
 * @see ArtifactAttachment
 */
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public enum ArtifactAttachmentType
{
    NO_ATTACHMENT(""),
    JAVADOC_ATTACHMENT("-javadoc.jar"),
    JAR_ATTACHMENT(".jar"),
    POM_ATTACHMENT(".pom"),
    SOURCES_ATTACHMENT("-sources.jar");

    /** The suffix to add to a base filename to resolve the name of this type of attachment */
    private final String fileSuffix;

    /**
     * Creates an attachment type with the given file suffix
     *
     * @param fileSuffix The suffix
     */
    ArtifactAttachmentType(String fileSuffix)
    {
        this.fileSuffix = fileSuffix;
    }

    /**
     * Returns the file suffix to use for artifact attachments of this type
     *
     * @return The suffix
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public String fileSuffix()
    {
        return fileSuffix;
    }

    public boolean isJar()
    {
        return fileSuffix.endsWith(".jar");
    }
}
