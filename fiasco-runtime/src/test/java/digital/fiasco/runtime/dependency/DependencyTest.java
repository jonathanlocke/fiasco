package digital.fiasco.runtime.dependency;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifacts;
import static digital.fiasco.runtime.dependency.artifact.Asset.assets;

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
        ensureEqual(kivakitApplication().assetDependencies(), assets(kivakitIcons(), kivakitLogos()));
    }

    @Test
    public void testLibraryDependencies()
    {
        ensureEqual(kivakitApplication().libraryDependencies(), artifacts(kivakitCore(), kivakitResource()));
    }
}
