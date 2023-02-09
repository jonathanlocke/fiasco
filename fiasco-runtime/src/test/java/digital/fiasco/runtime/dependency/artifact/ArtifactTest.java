package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.collections.map.ObjectMap;
import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.version.Version.version;
import static com.telenav.kivakit.interfaces.string.StringFormattable.Format.DEBUG;
import static com.telenav.kivakit.interfaces.string.StringFormattable.Format.USER_LABEL;
import static digital.fiasco.runtime.build.environment.BuildRepositoriesTrait.MAVEN_CENTRAL;
import static digital.fiasco.runtime.dependency.artifact.Artifact.artifactFromJson;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.JAR_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.SOURCES_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor.descriptor;
import static digital.fiasco.runtime.dependency.artifact.types.Asset.asset;
import static digital.fiasco.runtime.dependency.artifact.types.Library.library;
import static digital.fiasco.runtime.dependency.collections.lists.ArtifactDescriptorList.descriptors;
import static digital.fiasco.runtime.dependency.collections.lists.ArtifactList.artifacts;
import static digital.fiasco.runtime.dependency.collections.lists.AssetList.assets;
import static digital.fiasco.runtime.dependency.collections.lists.DependencyList.dependencies;
import static digital.fiasco.runtime.dependency.collections.lists.LibraryList.libraries;

public class ArtifactTest extends FiascoTest
{
    @Test
    public void testArtifact()
    {
        ensureEqual(kivakitCore().artifact().name(), "kivakit-core");
        ensureEqual(kivakitCore().artifact("kivakit-interfaces"),
            library("com.telenav.kivakit:kivakit-interfaces:1.8.5"));
        ensureEqual(kivakitCore().artifact(new ArtifactName("kivakit-interfaces")),
            library("com.telenav.kivakit:kivakit-interfaces:1.8.5"));
    }

    @Test
    public void testAsString()
    {
        ensureEqual(kivakitApplication().asString(USER_LABEL), "library:com.telenav.kivakit:kivakit-application:1.8.5");
        ensure(kivakitApplication().asString(DEBUG).contains("library:com.telenav.kivakit:kivakit-application:1.8.5"));
    }

    @Test
    public void testAttachmentOfType()
    {
        var library = library("com.telenav.kivakit:kivakit:1.8.0");

        library = library.withAttachment(jarAttachment())
            .withAttachment(javadocAttachment())
            .withAttachment(sourcesAttachment());

        {
            var attachment = library.attachmentOfType(JAR_ATTACHMENT);
            ensureEqual(attachment.toString(), jarAttachment().toString());
            ensureEqual(attachment, jarAttachment());
        }
        {
            var attachment = library.attachmentOfType(SOURCES_ATTACHMENT);
            ensureEqual(attachment.toString(), sourcesAttachment().toString());
            ensureEqual(attachment, sourcesAttachment());
        }
        {
            var attachment = library.attachmentOfType(JAVADOC_ATTACHMENT);
            ensureEqual(attachment.toString(), javadocAttachment().toString());
            ensureEqual(attachment, javadocAttachment());
        }
    }

    @Test
    public void testAttachments()
    {
        {
            var library = kivakitCore()
                .withAttachment(jarAttachment())
                .withAttachment(javadocAttachment())
                .withAttachment(sourcesAttachment());

            ensure(library.attachments().contains(jarAttachment()));
            ensure(library.attachments().contains(javadocAttachment()));
            ensure(library.attachments().contains(sourcesAttachment()));
        }
        {
            var attachments = new ObjectMap<ArtifactAttachmentType, ArtifactAttachment>();
            attachments.put(JAR_ATTACHMENT, jarAttachment());
            attachments.put(SOURCES_ATTACHMENT, sourcesAttachment());
            var library = kivakitApplication()
                .withAttachments(attachments);

            ensure(library.attachments().contains(jarAttachment()));
            ensure(!library.attachments().contains(javadocAttachment()));
            ensure(library.attachments().contains(sourcesAttachment()));
        }
    }

    @Test
    public void testContent()
    {
        var library = kivakitApplication()
            .withContent(packageContent());

        ensureEqual(library.content(), packageContent());
    }

