package digital.fiasco.runtime.repository.remote.server.serialization.converters;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static digital.fiasco.runtime.dependency.collections.lists.ArtifactList.artifacts;

public class ArtifactListConverterTest extends FiascoTest
{
    @Test
    public void test()
    {
        var converter = new ArtifactListConverter();
        var text = kivakitCore().name() + ", " + kivakitIcons().name() + ", " + kivakitResource().name();
        var list = converter.convert(text);
        ensureEqual(list, artifacts(kivakitCore(), kivakitIcons(), kivakitResource()));
        ensureEqual(converter.unconvert(list), text);
    }
}
