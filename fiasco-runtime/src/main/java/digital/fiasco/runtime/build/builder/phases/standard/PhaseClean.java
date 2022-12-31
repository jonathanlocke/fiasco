package digital.fiasco.runtime.build.builder.phases.standard;

import digital.fiasco.runtime.build.builder.phases.BasePhase;

public class PhaseClean extends BasePhase
{
    public PhaseClean()
    {
        super("clean");
    }

    @Override
    public String description()
    {
        return "cleans targets";
    }
}
