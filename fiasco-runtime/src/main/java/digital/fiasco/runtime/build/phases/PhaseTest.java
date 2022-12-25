package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.tools.builder.BuildListener;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseTest extends Phase
{
    public PhaseTest()
    {
        super("test");
    }

    @Override
    public String description()
    {
        return "runs unit tests";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list(new PhasePrepare(),
                new PhaseCompile());
    }

    @Override
    public void run(BuildListener buildListener)
    {
        buildListener.onTesting();
        buildListener.onTest();
        buildListener.onTested();
    }
}
