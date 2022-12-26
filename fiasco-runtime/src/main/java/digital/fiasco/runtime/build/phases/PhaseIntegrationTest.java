package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.tools.builder.BuildListener;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseIntegrationTest extends Phase
{
    public PhaseIntegrationTest()
    {
        super("integration-test");
    }

    @Override
    public String description()
    {
        return "runs integration tests";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list(new PhasePrepare(),
            new PhaseCompile(),
            new PhaseTest(),
            new PhaseDocument(),
            new PhasePackage());
    }

    @Override
    public void run(BuildListener buildListener)
    {
        buildListener.onIntegrationTesting();
        buildListener.onIntegrationTest();
        buildListener.onIntegrationTested();
    }
}
