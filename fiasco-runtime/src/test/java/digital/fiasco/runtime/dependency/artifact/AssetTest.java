package digital.fiasco.runtime.dependency.artifact;

import digital.fiasco.runtime.FiascoTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAR_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.SOURCES_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptor;
import static digital.fiasco.runtime.dependency.artifact.Asset.assets;

public class AssetTest extends FiascoTest
{
    @Test
    public void testAttachments()
    {
        var asset = asset();

        asset = asset
            .withAttachment(jarAttachment())
            .withAttachment(sourcesAttachment())
            .withAttachment(javadocAttachment());

        ensureEqual(asset.attachments().size(), 3);

        ensureEqual(asset.attachmentOfType(JAR_ATTACHMENT), jarAttachment());
        ensureEqual(asset.attachmentOfType(SOURCES_ATTACHMENT), sourcesAttachment());
        ensureEqual(asset.attachmentOfType(JAVADOC_ATTACHMENT), javadocAttachment());

        ensure(asset.attachments().contains(jarAttachment()));
        ensure(asset.attachments().contains(sourcesAttachment()));
        ensure(asset.attachments().contains(javadocAttachment()));
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
    }

    @Test
    public void testCreation()
    {
        ensureNotNull(Asset.asset("x:y:1.0"));
        ensureThrows(() -> Asset.asset("?"));
        {
            var assets = assets("x:y:1.0", "a:b:0.1");
            ensureEqual(assets.size(), 2);
            ensureEqual(assets.get(0), Asset.asset("x:y:1.0"));
        }
        {
            var assets = assets(Asset.asset("x:y:1.0"), Asset.asset("a:b:0.1"));
            ensureEqual(assets.size(), 2);
            ensureEqual(assets.get(0), Asset.asset("x:y:1.0"));
        }
    }

    @Test
    public void testDependencies()
    {
        var asset = asset()
            .withDependencies(dependencyList(Asset.asset("a:b:1.0")));
        ensureEqual(asset.dependencies().size(), 1);
        ensureEqual(asset.dependencies().get(0).descriptor(), descriptor("a:b:1.0"));
    }

    @Test
    public void testDescriptor()
    {
        var asset = asset();
        ensureEqual(asset.descriptor(), descriptor("x:y:1.0"));
    }

    @NotNull
    private Asset asset()
    {
        return Asset.asset("x:y:1.0");
    }
}
