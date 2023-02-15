package digital.fiasco.runtime.dependency.artifact.content.jar;

import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.value.count.Bytes;
import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Duration.ONE_SECOND;
import static digital.fiasco.runtime.dependency.artifact.content.jar.JarEntry.jarEntry;

public class JarEntryTest extends FiascoTest
{
    @Test
    public void test()
    {
        var time = LocalTime.now().roundDown(ONE_SECOND);

        var entry = jarEntry()
            .withPath("/test/path")
            .withSize(Bytes.kilobytes(4.5))
            .withLastModified(time)
            .withOffset(0);

        var yaml = entry.toYaml();

        var deserialized = jarEntry(yaml);

        ensureEqual(entry, deserialized);
    }
}
