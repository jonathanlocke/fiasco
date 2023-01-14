package digital.fiasco.runtime.dependency.artifact;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static digital.fiasco.runtime.dependency.artifact.ArtifactName.artifact;

public class ArtifactNameTest extends FiascoTest
{
    @Test
    public void testCompareTo()
    {
        var list = list(artifact("x:y:1.2.3"), artifact("a:b:0.9"), artifact("lm:nop:"));
        ensureEqual(list.sorted(), list(artifact("a:b:0.9"), artifact("lm:nop:"), artifact("x:y:1.2.3")));
    }

    @Test
    public void testName()
    {
        ensureEqual(artifact("y").name(), "y");
    }

    @Test
    public void testToString()
    {
        ensureEqual(artifact("y").toString(), "y");
    }
}
