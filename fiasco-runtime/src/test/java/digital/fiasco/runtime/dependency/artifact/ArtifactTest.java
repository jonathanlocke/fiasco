package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.collections.map.ObjectMap;
import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static digital.fiasco.runtime.build.BuildRepositories.MAVEN_CENTRAL;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAR_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.SOURCES_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptor;
import static digital.fiasco.runtime.dependency.artifact.Asset.assets;
import static digital.fiasco.runtime.dependency.artifact.Library.libraries;
import static digital.fiasco.runtime.dependency.artifact.Library.library;

public class ArtifactTest extends FiascoTest
{
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
            .withContent(content());

        ensureEqual(library.content(), content());
    }

    @Test
    public void testCopy()
    {
        {
            var asset = Asset.asset("a:b:1");
            ensureEqual(asset, asset.copy());
        }
        {
            var asset = Asset.asset("a::1");
            ensureEqual(asset, asset.copy());
        }
        {
            var asset = Asset.asset("a:b:");
            ensureEqual(asset, asset.copy());
        }
        {
            var asset = Asset.asset("a::");
            ensureEqual(asset, asset.copy());
        }
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
    public void testDependencies()
    {
        {
            var application = kivakitApplication()
                .dependsOn(kivakitCore(), kivakitResource());
            ensureEqual(application.dependencies(Library.class), libraries(kivakitCore(), kivakitResource()));
            ensureEqual(application.libraryDependencies(), libraries(kivakitCore(), kivakitResource()));
        }
        {
            var images = kivakitImages()
                .dependsOn(kivakitIcons(), kivakitLogos());

            ensureEqual(images.dependencies(Asset.class), assets(kivakitIcons(), kivakitLogos()));
            ensureEqual(images.assetDependencies(), assets(kivakitIcons(), kivakitLogos()));
        }
    }

    @Test
    public void testDependenciesMatching()
    {
        var application = kivakitApplication()
            .dependsOn(kivakitCore(), kivakitResource());

        ensureEqual(application.dependenciesMatching("com.telenav.kivakit::"), dependencyList(kivakitCore(), kivakitResource()));
    }

    @Test
    public void testDependencyMatching()
    {
        var application = kivakitApplication()
            .dependsOn(kivakitCore(), kivakitResource());

        ensureEqual(application.dependencyMatching("com.telenav.kivakit:kivakit-core:"), kivakitCore());
    }

    @Test
    public void testDependencyNamed()
    {
        var application = kivakitApplication()
            .dependsOn(kivakitCore(), kivakitResource());

        ensureEqual(application.dependencyMatching("com.telenav.kivakit:kivakit-core:1.8.5"), kivakitCore());
    }

    @Test
    public void testExclusions()
    {

    }

    @Test
    public void testJar()
    {
        var library = kivakitCore()
            .withJar(content());

        ensureEqual(library.jar(), content());
    }

    @Test
    public void testMavenPom()
    {
        var application = kivakitApplication()
            .dependsOn(kivakitCore(), kivakitResource());

        ensureEqual(application.mavenPom().trim(), """
            <project
              xmlns="http://maven.apache.org/POM/4.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
               <modelVersion>4.0.0</modelVersion>
               <groupId>com.telenav.kivakit</groupId>
               <artifactId>kivakit-application</artifactId>
               <version>1.8.5</version>

               <dependencies>

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

            </project>""".trim());
    }

    @Test
    public void testName()
    {
        ensureEqual(kivakitCore().name(), "com.telenav.kivakit:kivakit-core:1.8.5");
    }

    @Test
    public void testWith()
    {
        var library = kivakitApplication();

        ensureEqual(library.withDescriptor(descriptor("a:b:1")).descriptor(), descriptor("a:b:1"));

        ensureEqual(library.withArtifact("x").descriptor(), descriptor("com.telenav.kivakit:x:1.8.5"));
        ensureEqual(library.artifact("x").descriptor(), descriptor("com.telenav.kivakit:x:1.8.5"));

        ensureEqual(library.withVersion("1.0").descriptor(), descriptor("com.telenav.kivakit:kivakit-application:1.0"));
        ensureEqual(library.version("1.0").descriptor(), descriptor("com.telenav.kivakit:kivakit-application:1.0"));

        ensureEqual(library.withRepository(MAVEN_CENTRAL).repository(), MAVEN_CENTRAL);
    }
}