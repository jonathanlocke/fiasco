package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.collections.map.ObjectMap;
import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAR_ATTACHMENT;

public class ArtifactAttachmentTest extends FiascoTest
{
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
    }

    @Test
    public void testToString()
    {
        var text = jarAttachment().toString();
        ensure(text.contains("content"));
        ensure(text.contains("resource"));
        ensure(text.contains("created"));
        ensure(text.contains("lastModified"));
        ensure(text.contains("text"));
        ensure(text.contains("name"));
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
            .withContent(content())
            .withType(JAR_ATTACHMENT);

        ensureEqual(attachment.attachmentType(), JAR_ATTACHMENT);
        ensureEqual(attachment.artifact(), null);
        ensureEqual(attachment.content(), content());
    }
}
