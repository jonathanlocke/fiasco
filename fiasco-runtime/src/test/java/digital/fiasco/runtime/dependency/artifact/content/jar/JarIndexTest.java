package digital.fiasco.runtime.dependency.artifact.content.jar;

import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.value.count.Bytes;
import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Duration.ONE_SECOND;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.value.count.Count._10;
import static digital.fiasco.runtime.dependency.artifact.content.jar.JarEntry.jarEntry;
import static digital.fiasco.runtime.dependency.artifact.content.jar.JarIndex.jarIndex;

public class JarIndexTest extends FiascoTest
{
    @Test
    public void test()
    {
        var time = LocalTime.now().roundDown(ONE_SECOND);

        var index = jarIndex();

        for (var i : _10.ints())
        {
            index = index.withEntry(jarEntry()
                .withPath("/test/path/" + i)
                .withSize(Bytes.kilobytes(i))
                .withLastModified(time.plus(seconds(i)))
                .withOffset(i));
        }

        var yaml = index.toYaml();

        var deserialized = jarIndex(yaml);

        ensureEqual(index, deserialized);
    }
}
