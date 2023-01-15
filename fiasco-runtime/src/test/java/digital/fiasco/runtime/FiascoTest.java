package digital.fiasco.runtime;

import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.testing.UnitTest;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactContentSignatures;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.dependency.artifact.Asset;
import digital.fiasco.runtime.dependency.artifact.Library;
import digital.fiasco.runtime.serialization.FiascoGsonFactory;

import static com.telenav.kivakit.core.time.Day.dayOfMonth;
import static com.telenav.kivakit.core.time.Hour.hourOfDay;
import static com.telenav.kivakit.core.time.Meridiem.AM;
import static com.telenav.kivakit.core.time.Month.JANUARY;
import static com.telenav.kivakit.core.time.Year.year;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.attachment;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAR_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.SOURCES_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifactList;
import static digital.fiasco.runtime.dependency.artifact.Asset.asset;
import static digital.fiasco.runtime.dependency.artifact.Library.library;

public class FiascoTest extends UnitTest
{
    public FiascoTest()
    {
        register(new FiascoGsonFactory(this));
    }

    protected ArtifactContent content()
    {
        var resource = packageFor(FiascoTest.class).resource("content.txt");
        return ArtifactContent.content()
            .withResource(resource)
            .withLastModified(Time.utcTime(
                year(2023),
                JANUARY,
                dayOfMonth(12),
                hourOfDay(6, AM)).asLocalTime())
            .withSize(resource.sizeInBytes())
            .withOffset(0)
            .withName(resource.fileName().name())
            .withSignatures(signatures());
    }

    protected ArtifactDescriptor descriptorA()
    {
        return ArtifactDescriptor.descriptor("a::");
    }

    protected ArtifactDescriptor descriptorAb()
    {
        return ArtifactDescriptor.descriptor("a:b:");
    }

    protected ArtifactDescriptor descriptorAbv()
    {
        return ArtifactDescriptor.descriptor("a:b:1.2.3");
    }

    protected ArtifactDescriptor descriptorAv()
    {
        return ArtifactDescriptor.descriptor("a::1.2.3");
    }

    protected ArtifactDescriptor descriptorX()
    {
        return ArtifactDescriptor.descriptor("x::");
    }

    protected ArtifactDescriptor descriptorXv()
    {
        return ArtifactDescriptor.descriptor("x::1.2.3");
    }

    protected ArtifactDescriptor descriptorXy()
    {
        return ArtifactDescriptor.descriptor("x:y:");
    }

    protected ArtifactDescriptor descriptorXyv()
    {
        return ArtifactDescriptor.descriptor("x:y:1.2.3");
    }

    protected ArtifactAttachment jarAttachment()
    {
        return attachment(JAR_ATTACHMENT, content());
    }

    protected ArtifactAttachment javadocAttachment()
    {
        return attachment(JAVADOC_ATTACHMENT, content());
    }

    protected Library kivakitApplication()
    {
        return library("com.telenav.kivakit:kivakit-application:1.8.5");
    }

    protected ArtifactList kivakitArtifacts()
    {
        return artifactList(kivakitCore(), kivakitIcons(), kivakitApplication(), kivakitLogos());
    }

    protected ArtifactList kivakitAssets()
    {
        return artifactList(kivakitLogos(), kivakitIcons());
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
        return artifactList(kivakitApplication(), kivakitCore());
    }

    protected Asset kivakitLogos()
    {
        return asset("com.telenav.kivakit:kivakit-logos:1.8.5");
    }

    protected Library kivakitResource()
    {
        return library("com.telenav.kivakit:kivakit-resource:1.8.5");
    }

    protected ArtifactContentSignatures signatures()
    {
        return new ArtifactContentSignatures("oisdfoiusdfoiu198273", "12983791826501", "120378019821");
    }

    protected ArtifactAttachment sourcesAttachment()
    {
        return attachment(SOURCES_ATTACHMENT, content());
    }
}
