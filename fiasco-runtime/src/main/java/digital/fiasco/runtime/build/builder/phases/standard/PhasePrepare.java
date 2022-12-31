package digital.fiasco.runtime.build.builder.phases.standard;

import digital.fiasco.runtime.build.builder.phases.BasePhase;

public class PhasePrepare extends BasePhase
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
}
