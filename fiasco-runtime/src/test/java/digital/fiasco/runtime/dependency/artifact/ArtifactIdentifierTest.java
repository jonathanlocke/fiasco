package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

public class ArtifactIdentifierTest extends UnitTest
{
    @Test
    public void testIdentifier()
    {
        ensureEqual(new ArtifactIdentifier("y").identifier(), "y");
    }

    @Test
    public void testToString()
    {
        ensureEqual(new ArtifactIdentifier("y").toString(), "y");
    }
}