    @Test
    public void testCopy()
    {
        {
            var asset = asset("a:b:1");
            ensureEqual(asset, asset.copy());
        }
        {
            var asset = asset("a::1");
            ensureEqual(asset, asset.copy());
        }
        {
            var asset = asset("a:b:");
            ensureEqual(asset, asset.copy());
        }
        {
            var asset = asset("a::");
            ensureEqual(asset, asset.copy());
        }
        {
            var library = library("a:b:1");
            ensureEqual(library, library.copy());
        }
        {
            var library = library("a::1");
            ensureEqual(library, library.copy());
        }
        {
            var library = library("a:b:");
            ensureEqual(library, library.copy());
        }
        {
            var library = library("a::");
            ensureEqual(library, library.copy());
        }
    }

    @Test
    public void testDependencies()
    {
        {
            var application = kivakitApplication();
            ensureEqual(application.libraries(), dependencies(kivakitCore(), kivakitResource()));
        }
        {
            var images = kivakitImages()
                .withDependencies(kivakitIcons(), kivakitLogos());

            ensureEqual(images.assets(), assets(kivakitIcons(), kivakitLogos()));
        }
    }

    @Test
    public void testDependenciesMatching()
    {
        var application = kivakitApplication();

        ensureEqual(application.dependenciesMatching(":com.telenav.kivakit::"),
            artifacts(kivakitCore(), kivakitResource(), kivakitIcons(), kivakitLogos()));
    }

    @Test
    public void testDependencyNamed()
    {
        var application = kivakitApplication()
            .withDependencies(kivakitCore(), kivakitResource());
        ensureNotNull(application.dependencyNamed("library:com.telenav.kivakit:kivakit-resource:1.8.5"));
        ensureThrows(() -> application.dependencyNamed(":com.telenav.kivakit:kivakit-x:1.8.5"));
    }

    @Test
    public void testDependsOn()
    {
        var application = kivakitApplication()
            .withDependencies(kivakitCore(), kivakitResource());

        ensure(application.dependencies().containsAll(artifacts(kivakitCore(), kivakitResource())));
    }

    @Test
    public void testDescriptor()
    {
        ensureEqual(kivakitApplication().descriptor(), descriptor("library:com.telenav.kivakit:kivakit-application:1.8.5"));
    }

    @Test
    public void testEqualsHash()
    {
        var map = new ObjectMap<Artifact<?>, Integer>();
        map.put(kivakitApplication(), 1);
        map.put(kivakitCore(), 2);

        ensure(map.containsKey(kivakitApplication()));
        ensure(map.containsKey(kivakitCore()));
        ensureEqual(map.get(kivakitApplication()), 1);
        ensureEqual(map.get(kivakitCore()), 2);

        ensureNotEqual(kivakitCore(), 7);
    }

    @Test
    public void testExcluding()
    {
        var application = kivakitApplication()
            .withDependencies(kivakitCore(), kivakitResource());

        ensureEqual(application
                .excluding(":com.telenav.kivakit:kivakit-core:")
                .excluding(kivakitIcons())
                .excluding(kivakitLogos())
                .dependencies(),
            libraries(kivakitResource()));

        ensureEqual(application
                .excluding(":com.telenav.kivakit::")
                .dependencies(),
            artifacts());

        ensureEqual(application
                .excluding(kivakitCore().descriptor())
                .dependencies(),
            artifacts(kivakitResource(),
                kivakitLogos(),
                kivakitIcons()));

        ensureEqual(application
                .excluding(kivakitResource().descriptor())
                .dependencies(),
            artifacts(kivakitCore(),
                kivakitLogos(),
                kivakitIcons()));

        ensureEqual(application
                .excluding(kivakitResource().descriptor(),
                    kivakitCore().descriptor(),
                    kivakitIcons().descriptor(),
                    kivakitLogos().descriptor())
                .dependencies(),
            artifacts());

        ensureEqual(application
                .excluding(kivakitCore())
                .excluding(kivakitIcons())
                .excluding(kivakitLogos())
                .dependencies(),
            artifacts(kivakitResource()));

        ensureEqual(application
                .excluding(kivakitResource())
                .excluding(kivakitIcons())
                .excluding(kivakitLogos())
                .dependencies(),
            artifacts(kivakitCore()));

        ensureEqual(application
                .excluding(kivakitResource())
                .excluding(kivakitIcons())
                .excluding(kivakitLogos())
                .excluding(kivakitCore()).dependencies(),
            artifacts());

        ensureEqual(application
                .excluding(kivakitResource().descriptor(),
                    kivakitIcons().descriptor(),
                    kivakitLogos().descriptor())
                .dependencies()
                .asDescriptors(),
            descriptors(artifacts(kivakitCore())));
    }

