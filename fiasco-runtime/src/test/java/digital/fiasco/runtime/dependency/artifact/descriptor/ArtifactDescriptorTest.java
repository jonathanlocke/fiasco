package digital.fiasco.runtime.dependency.artifact.descriptor;

import com.telenav.kivakit.core.version.Version;
import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static com.telenav.kivakit.core.version.Version.version;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor.artifactDescriptor;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor.parseDescriptor;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactGroup.group;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName.artifactName;

public class ArtifactDescriptorTest extends FiascoTest
{
    @Test
    public void testAsAsset()
    {
        ensureEqual(kivakitIcons(), kivakitIcons().descriptor().asAsset());
    }

    @Test
    public void testAsLibrary()
    {
        ensureEqual(kivakitCore(), kivakitCore().descriptor().asLibrary());
    }

    @Test
    public void testCreation()
    {
        var descriptor = artifactDescriptor("library:x:y:");
        ensureEqual(descriptor.group(), group("x"));
        ensureEqual(descriptor.artifactName(), artifactName("y"));
        ensureEqual(descriptor.version(), null);
        ensureFalse(descriptor.isComplete());
        ensureEqual("library:x:y:", descriptor.name());
        ensureThrows(() -> artifactDescriptor("xyz"));
        ensureThrows(() -> artifactDescriptor("1"));
        ensureNull(parseDescriptor(nullListener(), ""));
        ensureNull(parseDescriptor(nullListener(), "x"));
        ensureEqual(parseDescriptor(nullListener(), "library:x:y:1"), artifactDescriptor("library:x:y:1"));
    }

    @Test
    public void testGroup()
    {
        var descriptor = artifactDescriptor("library:x::");
        ensureEqual(descriptor.group(), group("x"));
        ensureEqual(descriptor.artifactName(), null);
        ensureEqual(descriptor.version(), null);
        ensureFalse(descriptor.isComplete());
        ensureEqual("library:x::", descriptor.name());
    }

    @Test
    public void testIsComplete()
    {
        var descriptor = artifactDescriptor("library:x:y:1.5");
        ensureEqual(descriptor.group(), group("x"));
        ensureEqual(descriptor.artifactName(), artifactName("y"));
        ensureEqual(descriptor.version(), version("1.5"));
        ensure(descriptor.isComplete());
        ensureEqual("library:x:y:1.5", descriptor.name());
    }

    @Test
    public void testMatching()
    {
        ensure(artifactDescriptor("library:x::").matches(artifactDescriptor("library:x:y:1.0")));
        ensure(artifactDescriptor("library:x:y:").matches(artifactDescriptor("library:x:y:1.0")));
        ensure(artifactDescriptor("library:x::1.0").matches(artifactDescriptor("library:x:y:1.0")));

        ensureFalse(artifactDescriptor("library:x::").matches(artifactDescriptor("library:z:y:1.0")));
        ensureFalse(artifactDescriptor("library:x:y:").matches(artifactDescriptor("library:x:z:1.0")));
        ensureFalse(artifactDescriptor("library:x::1.0").matches(artifactDescriptor("library:x:y:9.9")));
    }

    @Test
    public void testMatchingA()
    {
        ensureMatches(descriptorA(), descriptorA(), descriptorAb(), descriptorAv(), descriptorAbv());
        ensureNotMatches(descriptorA(), descriptorX(), descriptorXy(), descriptorXv(), descriptorXyv());
    }

    @Test
    public void testMatchingAbv()
    {
        ensureMatches(descriptorAbv(), descriptorAbv());
        ensureNotMatches(descriptorAbv(), descriptorA(), descriptorAb(), descriptorAv(),
            descriptorX(), descriptorXy(), descriptorXv(), descriptorXyv());
    }

    @Test
    public void testMatchingAv()
    {
        ensureMatches(descriptorAv(), descriptorAv(), descriptorAbv());
        ensureNotMatches(descriptorAv(), descriptorAb(), descriptorA(),
            descriptorX(), descriptorXy(), descriptorXv(), descriptorXyv());
    }

    @Test
    public void testMatchingAy()
    {
        ensureMatches(descriptorAb(), descriptorAb(), descriptorAbv());
        ensureNotMatches(descriptorAb(), descriptorA(), descriptorAv(),
            descriptorX(), descriptorXy(), descriptorXv(), descriptorXyv());
    }

