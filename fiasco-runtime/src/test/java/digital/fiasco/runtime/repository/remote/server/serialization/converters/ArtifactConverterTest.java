package digital.fiasco.runtime.repository.remote.server.serialization.converters;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.types.Library;
import org.junit.Test;

public class ArtifactConverterTest extends FiascoTest
{
    @Test
    public void test()
    {
        var converter = new ArtifactConverter<>(Library.class);
        var library = converter.convert("library:com.telenav.kivakit:kivakit-core:1.8.5");
        ensureEqual(library, kivakitCore());
        ensureEqual(converter.unconvert(library), kivakitCore().name());
    }
}
