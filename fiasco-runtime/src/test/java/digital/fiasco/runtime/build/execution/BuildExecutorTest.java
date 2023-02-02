package digital.fiasco.runtime.build.execution;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.builder.Builder;
import org.junit.Test;

import java.util.HashSet;

import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_COMPILE;

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
                return root
                    .withActionAfterPhase(PHASE_COMPILE, compiled::add)
                    .withDependencies(
                        root.deriveBuilder("utilities")
                            .withActionAfterPhase(PHASE_COMPILE, compiled::add));
            }

            @Override
            protected Builder newBuilder()
            {
                return new Builder(this)
                    .withRootFolder(currentFolder().folder("project"))
                    .withArtifactDescriptor("library:com.telenav.kivakit:kivakit-core:1.8.5");
            }
        };

        build.onConfigureBuild(build.newBuilder());

        var results = new BuildExecutor(build.rootBuilder()).build();

        results.forEach(it -> ensure(it.succeeded()));

        ensure(compiled.contains(build.rootBuilder()));
        ensure(compiled.contains(build.rootBuilder().builder("utilities")));
    }
}