    @Test
    public void testMatchingX()
    {
        ensureMatches(descriptorX(), descriptorX(), descriptorXy(), descriptorXv(), descriptorXyv());
        ensureNotMatches(descriptorX(), descriptorA(), descriptorAb(), descriptorAv(), descriptorAbv());
    }

    @Test
    public void testMatchingXv()
    {
        ensureMatches(descriptorXv(), descriptorXv(), descriptorXyv());
        ensureNotMatches(descriptorXv(), descriptorXy(), descriptorX(),
            descriptorA(), descriptorAb(), descriptorAv(), descriptorAbv());
    }

    @Test
    public void testMatchingXy()
    {
        ensureMatches(descriptorXy(), descriptorXy(), descriptorXyv());
        ensureNotMatches(descriptorXy(), descriptorX(), descriptorXv(),
            descriptorA(), descriptorAb(), descriptorAv(), descriptorAbv());
    }

    @Test
    public void testMatchingXyv()
    {
        ensureMatches(descriptorXyv(), descriptorXyv());
        ensureNotMatches(descriptorXyv(), descriptorX(), descriptorXy(), descriptorXv(),
            descriptorA(), descriptorAb(), descriptorAv(), descriptorAbv());
    }

    @Test
    public void testName()
    {
        ensureEqual(descriptorX().name(), "library:x::");
        ensureEqual(descriptorXy().name(), "library:x:y:");
        ensureEqual(descriptorXv().name(), "library:x::1.2.3");
        ensureEqual(descriptorXyv().name(), "library:x:y:1.2.3");

        ensureEqual(descriptorA().name(), "library:a::");
        ensureEqual(descriptorAb().name(), "library:a:b:");
        ensureEqual(descriptorAv().name(), "library:a::1.2.3");
        ensureEqual(descriptorAbv().name(), "library:a:b:1.2.3");
    }

    @Test
    public void testParsing()
    {
        ensureNotNull(artifactDescriptor("library:x:y:1.0"));
        ensureThrows(() -> artifactDescriptor("library:x:y:?"));
        ensureThrows(() -> artifactDescriptor("::y:1.0"));
        ensureThrows(() -> artifactDescriptor(":::"));
    }

    @Test
    public void testVersion()
    {
        var descriptor = artifactDescriptor("library:x::1.5.9");
        ensureEqual(descriptor.group(), group("x"));
        ensureEqual(descriptor.artifactName(), null);
        ensureEqual(descriptor.version(), version("1.5.9"));
        ensureFalse(descriptor.isComplete());
        ensureEqual("library:x::1.5.9", descriptor.name());
        ensureEqual(descriptor.version(version("1.1.1")).version(), version("1.1.1"));
    }

    @Test
    public void testWith()
    {
        ensureEqual(artifactDescriptor("library:x::1.5.9")
            .withGroup("y").toString(), "library:y::1.5.9");

        ensureEqual(artifactDescriptor("library:x::1.5.9")
            .withArtifact("y").toString(), "library:x:y:1.5.9");

        ensureEqual(artifactDescriptor("library:x::1.5.9")
            .withVersion("1.0").toString(), "library:x::1.0");

        ensureEqual(artifactDescriptor("library:x::1.5.9")
            .withVersion((Version) null).toString(), "library:x::");

        ensureEqual(artifactDescriptor("library:x::")
            .withVersion("6.0").toString(), "library:x::6.0");

        ensureEqual(artifactDescriptor("library:x::")
            .withArtifact("y")
            .withVersion("6.0").toString(), "library:x:y:6.0");
    }

    @Test
    public void testWithout()
    {
        ensureEqual(artifactDescriptor("library:x:y:1.5.9")
            .withoutArtifact().toString(), "library:x::1.5.9");

        ensureEqual(artifactDescriptor("library:x:y:1.5.9")
            .withoutVersion().toString(), "library:x:y:");

        ensureEqual(artifactDescriptor("library:x:y:1.5.9")
            .withoutArtifact()
            .withoutVersion().toString(), "library:x::");
    }

    private void ensureMatches(ArtifactDescriptor x, ArtifactDescriptor... y)
    {
        for (var at : y)
        {
            ensure(x.matches(at));
        }
    }

    private void ensureNotMatches(ArtifactDescriptor x, ArtifactDescriptor... y)
    {
        for (var at : y)
        {
            ensureNotEqual(x, at);
        }
    }
}
