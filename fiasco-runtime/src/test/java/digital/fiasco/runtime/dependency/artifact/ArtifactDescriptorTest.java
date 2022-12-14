package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.version.Version.version;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptor;
import static digital.fiasco.runtime.dependency.artifact.ArtifactGroup.group;
import static digital.fiasco.runtime.dependency.artifact.ArtifactIdentifier.artifact;

public class ArtifactDescriptorTest extends UnitTest
{
    @Test
    public void testArtifact()
    {
        var descriptor = descriptor("x:y:");
        ensureEqual(descriptor.group(), group("x"));
        ensureEqual(descriptor.artifact(), artifact("y"));
        ensureEqual(descriptor.version(), null);
        ensureFalse(descriptor.isComplete());
        ensureEqual("x:y:", descriptor.name());
    }

    @Test
    public void testComplete()
    {
        var descriptor = descriptor("x:y:1.5");
        ensureEqual(descriptor.group(), group("x"));
        ensureEqual(descriptor.artifact(), artifact("y"));
        ensureEqual(descriptor.version(), version("1.5"));
        ensure(descriptor.isComplete());
        ensureEqual("x:y:1.5", descriptor.name());
    }

    @Test
    public void testGroup()
    {
        var descriptor = descriptor("x::");
        ensureEqual(descriptor.group(), group("x"));
        ensureEqual(descriptor.artifact(), null);
        ensureEqual(descriptor.version(), null);
        ensureFalse(descriptor.isComplete());
        ensureEqual("x::", descriptor.name());
    }

    @Test
    public void testMatching()
    {
        ensure(descriptor("x::").matches(descriptor("x:y:1.0")));
        ensure(descriptor("x:y:").matches(descriptor("x:y:1.0")));
        ensure(descriptor("x::1.0").matches(descriptor("x:y:1.0")));

        ensureFalse(descriptor("x::").matches(descriptor("z:y:1.0")));
        ensureFalse(descriptor("x:y:").matches(descriptor("x:z:1.0")));
        ensureFalse(descriptor("x::1.0").matches(descriptor("x:y:9.9")));
    }

    @Test
    public void testParsing()
    {
        ensureNotNull(descriptor("x:y:1.0"));
        ensureThrows(() -> descriptor("x:y:?"));
        ensureThrows(() -> descriptor(":y:1.0"));
        ensureThrows(() -> descriptor("::"));
    }

    @Test
    public void testVersion()
    {
        var descriptor = descriptor("x::1.5.9");
        ensureEqual(descriptor.group(), group("x"));
        ensureEqual(descriptor.artifact(), null);
        ensureEqual(descriptor.version(), version("1.5.9"));
        ensureFalse(descriptor.isComplete());
        ensureEqual("x::1.5.9", descriptor.name());
    }

    @Test
    public void testWith()
    {
        ensureEqual(descriptor("x::1.5.9")
            .withGroup("y").toString(), "y::1.5.9");

        ensureEqual(descriptor("x::1.5.9")
            .withArtifact("y").toString(), "x:y:1.5.9");

        ensureEqual(descriptor("x::1.5.9")
            .withVersion("1.0").toString(), "x::1.0");

        ensureEqual(descriptor("x::1.5.9")
            .withVersion((Version) null).toString(), "x::");

        ensureEqual(descriptor("x::")
            .withVersion("6.0").toString(), "x::6.0");

        ensureEqual(descriptor("x::")
            .withArtifact("y")
            .withVersion("6.0").toString(), "x:y:6.0");
    }

    @Test
    public void testWithout()
    {
        ensureEqual(descriptor("x:y:1.5.9")
            .withoutArtifact().toString(), "x::1.5.9");

        ensureEqual(descriptor("x:y:1.5.9")
            .withoutVersion().toString(), "x:y:");
    }
}
