package digital.fiasco.runtime.dependency.artifact.attachment;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.attachment.ArtifactAttachmentType.JAR_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.attachment.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.attachment.ArtifactAttachmentType.NO_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.attachment.ArtifactAttachmentType.POM_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.attachment.ArtifactAttachmentType.SOURCES_ATTACHMENT;

public class ArtifactAttachmentTypeTest extends FiascoTest
{
    @Test
    public void testSuffixes()
    {
        ensureEqual(NO_ATTACHMENT.fileSuffix(), "");
        ensureEqual(JAVADOC_ATTACHMENT.fileSuffix(), "-javadoc.jar");
        ensureEqual(JAR_ATTACHMENT.fileSuffix(), ".jar");
        ensureEqual(POM_ATTACHMENT.fileSuffix(), ".pom");
        ensureEqual(SOURCES_ATTACHMENT.fileSuffix(), "-sources.jar");
    }
}
