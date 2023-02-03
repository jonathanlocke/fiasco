package digital.fiasco.runtime.build.execution;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.builder.Builder;
import org.junit.Test;

import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_COMPILE;

public class ArtifactResolverTest extends FiascoTest
{
    @Test
    public void test()
    {
        var build = new BaseBuild()
        {
            @Override
            public Builder onConfigureBuild(Builder root)
            {
                root = root
                    .withEnabled(PHASE_COMPILE)
                    .withDependencies(kivakitArtifacts());

                root = root.withDependencies(
                    root.deriveBuilder("utilities")
                        .withDependencies(kivakitLibraries()));

                return root;
            }

            @Override
            protected Builder newBuilder()
            {
                return new Builder(this)
                    .withRootFolder(currentFolder().folder("project"))
                    .withArtifactDescriptor("library:com.telenav.kivakit:kivakit-xyz:1.8.5");
            }
        };

        var resolved = new ResolvedArtifacts(this);
        var resolver = new ArtifactResolver(this);
        resolver.resolveArtifacts(build.rootBuilder(), resolved);

        resolved.waitForResolutionOf(kivakitArtifacts());
        ensure(resolved.size() == 4);
    }
}