    @Test
    public void testIsExcluded()
    {
        var application = kivakitApplication()
            .withDependencies(kivakitCore(), kivakitResource());

        ensure(application
            .excluding(":com.telenav.kivakit:kivakit-core:")
            .isExcluded(kivakitCore()));

        ensure(application
            .excluding(":com.telenav.kivakit::")
            .isExcluded(kivakitCore()));

        ensure(application
            .excluding(":com.telenav.kivakit::")
            .isExcluded(kivakitResource()));
    }

    @Test
    public void testJar()
    {
        var library = kivakitCore()
            .withJar(packageContent());

        ensureEqual(library.jar(), packageContent());
    }

    @Test
    public void testJson()
    {
        var application = kivakitApplication()
            .withDependencies(kivakitCore(), kivakitResource())
            .withRepository(MAVEN_CENTRAL)
            .withJar(packageContent())
            .withJavadoc(packageContent());

        var json = application.toJson();
        var deserialized = artifactFromJson(json);
        ensureEqual(application, deserialized);
    }

    @Test
    public void testMavenPom()
    {
        var application = kivakitApplication()
            .excluding(kivakitIcons())
            .excluding(kivakitLogos());

        ensureEqual(application.mavenPom().trim(), """
            <project
              xmlns="http://maven.apache.org/POM/4.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

                <modelVersion>4.0.0</modelVersion>

                <groupId>com.telenav.kivakit</groupId>
                <artifactId>kivakit-application</artifactId>
                <version>1.8.5</version>
               \s
                <dependencies>
               \s
                    <dependency>
                        <groupId>com.telenav.kivakit</groupId>
                        <artifactId>kivakit-core</artifactId>
                        <version>1.8.5</version>
                    </dependency>
                   \s
                    <dependency>
                        <groupId>com.telenav.kivakit</groupId>
                        <artifactId>kivakit-resource</artifactId>
                        <version>1.8.5</version>
                    </dependency>
                   \s
                </dependencies>
               \s
            </project>""".trim());
    }

    @Test
    public void testName()
    {
        ensureEqual(kivakitCore().name(), "library:com.telenav.kivakit:kivakit-core:1.8.5");
    }

    @Test
    public void testRepository()
    {
        var application = kivakitApplication()
            .withRepository(MAVEN_CENTRAL);

        ensureEqual(application.repository(), MAVEN_CENTRAL);
    }

    @Test
    public void testVersion()
    {
        ensureEqual(kivakitCore().withVersion("1.0.0"),
            library("com.telenav.kivakit:kivakit-core:1.0.0"));
        ensureEqual(kivakitCore().withVersion(version("1.0.0")),
            library("com.telenav.kivakit:kivakit-core:1.0.0"));
    }

    @Test
    public void testWith()
    {
        var library = kivakitApplication();

        ensureEqual(library.withDescriptor(descriptor("library:a:b:1")).descriptor(), descriptor("library:a:b:1"));

        ensureEqual(library.withArtifactName("x").descriptor(), descriptor("library:com.telenav.kivakit:x:1.8.5"));
        ensureEqual(library.withArtifactName(new ArtifactName("x")).descriptor(), descriptor("library:com.telenav.kivakit:x:1.8.5"));
        ensureEqual(library.artifact("x").descriptor(), descriptor("library:com.telenav.kivakit:x:1.8.5"));

        ensureEqual(library.withVersion("1.0").descriptor(), descriptor("library:com.telenav.kivakit:kivakit-application:1.0"));
        ensureEqual(library.withVersion("1.0").descriptor(), descriptor("library:com.telenav.kivakit:kivakit-application:1.0"));

        ensureEqual(library.withRepository(MAVEN_CENTRAL).repository(), MAVEN_CENTRAL);

        ensureEqual(kivakitApplication().withContent(packageContent()).content(), packageContent());
    }
}