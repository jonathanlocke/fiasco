package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.BuildListener;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseDeployPackages extends Phase
{
    public PhaseDeployPackages()
    {
        super("deploy");
    }

    @Override
    public String description()
    {
        return "deploys packaged artifacts";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list(new PhasePrepare(),
                new PhaseCompile(),
                new PhaseTest(),
                new PhaseDocument(),
                new PhasePackage(),
                new PhaseInstall());
    }

    @Override
    public void run(BuildListener buildListener)
    {
        buildListener.onDeployingPackages();
        buildListener.onDeployPackages();
        buildListener.onDeployedPackages();
    }
}
