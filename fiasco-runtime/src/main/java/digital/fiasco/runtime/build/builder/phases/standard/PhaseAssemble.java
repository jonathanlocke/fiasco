package digital.fiasco.runtime.build.builder.phases.standard;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.builder.phases.BasePhase;
import digital.fiasco.runtime.build.builder.phases.Phase;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseAssemble extends BasePhase
{
    public PhaseAssemble()
    {
        super("assemble");
    }

    @Override
    public ObjectList<Class<? extends Phase>> dependsOnPhases()
    {
        return list
            (
                PhasePrepare.class,
                PhaseCompile.class,
                PhaseTest.class,
                PhaseDocument.class
            );
    }

    @Override
    public String description()
    {
        return "creates packaged artifacts";
    }
}
