package digital.fiasco.runtime.repository.remote.server;

import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.settings.SettingsTrait;
import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Duration.seconds;
import static digital.fiasco.runtime.repository.Repository.InstallationResult.INSTALLED;
import static digital.fiasco.runtime.repository.remote.server.FiascoClient.fiascoClient;

public class FiascoClientServerTest extends FiascoTest implements SettingsTrait
{
    @Test
    public void test()
    {
        startServer();
        seconds(0.5).sleep();

        var resolved = fiascoClient().resolveArtifacts(kivakitAssets().asArtifactDescriptors());

        ensureNotNull(resolved);
        ensure(resolved.size() == 2);
        ensure(resolved.asArtifactDescriptors().contains(kivakitIcons().descriptor()));
        ensure(resolved.asArtifactDescriptors().contains(kivakitLogos().descriptor()));
        ensure(!resolved.asArtifactDescriptors().contains(kivakitCore().descriptor()));
    }

    private void startServer()
    {
        var repository = register(localRepository());
        repository.clear();

        ensure(repository.installArtifact(kivakitCore().withContent(packageContent())) == INSTALLED);
        ensure(repository.installArtifact(kivakitIcons().withContent(packageContent())) == INSTALLED);
        ensure(repository.installArtifact(kivakitLogos().withContent(packageContent())) == INSTALLED);

        register(new FiascoServerSettings()
            .server(true)
            .port(8080));

        var server = new FiascoServer();
        KivaKitThread.run(this, "FiascoTest", () -> server.run(new String[] {}));
        server.waitForReady();
    }
}
