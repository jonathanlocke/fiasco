package digital.fiasco.runtime.build.phases.standard;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.phases.BasePhase;
import digital.fiasco.runtime.build.phases.Phase;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseTest extends BasePhase
{
    public PhaseTest()
    {
        super("test");
    }

    @Override
    public ObjectList<Class<? extends Phase>> dependsOnPhases()
    {
        return list
            (
                PhasePrepare.class,
                PhaseCompile.class
            );
    }

    @Override
    public String description()
    {
        return "runs unit tests";
    }
}
