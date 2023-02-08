package digital.fiasco.runtime.repository.maven;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.build.environment.BuildRepositoriesTrait.MAVEN_CENTRAL;
import static digital.fiasco.runtime.build.environment.BuildRepositoriesTrait.MAVEN_CENTRAL_STAGING;
import static digital.fiasco.runtime.dependency.collections.lists.ArtifactDescriptorList.descriptors;
import static digital.fiasco.runtime.dependency.collections.lists.LibraryList.libraries;

public class MavenRepositoryTest extends FiascoTest
{
    @Test
    public void testInstallArtifact()
    {
        var artifact = kivakitApplication()
            .withContent(packageContent());

        var repositoryFolder = currentFolder().folder("target/.fiasco/maven-repository");

        var repository = new MavenRepository("test", "test", repositoryFolder);
        repository.installArtifact(artifact);

        var folder = repositoryFolder.folder("com/telenav/kivakit/kivakit-application/1.8.5");
        ensure(folder.file("kivakit-application-1.8.5.jar").exists());
        ensure(folder.file("kivakit-application-1.8.5.jar.asc").exists());
        ensure(folder.file("kivakit-application-1.8.5.jar.md5").exists());
        ensure(folder.file("kivakit-application-1.8.5.jar.sha1").exists());
        ensure(folder.file("kivakit-application-1.8.5.pom").exists());
    }

    @Test
    public void testIsRemote()
    {
        ensure(MAVEN_CENTRAL.isRemote());
        ensure(MAVEN_CENTRAL_STAGING.isRemote());
    }

    @Test
    public void testResolveArtifacts()
    {
        var artifacts = MAVEN_CENTRAL.resolveArtifacts(descriptors("library:com.telenav.kivakit:kivakit-interfaces:1.10.0")).sorted();
        ensureEqual(artifacts, libraries(
            "com.telenav.kivakit:kivakit-annotations:1.10.0",
            "com.telenav.kivakit:kivakit-interfaces:1.10.0",
            "com.telenav.lexakai.annotations:lexakai-annotations:1.0.9",
            "org.jetbrains:annotations:23.0.0"));
    }

    @Test
    public void testResolveFailure()
    {
        ensure(MAVEN_CENTRAL.resolveArtifacts(descriptors("library:com.shibo:unknown:1.0.1")).isEmpty());
    }
}