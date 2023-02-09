package digital.fiasco.runtime.dependency.artifact.resolver;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.metadata.BuildMetadata;
import org.junit.Test;

import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_COMPILE;
import static digital.fiasco.runtime.build.metadata.BuildMetadata.buildMetadata;
import static digital.fiasco.runtime.dependency.collections.ArtifactList.artifacts;

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
                    .withRootFolder(currentFolder().folder("project"))
                    .withArtifactDescriptor("library:com.telenav.kivakit:kivakit-xyz:1.8.5")
                    .withEnabled(PHASE_COMPILE)
                    .withLibrarian(new MockLibrarian())
                    .withDependencies(kivakitArtifacts());

                root = root.withDependencies(
                    root.deriveBuilder("utilities")
                        .withDependencies(kivakitLibraries()));

                return root;
            }

            @Override
            public BuildMetadata onMetadata()
            {
                return buildMetadata().withDescriptor("library:digital.fiasco:fiasco-test:0.9.0");
            }

            @Override
            protected Builder newBuilder()
            {
                return new Builder(this)
                    .withArtifactDescriptor(metadata().descriptor());
            }
        };

        var resolved = new ArtifactResolutionTracker(this);
        new ArtifactResolver(build, resolved).resolveArtifacts();

        resolved.waitForResolutionOf(kivakitArtifacts());
        ensure(resolved.isResolved(kivakitArtifacts()));
        ensure(resolved.isResolved(artifacts(kivakitResource())));
        ensure(resolved.size() == 5);
    }
}
