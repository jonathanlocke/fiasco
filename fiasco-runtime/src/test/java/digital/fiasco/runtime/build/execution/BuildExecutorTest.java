package digital.fiasco.runtime.build.execution;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.builder.Builder;
import org.junit.Test;

import java.util.HashSet;

import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_COMPILE;

public class BuildExecutorTest extends FiascoTest
{
    @Test
    public void test()
    {
        var build = new BaseBuild()
        {
            @Override
            public Builder onConfigureBuild(Builder root)
            {
                return root.withDependencies(root.deriveBuilder("utilities"));
            }
        };

        var compiled = new HashSet<Builder>();

        var root = build.rootBuilder()
            .withActionAfterPhase(PHASE_COMPILE, compiled::add);

        var utilities = root.builder("utilities")
            .withActionAfterPhase(PHASE_COMPILE, compiled::add);

        var executor = new BuildExecutor(root);
        executor.build();
        ensure(compiled.contains(root));
        ensure(compiled.contains(utilities));
    }
}
