package digital.fiasco.runtime.build.execution;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.metadata.BuildMetadata;
import org.junit.Test;

import java.util.HashSet;

import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_COMPILE;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_PREPARE;
import static digital.fiasco.runtime.build.metadata.BuildMetadata.buildMetadata;
import static digital.fiasco.runtime.build.settings.BuildSettings.buildSettings;

public class BuildExecutorTest extends FiascoTest
{
    @Test
    public void test()
    {
        var compiled = new HashSet<Builder>();

        var build = new BaseBuild()
        {
            @Override
            public Builder onConfigureBuild(Builder root)
            {
                root = root
                    .withEnabled(PHASE_COMPILE)
                    .withRootFolder(currentFolder().folder("../fiasco-example"))
                    .withoutActions(PHASE_PREPARE)
                    .withoutActions(PHASE_COMPILE)
                    .withActionAfterPhase(PHASE_COMPILE, compiled::add);

                return root;
            }

            @Override
            public BuildMetadata onMetadata()
            {
                return buildMetadata().withDescriptor("library:digital.fiasco:fiasco-example:0.9.0");
            }

            @Override
            protected Builder newBuilder()
            {
                var builder = new Builder(this);
                return builder
                    .withSettings(buildSettings(builder)
                        .withEnabled(PHASE_COMPILE)
                        .withArtifactDescriptor(metadata().descriptor()));
            }
        };

        var results = new BuildExecutor(build).run();

        results.forEach(it -> ensure(it.succeeded()));

        ensure(compiled.contains(build.root()));
    }
}
