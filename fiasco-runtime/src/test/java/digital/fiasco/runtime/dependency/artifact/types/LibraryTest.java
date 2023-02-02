package digital.fiasco.runtime.dependency.artifact.types;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.JAR_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.SOURCES_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor.descriptor;
import static digital.fiasco.runtime.dependency.collections.lists.LibraryList.libraries;

public class LibraryTest extends FiascoTest
{
    @Test
    public void testAttachments()
    {
        var library = library();

        library = library
            .withAttachment(jarAttachment())
            .withAttachment(sourcesAttachment())
            .withAttachment(javadocAttachment());

        ensureEqual(library.attachments().size(), 3);

        ensureEqual(library.attachmentOfType(JAR_ATTACHMENT), jarAttachment());
        ensureEqual(library.attachmentOfType(SOURCES_ATTACHMENT), sourcesAttachment());
        ensureEqual(library.attachmentOfType(JAVADOC_ATTACHMENT), javadocAttachment());

        ensure(library.attachmentOfType(JAR_ATTACHMENT).content().equals(library.jar()));
        ensure(library.attachmentOfType(JAVADOC_ATTACHMENT).content().equals(library.javadoc()));
        ensure(library.attachmentOfType(SOURCES_ATTACHMENT).content().equals(library.javadoc()));

        ensure(library.attachments().contains(jarAttachment()));
        ensure(library.attachments().contains(sourcesAttachment()));
        ensure(library.attachments().contains(javadocAttachment()));
    }

    @Test
    public void testCopy()
    {
        {
            var library = Library.library("a:b:1");
            ensureEqual(library, library.copy());
        }
        {
            var library = Library.library("a::1");
            ensureEqual(library, library.copy());
        }
        {
            var library = Library.library("a:b:");
            ensureEqual(library, library.copy());
        }
        {
            var library = Library.library("a::");
            ensureEqual(library, library.copy());
        }
    }

    @Test
    public void testCreation()
    {
        ensureNotNull(Library.library("x:y:1.0"));
        ensureThrows(() -> Library.library("?"));
        {
            var librarys = libraries("x:y:1.0", "a:b:0.1");
            ensureEqual(librarys.size(), 2);
            ensureEqual(librarys.get(0), Library.library("x:y:1.0"));
        }
        {
            var librarys = libraries(Library.library("x:y:1.0"), Library.library("a:b:0.1"));
            ensureEqual(librarys.size(), 2);
            ensureEqual(librarys.get(0), Library.library("x:y:1.0"));
        }
    }

    @Test
    public void testDeduplicate()
    {
        ensure(libraries(kivakitCore(), kivakitCore(), kivakitCore()).deduplicated()
            .equals(libraries(kivakitCore())));
        ensure(!libraries(kivakitCore(), kivakitCore(), kivakitApplication())
            .equals(libraries(kivakitApplication(), kivakitCore())));
    }

    @Test
    public void testDependencies()
    {
        var library = library()
            .withDependencies(ArtifactList.artifacts(Library.library("a:b:1.0")));
        ensureEqual(library.artifactDependencies().size(), 1);
        ensureEqual(library.artifactDependencies().get(0).descriptor(), descriptor("library:a:b:1.0"));
    }

    @Test
    public void testDescriptor()
    {
        var library = library();
        ensureEqual(library.descriptor(), descriptor("library:x:y:1.0"));
    }

    @Test
    public void testWith()
    {
        {
            var library = library().withSources(packageContent());
            ensure(library.sources().equals(packageContent()));
        }
        {
            var library = library().withJavadoc(packageContent());
            ensure(library.javadoc().equals(packageContent()));
        }
        {
            var library = library().withJar(packageContent());
            ensure(library.jar().equals(packageContent()));
        }
    }

    @NotNull
    private Library library()
    {
        return Library.library("x:y:1.0");
    }
}
