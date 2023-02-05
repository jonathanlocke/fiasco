package digital.fiasco.runtime.dependency.artifact.resolver;

import com.telenav.kivakit.core.thread.KivaKitThread;
import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.resolver.ArtifactResolutionTracker;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Duration.seconds;
import static digital.fiasco.runtime.dependency.collections.lists.ArtifactList.artifacts;

public class ArtifactResolutionTrackerTest extends FiascoTest
{
    @Test
    public void test()
    {
        var resolved = new ArtifactResolutionTracker(this);

        KivaKitThread.run(this, "resolver", () ->
        {
            for (var at : kivakitAssets())
            {
                seconds(0.05).sleep();
                resolved.resolved(artifacts(at));
            }
            for (var at : kivakitLibraries())
            {
                seconds(0.05).sleep();
                resolved.resolved(artifacts(at));
            }
        });

        resolved.waitForResolutionOf(kivakitAssets());
        ensure(resolved.isResolved(kivakitAssets()));

        resolved.waitForResolutionOf(kivakitLibraries());
        ensure(resolved.isResolved(kivakitLibraries()));
    }
}
