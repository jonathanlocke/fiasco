package digital.fiasco.runtime.build.phases.standard;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.phases.BasePhase;
import digital.fiasco.runtime.build.phases.Phase;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseDeployDocumentation extends BasePhase
{
    public PhaseDeployDocumentation()
    {
        super("deploy-documentation");
    }

    @Override
    public ObjectList<Class<? extends Phase>> dependsOnPhases()
    {
        return list(PhaseDocument.class);
    }

    @Override
    public String description()
    {
        return "deploys documentation";
    }
}
