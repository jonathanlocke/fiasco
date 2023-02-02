package digital.fiasco.runtime.dependency.collections;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.collections.lists.ArtifactList.artifacts;
import static digital.fiasco.runtime.dependency.collections.lists.DependencyList.dependencies;

public class DependencyListTest extends FiascoTest
{
    @Test
    public void testAsArtifactList()
    {
        var dependencies = dependencies(kivakitIcons(), kivakitCore());
        ensure(dependencies.asArtifactList().equals(artifacts(kivakitIcons(), kivakitCore())));
    }

    @Test
    public void testAsSet()
    {
        ensureEqual(dependencies(kivakitCore(), kivakitIcons(), kivakitResource()).asMutableSet(),
                dependencies(kivakitResource(), kivakitIcons(), kivakitCore()).asMutableSet());
    }

    @Test
    public void testDeduplicate()
    {
        ensure(dependencies(kivakitIcons(), kivakitIcons(), kivakitCore(), kivakitIcons()).deduplicated()
                .equals(dependencies(kivakitIcons(), kivakitCore())));
        ensure(!dependencies(kivakitIcons(), kivakitIcons(), kivakitCore(), kivakitIcons())
                .equals(dependencies(kivakitIcons(), kivakitCore())));
    }

    @Test
    public void testWith()
    {
        var dependencies = dependencies(kivakitIcons(), kivakitCore());
        ensureEqual(dependencies.with(kivakitImages(), kivakitApplication()),
                dependencies(kivakitCore(), kivakitApplication(), kivakitIcons(), kivakitImages()));
    }
}
