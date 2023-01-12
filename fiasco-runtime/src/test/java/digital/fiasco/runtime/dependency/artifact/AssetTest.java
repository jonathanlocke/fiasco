package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptor;
import static digital.fiasco.runtime.dependency.artifact.Asset.asset;
import static digital.fiasco.runtime.dependency.artifact.Asset.assets;

public class AssetTest extends UnitTest
{
    @Test
    public void testCreation()
    {
        ensureNotNull(asset("x:y:1.0"));
        ensureThrows(() -> asset("?"));
        var assets = assets("x:y:1.0", "a:b:0.1");
        ensureEqual(assets.size(), 2);
        ensureEqual(assets.get(0), asset("x:y:1.0"));
    }

    @Test
    public void testDependencies()
    {
        var asset = asset("x:y:1.0")
            .withDependencies(dependencyList(asset("a:b:1.0")));
        ensureEqual(asset.dependencies().size(), 1);
    }

    @Test
    public void testDescriptor()
    {
        var asset = asset("x:y:1.0");
        ensureEqual(asset.descriptor(), descriptor("x:y:1.0"));
    }
}
