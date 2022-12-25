package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.tools.builder.BuildListener;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseInstall extends Phase
{
    public PhaseInstall()
    {
        super("install");
    }

    @Override
    public String description()
    {
        return "installs packaged artifacts";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list(new PhasePrepare(),
                new PhaseCompile(),
                new PhaseTest(),
                new PhaseDocument(),
                new PhasePackage(),
                new PhaseIntegrationTest());
    }

    @Override
    public void run(BuildListener buildListener)
    {
        buildListener.onInstallingPackages();
        buildListener.onInstallPackages();
        buildListener.onInstalledPackages();
    }
}
