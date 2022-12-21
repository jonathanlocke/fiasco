package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.BuildListener;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseEnd extends Phase
{
    public PhaseEnd()
    {
        super("end");
    }

    @Override
    public String description()
    {
        return "ends the build";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list();
    }

    @Override
    public void run(BuildListener buildListener)
    {
        buildListener.onBuildStarting();
        buildListener.onBuildStart();
        buildListener.onBuildStarted();
    }
}
