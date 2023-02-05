package digital.fiasco.runtime.librarian;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.builder.Builder;
import org.junit.Test;

import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_COMPILE;
import static digital.fiasco.runtime.dependency.collections.lists.ArtifactList.artifacts;

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
                    .withArtifactDescriptor("library:com.telenav.kivakit:kivakit-xyz:1.8.5")
                    .withLibrarian(new MockLibrarian());
            }
        };

        var resolved = new ResolvedArtifactSet(this);
        new ArtifactResolver(build, resolved).resolveArtifacts();

        resolved.waitForResolutionOf(kivakitArtifacts());
        ensure(resolved.isResolved(kivakitArtifacts()));
        ensure(resolved.isResolved(artifacts(kivakitResource())));
        ensure(resolved.size() == 5);
    }
}
