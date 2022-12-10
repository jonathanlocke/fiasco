package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.BuildListener;

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
        return "Run integration tests";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list(new PhaseBuildStart(),
                new PhasePrepare(),
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
