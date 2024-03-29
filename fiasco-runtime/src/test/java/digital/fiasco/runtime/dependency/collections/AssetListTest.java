package digital.fiasco.runtime.dependency.collections;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static digital.fiasco.runtime.dependency.collections.ArtifactList.artifacts;
import static digital.fiasco.runtime.dependency.collections.AssetList.assets;

public class AssetListTest extends FiascoTest
{
    @Test
    public void testAsAssetList()
    {
        var assets = assets(kivakitIcons(), kivakitLogos());
        ensure(assets.equals(artifacts(kivakitIcons(), kivakitLogos()).asAssetList()));
    }

    @Test
    public void testCreation()
    {
        {
            var assets = assets(set(kivakitIcons(), kivakitLogos()));
            ensure(assets.contains(kivakitIcons()));
            ensure(assets.contains(kivakitLogos()));
        }
        {
            var assets = assets(list(kivakitIcons().descriptor(), kivakitLogos().descriptor()));
            ensure(assets.contains(kivakitIcons()));
            ensure(assets.contains(kivakitLogos()));
        }
        {
            var assets = assets(kivakitIcons().descriptor(), kivakitLogos().descriptor());
            ensure(assets.contains(kivakitIcons()));
            ensure(assets.contains(kivakitLogos()));
        }
    }
}
