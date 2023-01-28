package digital.fiasco.runtime.dependency.artifact;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName.artifactName;

public class ArtifactNameTest extends FiascoTest
{
    @Test
    public void testCompareTo()
    {
        var list = list(artifactName("x:y:1.2.3"), artifactName("a:b:0.9"), artifactName("lm:nop:"));
        ensureEqual(list.sorted(), list(artifactName("a:b:0.9"), artifactName("lm:nop:"), artifactName("x:y:1.2.3")));
    }

    @Test
    public void testName()
    {
        ensureEqual(artifactName("y").name(), "y");
    }

    @Test
    public void testToString()
    {
        ensureEqual(artifactName("y").toString(), "y");
    }
}
