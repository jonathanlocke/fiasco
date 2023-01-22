package digital.fiasco.runtime.repository.remote.server;

import com.telenav.kivakit.core.thread.KivaKitThread;
import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptors;

public class FiascoClientServerTest extends FiascoTest
{
    @Test
    public void test()
    {
        KivaKitThread.run(this, "test", () ->
            FiascoServer.main(new String[] { "-port=" + 8080 }));

        var descriptors = descriptors(
            "library:com.telenav.kivakit:kivakit-core:1.8.5",
            "library:com.telenav.kivakit:kivakit-core:1.8.5");

        for (var at : new FiascoClient().resolveArtifacts(descriptors))
        {
            println("-> " + at);
        }
    }
}
