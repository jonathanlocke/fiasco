package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptor;
import static digital.fiasco.runtime.dependency.artifact.ArtifactGroup.group;

public class ArtifactGroupTest extends UnitTest
{
    @Test
    public void testArtifact()
    {
        ensureEqual(group("x").artifact("y"), descriptor("x:y:"));

        ensureEqual(group("x").artifact("y").version("1.9"), descriptor("x:y:1.9"));
    }

    @Test
    public void testAsset()
    {
        var asset = group("x").asset("y");
        ensureEqual(asset.descriptor(), descriptor("x:y:"));
        ensure(asset instanceof Asset);
    }

    @Test
    public void testLibrary()
    {
        var library = group("x").library("y");
        ensureEqual(library.descriptor(), descriptor("x:y:"));
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
