package digital.fiasco.runtime.dependency.artifact;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptor;
import static digital.fiasco.runtime.dependency.artifact.ArtifactGroup.group;
import static digital.fiasco.runtime.dependency.artifact.ArtifactName.artifactName;
import static digital.fiasco.runtime.dependency.artifact.Asset.asset;
import static digital.fiasco.runtime.dependency.artifact.Library.library;

public class ArtifactGroupTest extends FiascoTest
{
    @Test
    public void testArtifact()
    {
        ensureEqual(group("x").library("y").asLibrary(), descriptor("library:x:y:").asLibrary());
        ensureEqual(group("x").library("y").version("1.9").asLibrary(), descriptor("library:x:y:1.9").asLibrary());
        ensureEqual(group("x").library(artifactName("y")).asLibrary(), descriptor("library:x:y:").asLibrary());
        ensureEqual(group("x").library(artifactName("y")).version("1.9").asLibrary(), descriptor("library:x:y:1.9").asLibrary());
    }

    @Test
    public void testAsset()
    {
        var asset = asset(group("x").asset("y"));
        ensureEqual(asset.descriptor(), descriptor("library:x:y:"));
        ensure(asset instanceof Asset);
    }

    @Test
    public void testLibrary()
    {
        var library = library(group("x").library("y").asLibrary());
        ensureEqual(library.descriptor(), descriptor("library:x:y:"));
        ensure(library instanceof Library);
    }

    @Test
    public void testName()
    {
        ensureEqual(group("x").name(), "x");
    }

    @Test
    public void testToString()
    {
        ensureEqual(group("x").toString(), "x");
    }
}
