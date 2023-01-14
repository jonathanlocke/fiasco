package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.time.Time;
import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.value.count.Bytes.bytes;

public class ArtifactContentTest extends FiascoTest
{
    @Test
    public void testWith()
    {
        var content = content();

        {
            content = content.withName("shibo");
            ensureEqual(content.name(), "shibo");
        }
        {
            content = content.withOffset(5L);
            ensureEqual(content.offset(), 5L);
        }
        {
            content = content.withSize(bytes(6));
            ensureEqual(content.size(), bytes(6));
        }
        {
            var now = Time.now();
            content = content.withLastModified(now);
            ensureEqual(content.lastModified(), now);
        }
        {
            var signatures = signatures()
                .withPgp("pgp")
                .withMd5("md5")
                .withSha1("sha1");

            content = content.withSignatures(signatures);
            ensureEqual(content.signatures(), signatures);
        }
    }
}
