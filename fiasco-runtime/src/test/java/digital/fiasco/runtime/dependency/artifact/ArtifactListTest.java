package digital.fiasco.runtime.dependency.artifact;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.value.count.Count._2;
import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifactList;

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
        ensureEqual(joined, "com.telenav.kivakit:kivakit-core:1.8.5, com.telenav.kivakit:kivakit-icons:1.8.5, com.telenav.kivakit:kivakit-application:1.8.5, com.telenav.kivakit:kivakit-logos:1.8.5");
    }

    @Test
    public void testAsStringList()
    {
        ensure(kivakitLibraries().asStringList()
            .equals(stringList(
                "com.telenav.kivakit:kivakit-application:1.8.5",
                "com.telenav.kivakit:kivakit-core:1.8.5"
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
        ensureEqual(artifactList(kivakitLogos(), kivakitImages(), kivakitApplication()).first(), kivakitLogos());
    }

    @Test
    public void testGet()
    {
        var artifacts = artifactList(kivakitLogos(), kivakitImages(), kivakitApplication());
        ensureEqual(artifacts.get(0), kivakitLogos());
        ensureEqual(artifacts.get(1), kivakitImages());
        ensureEqual(artifacts.get(2), kivakitApplication());
        ensureThrows(() -> artifacts.get(3));
    }

    @Test
    public void testJoin()
    {
        var joined = kivakitArtifacts().join(", ");
        ensureEqual(joined, "com.telenav.kivakit:kivakit-core:1.8.5, com.telenav.kivakit:kivakit-icons:1.8.5, com.telenav.kivakit:kivakit-application:1.8.5, com.telenav.kivakit:kivakit-logos:1.8.5");
    }

    @Test
    public void testLast()
    {
        ensureEqual(artifactList(kivakitLogos(), kivakitImages(), kivakitApplication()).last(), kivakitApplication());
    }

    @Test
    public void testMatching()
    {
        var libraries = kivakitArtifacts().matching(at -> at instanceof Library);
        ensure(libraries.containsAll(artifactList(kivakitCore(), kivakitApplication())));
        ensure(kivakitArtifacts().libraries().containsAll(kivakitLibraries()));
        ensure(kivakitArtifacts().assets().containsAll(kivakitAssets()));
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
        var artifacts = artifactList(kivakitLogos(), kivakitImages(), kivakitApplication());
        ensureEqual(artifacts.sorted(), artifactList(kivakitApplication(), kivakitImages(), kivakitLogos()));
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
            .equals(artifactList(kivakitResource()).with(kivakitArtifacts())));
        ensure(kivakitArtifacts().with(kivakitResource(), kivakitImages())
            .equals(artifactList(kivakitResource(), kivakitImages()).with(kivakitArtifacts())));
        ensure(kivakitArtifacts().without(at -> at instanceof Asset)
            .equals(kivakitLibraries()));
        ensure(kivakitArtifacts().without(list(kivakitApplication()))
            .equals(kivakitArtifacts().without(kivakitApplication())));
        ensure(kivakitLibraries().with(kivakitAssets().asList().asArray(Artifact.class))
            .equals(kivakitArtifacts()));
    }
}
