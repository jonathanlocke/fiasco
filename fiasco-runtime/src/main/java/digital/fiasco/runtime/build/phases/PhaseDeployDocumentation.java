package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.BuildListener;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseDeployDocumentation extends Phase
{
    public PhaseDeployDocumentation()
    {
        super("deploy-documentation");
    }

    @Override
    public String description()
    {
        return "Deploy documentation";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list(new PhaseDocument());
    }

    @Override
    public void run(BuildListener buildListener)
    {
        buildListener.onDeployingDocumentation();
        buildListener.onDeployDocumentation();
        buildListener.onDeployedDocumentation();
    }
}
