package digital.fiasco.runtime.dependency;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.collections.ArtifactList.artifacts;
import static digital.fiasco.runtime.dependency.collections.AssetList.assets;

public class DependencyTest extends FiascoTest
{
    @Test
    public void testArtifactDependencies()
    {
        ensureEqual(kivakitApplication().artifactDependencies(), artifacts(kivakitCore(), kivakitResource(), kivakitIcons(), kivakitLogos()));
    }

    @Test
    public void testAssetDependencies()
    {
        ensureEqual(kivakitApplication().assets(), assets(kivakitIcons(), kivakitLogos()));
    }

    @Test
    public void testLibraryDependencies()
    {
        ensureEqual(kivakitApplication().libraries(), artifacts(kivakitCore(), kivakitResource()));
    }
}
