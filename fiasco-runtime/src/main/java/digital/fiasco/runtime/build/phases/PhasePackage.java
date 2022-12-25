package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.tools.builder.BuildListener;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhasePackage extends Phase
{
    public PhasePackage()
    {
        super("package");
    }

    @Override
    public String description()
    {
        return "creates packaged artifacts";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list(new PhasePrepare(),
                new PhaseCompile(),
                new PhaseTest(),
                new PhaseDocument());
    }

    @Override
    public void run(BuildListener buildListener)
    {
        buildListener.onPackaging();
        buildListener.onPackage();
        buildListener.onPackaged();
    }
}
