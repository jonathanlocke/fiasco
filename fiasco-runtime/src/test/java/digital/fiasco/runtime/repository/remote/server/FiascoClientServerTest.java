package digital.fiasco.runtime.repository.remote.server;

import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.settings.SettingsTrait;
import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static digital.fiasco.runtime.repository.remote.server.FiascoClient.fiascoClient;

public class FiascoClientServerTest extends FiascoTest implements SettingsTrait
{
    @Test
    public void test()
    {
        startServer();

        for (var at : fiascoClient().resolveArtifacts(kivakitAssets().asArtifactDescriptors()))
        {
            println("-> " + at);
        }
    }

    private void startServer()
    {
        registerSettings(new FiascoServerSettings()
            .server(true)
            .port(8080));

        KivaKitThread.run(this, "test", () ->
            FiascoServer.main(new String[] {}));
    }
}
