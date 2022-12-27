package digital.fiasco.runtime.build.phases.standard;

import digital.fiasco.runtime.build.phases.BasePhase;

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
