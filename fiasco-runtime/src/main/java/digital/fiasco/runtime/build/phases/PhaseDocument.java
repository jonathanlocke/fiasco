package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.BuildListener;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseDocument extends Phase
{
    public PhaseDocument()
    {
        super("document");
    }

    @Override
    public String description()
    {
        return "Build documentation";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list(new PhaseBuildStart());
    }

    @Override
    public void run(BuildListener buildListener)
    {
        buildListener.onDocumenting();
        buildListener.onDocument();
        buildListener.onDocumented();
    }
}
