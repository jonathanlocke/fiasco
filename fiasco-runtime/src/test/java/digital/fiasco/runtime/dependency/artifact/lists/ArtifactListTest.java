package digital.fiasco.runtime.dependency.artifact.lists;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.artifacts.Asset;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.value.count.Count._2;
import static digital.fiasco.runtime.dependency.artifact.lists.ArtifactList.artifacts;

public class ArtifactListTest extends FiascoTest
{
    @Test
    public void testAsArtifactDescriptors()
    {
        var descriptors = kivakitArtifacts().asArtifactDescriptors();
        ensureEqual(kivakitArtifacts().size(), descriptors.size());
        for (var artifact : kivakitArtifacts())
        {
            ensure(descriptors.contains(artifact.descriptor()));
        }
    }

    @Test
    public void testAsString()
    {
        var joined = kivakitArtifacts().asString();
        ensureEqual(joined, "library:com.telenav.kivakit:kivakit-core:1.8.5, asset:com.telenav.kivakit:kivakit-icons:1.8.5, library:com.telenav.kivakit:kivakit-application:1.8.5, asset:com.telenav.kivakit:kivakit-logos:1.8.5");
    }

    @Test
    public void testAsStringList()
    {
        ensure(kivakitLibraries().asStringList()
            .equals(stringList(
                "library:com.telenav.kivakit:kivakit-application:1.8.5",
                "library:com.telenav.kivakit:kivakit-core:1.8.5"
            )));
    }

    @Test
    public void testContains()
    {
        ensure(kivakitArtifacts().contains(kivakitApplication()));
        ensure(kivakitLibraries().contains(kivakitApplication()));
        ensure(!kivakitLibraries().contains(kivakitIcons()));
        ensure(!kivakitAssets().contains(kivakitCore()));
    }

    @Test
    public void testCopy()
    {
        ensureEqual(kivakitArtifacts().copy(), kivakitArtifacts());
        ensureEqual(kivakitLibraries().copy(), kivakitLibraries());
        ensureEqual(kivakitAssets().copy(), kivakitAssets());
    }

    @Test
    public void testCount()
    {
        ensureEqual(kivakitAssets().count(), _2);
        ensureEqual(kivakitLibraries().count(), _2);
    }

    @Test
    public void testCreation()
    {
        ensure(kivakitArtifacts().containsAll(kivakitArtifacts()));
        ensureNotNull(new ArtifactList());
    }

    @Test
    public void testDeduplicate()
    {
        ensure(artifacts(kivakitIcons(), kivakitIcons(), kivakitCore(), kivakitIcons())
            .deduplicate().equals(artifacts(kivakitIcons(), kivakitCore())));
    }

    @Test
    public void testEquals()
    {
        ensureEqual(kivakitArtifacts(), kivakitArtifacts());
        ensureNotEqual(kivakitArtifacts(), kivakitAssets());
        ensureNotEqual(kivakitArtifacts(), 1);
    }

    @Test
    public void testFirst()
    {
        ensureEqual(artifacts(kivakitLogos(), kivakitImages(), kivakitApplication()).first(), kivakitLogos());
    }

    @Test
    public void testGet()
    {
        var artifacts = artifacts(kivakitLogos(), kivakitImages(), kivakitApplication());
        ensureEqual(artifacts.get(0), kivakitLogos());
        ensureEqual(artifacts.get(1), kivakitImages());
        ensureEqual(artifacts.get(2), kivakitApplication());
        ensureThrows(() -> artifacts.get(3));
    }

    @Test
    public void testJoin()
    {
        var joined = kivakitArtifacts().join(", ");
        ensureEqual(joined, "library:com.telenav.kivakit:kivakit-core:1.8.5, asset:com.telenav.kivakit:kivakit-icons:1.8.5, library:com.telenav.kivakit:kivakit-application:1.8.5, asset:com.telenav.kivakit:kivakit-logos:1.8.5");
    }

    @Test
    public void testLast()
    {
        ensureEqual(artifacts(kivakitLogos(), kivakitImages(), kivakitApplication()).last(), kivakitApplication());
    }

    @Test
    public void testMatching()
    {
        var libraries = kivakitArtifacts().matching(at -> at instanceof Library);
        ensure(libraries.containsAll(artifacts(kivakitCore(), kivakitApplication())));
        ensure(kivakitArtifacts().asLibraryList().containsAll(kivakitLibraries().asLibraryList()));
        ensure(kivakitArtifacts().asAssetList().containsAll(kivakitAssets().asAssetList()));
    }

    @Test
    public void testSize()
    {
        ensureEqual(kivakitAssets().size(), 2);
        ensureEqual(kivakitLibraries().size(), 2);
    }

    @Test
    public void testSorted()
    {
        var artifacts = artifacts(kivakitLogos(), kivakitImages(), kivakitApplication());
        ensureEqual(artifacts.sorted().asArtifactList(), artifacts(kivakitApplication(), kivakitImages(), kivakitLogos()));
    }

    @Test
    public void testWith()
    {
        ensure(kivakitArtifacts().without(kivakitAssets())
            .equals(kivakitLibraries()));
        ensure(kivakitArtifacts().without(kivakitLibraries())
            .equals(kivakitAssets()));
        ensure(kivakitAssets().with(kivakitLibraries())
            .equals(kivakitArtifacts()));
        ensure(kivakitLibraries().with(kivakitAssets())
            .equals(kivakitArtifacts()));
        ensure(kivakitArtifacts().with(kivakitResource())
            .equals(artifacts(kivakitResource()).with(kivakitArtifacts())));
        ensure(kivakitArtifacts().with(artifacts(kivakitResource(), kivakitImages()))
            .equals(artifacts(kivakitResource(), kivakitImages()).with(kivakitArtifacts())));
        ensure(kivakitArtifacts().without(at -> at instanceof Asset)
            .equals(kivakitLibraries()));
        ensure(kivakitArtifacts().without(list(kivakitApplication()))
            .equals(kivakitArtifacts().without(kivakitApplication())));
        ensure(kivakitLibraries().with(kivakitAssets().asList().asArray(Artifact.class))
            .equals(kivakitArtifacts()));
    }
}
