package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.BuildListener;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseClean extends Phase
{
    public PhaseClean()
    {
        super("clean");
    }

    @Override
    public String description()
    {
        return "Clean targets";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list();
    }

    @Override
    public void run(BuildListener buildListener)
    {
        buildListener.onCleaning();
        buildListener.onClean();
        buildListener.onCleaned();
    }
}
