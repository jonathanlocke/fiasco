package digital.fiasco.runtime.dependency;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.lists.ArtifactList;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.DependencyList.dependencies;

public class DependencyListTest extends FiascoTest
{
    @Test
    public void testAsArtifactList()
    {
        var dependencies = dependencies(kivakitIcons(), kivakitCore());
        ensure(dependencies.asArtifactList().equals(ArtifactList.artifacts(kivakitIcons(), kivakitCore())));
    }

    @Test
    public void testAsSet()
    {
        ensureEqual(dependencies(kivakitCore(), kivakitIcons(), kivakitResource()).asSet(),
            dependencies(kivakitResource(), kivakitIcons(), kivakitCore()).asSet());
    }

    @Test
    public void testDeduplicate()
    {
        ensure(dependencies(kivakitIcons(), kivakitIcons(), kivakitCore(), kivakitIcons()).deduplicate()
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
