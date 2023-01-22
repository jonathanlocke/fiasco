package digital.fiasco.runtime;

import com.telenav.kivakit.testing.UnitTest;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactContentSignatures;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.dependency.artifact.Asset;
import digital.fiasco.runtime.dependency.artifact.Library;
import digital.fiasco.runtime.repository.remote.serialization.FiascoGsonFactory;

import static com.telenav.kivakit.core.os.Console.console;
import static com.telenav.kivakit.filesystem.File.file;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.attachment;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAR_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.SOURCES_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifacts;
import static digital.fiasco.runtime.dependency.artifact.Asset.asset;
import static digital.fiasco.runtime.dependency.artifact.Library.library;

public class FiascoTest extends UnitTest
{
    public FiascoTest()
    {
        register(new FiascoGsonFactory());
    }

    protected ArtifactContent badPackageContent()
    {
        var resource = packageFor(FiascoTest.class)
            .resource("content.txt");

        return ArtifactContent.content()
            .withResource(resource)
            .withLastModified(resource.lastModified())
            .withSize(resource.sizeInBytes())
            .withOffset(0)
            .withName(resource.fileName().name())
            .withSignatures(signatures())
            .withResource(null);
    }

    protected ArtifactDescriptor descriptorA()
    {
        return ArtifactDescriptor.descriptor("library:a::");
    }

    protected ArtifactDescriptor descriptorAb()
    {
        return ArtifactDescriptor.descriptor("library:a:b:");
    }

    protected ArtifactDescriptor descriptorAbv()
    {
        return ArtifactDescriptor.descriptor("library:a:b:1.2.3");
    }

    protected ArtifactDescriptor descriptorAv()
    {
        return ArtifactDescriptor.descriptor("library:a::1.2.3");
    }

    protected ArtifactDescriptor descriptorX()
    {
        return ArtifactDescriptor.descriptor("library:x::");
    }

    protected ArtifactDescriptor descriptorXv()
    {
        return ArtifactDescriptor.descriptor("library:x::1.2.3");
    }

    protected ArtifactDescriptor descriptorXy()
    {
        return ArtifactDescriptor.descriptor("library:x:y:");
    }

    protected ArtifactDescriptor descriptorXyv()
    {
        return ArtifactDescriptor.descriptor("library:x:y:1.2.3");
    }

    protected ArtifactContent fileContent()
    {
        var resource = file("src/test/java/digital/fiasco/runtime/content.txt");
        ensure(resource.exists());

        return ArtifactContent.content()
            .withResource(resource)
            .withLastModified(resource.lastModified())
            .withSize(resource.sizeInBytes())
            .withOffset(0)
            .withName(resource.fileName().name())
            .withSignatures(signatures());
    }

    protected ArtifactAttachment jarAttachment()
    {
        return attachment(JAR_ATTACHMENT, packageContent());
    }

    protected ArtifactAttachment javadocAttachment()
    {
        return attachment(JAVADOC_ATTACHMENT, packageContent());
    }

    protected Library kivakitApplication()
    {
        return library("com.telenav.kivakit:kivakit-application:1.8.5")
            .dependsOn(kivakitCore(), kivakitResource())
            .dependsOn(kivakitIcons())
            .dependsOn(kivakitLogos());
    }

    protected ArtifactList kivakitArtifacts()
    {
        return artifacts(kivakitCore(), kivakitIcons(), kivakitApplication(), kivakitLogos());
    }

    protected ArtifactList kivakitAssets()
    {
        return artifacts(kivakitLogos(), kivakitIcons());
    }

    protected Library kivakitCore()
    {
        return library("com.telenav.kivakit:kivakit-core:1.8.5");
    }

    protected Asset kivakitIcons()
    {
        return asset("com.telenav.kivakit:kivakit-icons:1.8.5");
    }

    protected Asset kivakitImages()
    {
        return asset("com.telenav.kivakit:kivakit-images:1.8.5");
    }

    protected ArtifactList kivakitLibraries()
    {
        return artifacts(kivakitApplication(), kivakitCore());
    }

    protected Asset kivakitLogos()
    {
        return asset("com.telenav.kivakit:kivakit-logos:1.8.5");
    }

    protected Library kivakitResource()
    {
        return library("com.telenav.kivakit:kivakit-resource:1.8.5");
    }

    protected ArtifactContent packageContent()
    {
        var resource = packageFor(FiascoTest.class)
            .resource("content.txt");

        return ArtifactContent.content()
            .withResource(resource)
            .withLastModified(resource.lastModified())
            .withSize(resource.sizeInBytes())
            .withOffset(0)
            .withName(resource.fileName().name())
            .withSignatures(signatures());
    }

    protected void println(String message, Object... arguments)
    {
        console().println(message, arguments);
    }

    protected ArtifactContentSignatures signatures()
    {
        return ArtifactContentSignatures.signatures()
            .withAsc("1769187598761516661")
            .withMd5("12983791826501")
            .withSha1("120378019821");
    }

    protected ArtifactAttachment sourcesAttachment()
    {
        return attachment(SOURCES_ATTACHMENT, packageContent());
    }
}
