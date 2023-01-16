package digital.fiasco.runtime.dependency;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;

public class DependencyListTest extends FiascoTest
{
    @Test
    public void testAsArtifactList()
    {
        var dependencies = dependencyList(kivakitIcons(), kivakitCore());
        ensure(dependencies.asArtifactList().equals(ArtifactList.artifactList(kivakitIcons(), kivakitCore())));
    }
}
