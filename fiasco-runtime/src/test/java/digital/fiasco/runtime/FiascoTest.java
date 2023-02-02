package digital.fiasco.runtime;

import com.telenav.kivakit.filesystem.Folders;
import com.telenav.kivakit.serialization.gson.GsonFactory;
import com.telenav.kivakit.testing.UnitTest;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContentSignatures;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;
import digital.fiasco.runtime.repository.local.LocalRepository;
import digital.fiasco.runtime.repository.remote.server.serialization.FiascoGsonFactory;

import static com.telenav.kivakit.filesystem.File.file;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachment.attachment;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.JAR_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.SOURCES_ATTACHMENT;
import static digital.fiasco.runtime.dependency.collections.lists.ArtifactList.artifacts;
import static digital.fiasco.runtime.dependency.artifact.types.Asset.asset;
import static digital.fiasco.runtime.dependency.artifact.types.Library.library;

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
            .withDependencies(kivakitCore(), kivakitResource())
            .withDependencies(kivakitIcons())
            .withDependencies(kivakitLogos());
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

    protected LocalRepository localRepository()
    {
        return new LocalRepository("local", Folders.currentFolder().folder("target/.fiasco/local"));
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

    protected void testSerialization(Object object)
    {
        var gson = require(GsonFactory.class).gson();
        var serialized = gson.toJson(object);
        ensureEqual(gson.fromJson(serialized, object.getClass()), object);
    }
}
