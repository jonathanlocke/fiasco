package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.collections.map.ObjectMap;
import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.attachment;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAR_ATTACHMENT;

public class ArtifactAttachmentTest extends FiascoTest
{
    @Test
    public void testAttachment()
    {
        var attachment = attachment(JAR_ATTACHMENT);
        ensureEqual(attachment.attachmentType(), JAR_ATTACHMENT);
    }

    @Test
    public void testAttachmentOfType()
    {
        var attachment = jarAttachment()
            .withContent(packageContent())
            .withType(JAR_ATTACHMENT);

        var application = kivakitApplication()
            .withAttachment(attachment);

        ensure(application.attachmentOfType(JAR_ATTACHMENT).equals(attachment));
    }

    @Test
    public void testEqualsHashCode()
    {
        var map = new ObjectMap<ArtifactAttachment, Integer>();

        map.put(jarAttachment(), 1);
        map.put(javadocAttachment(), 3);
        map.put(sourcesAttachment(), 2);

        ensureEqual(map.get(jarAttachment()), 1);
        ensureEqual(map.get(sourcesAttachment()), 2);
        ensureEqual(map.get(javadocAttachment()), 3);

        ensureNotEqual(jarAttachment(), 5);
        ensureEqual(jarAttachment(), jarAttachment());
    }

    @Test
    public void testToString()
    {
        var text = jarAttachment().toString();
        ensure(text.contains("content"));
        ensure(text.contains("resourceIdentifier"));
        ensure(text.contains("lastModified"));
        ensure(text.contains("offset"));
        ensure(text.contains("signatures"));
        ensure(text.contains("md5"));
        ensure(text.contains("pgp"));
        ensure(text.contains("sha1"));
        ensure(text.contains("size"));
        ensure(text.contains("attachmentType"));
    }

    @Test
    public void testWith()
    {
        var attachment = jarAttachment()
            .withArtifact(null)
            .withContent(packageContent())
            .withType(JAR_ATTACHMENT);

        ensureEqual(attachment.attachmentType(), JAR_ATTACHMENT);
        ensureEqual(attachment.artifact(), null);
        ensureEqual(attachment.content(), packageContent());
    }
}
