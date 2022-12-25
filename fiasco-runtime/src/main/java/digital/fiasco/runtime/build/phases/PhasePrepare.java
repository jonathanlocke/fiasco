package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.tools.builder.BuildListener;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhasePrepare extends Phase
{
    public PhasePrepare()
    {
        super("prepare");
    }

    @Override
    public String description()
    {
        return "prepares resources and sources for compilation";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list();
    }

    @Override
    public void run(BuildListener buildListener)
    {
        buildListener.onPreparingResources();
        buildListener.onPrepareResources();
        buildListener.onPreparedResources();

        buildListener.onPreparingSources();
        buildListener.onPrepareSources();
        buildListener.onPreparedSources();

        buildListener.onPreparingTestResources();
        buildListener.onPrepareTestResources();
        buildListener.onPrepareTestedResources();

        buildListener.onPreparingTestSources();
        buildListener.onPrepareTestSources();
        buildListener.onPreparedTestSources();
    }
}
