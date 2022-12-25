package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.tools.builder.BuildListener;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

public class PhaseCompile extends Phase
{
    public PhaseCompile()
    {
        super("compile");
    }

    @Override
    public String description()
    {
        return "compiles targets";
    }

    @Override
    public ObjectList<Phase> requiredPhases()
    {
        return list(new PhasePrepare());
    }

    @Override
    public void run(BuildListener buildListener)
    {
        buildListener.onCompiling();
        buildListener.onCompile();
        buildListener.onCompiled();

        buildListener.onTestCompiling();
        buildListener.onTestCompile();
        buildListener.onTestCompiled();
    }
}
