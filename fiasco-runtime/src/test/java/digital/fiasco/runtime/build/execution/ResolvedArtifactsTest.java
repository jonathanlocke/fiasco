package digital.fiasco.runtime.build.execution;

import com.telenav.kivakit.core.thread.KivaKitThread;
import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Duration.seconds;
import static digital.fiasco.runtime.dependency.collections.ArtifactList.artifacts;

public class ResolvedArtifactsTest extends FiascoTest
{
    @Test
    public void test()
    {
        var resolved = new ResolvedArtifacts();

        KivaKitThread.run(this, "resolver", () ->
        {
            for (var at : kivakitAssets())
            {
                seconds(0.05).sleep();
                resolved.resolve(artifacts(at));
            }
            for (var at : kivakitLibraries())
            {
                seconds(0.05).sleep();
                resolved.resolve(artifacts(at));
            }
        });

        resolved.waitForResolutionOf(kivakitAssets());
        ensure(resolved.isResolved(kivakitAssets()));

        resolved.waitForResolutionOf(kivakitLibraries());
        ensure(resolved.isResolved(kivakitLibraries()));
    }
}