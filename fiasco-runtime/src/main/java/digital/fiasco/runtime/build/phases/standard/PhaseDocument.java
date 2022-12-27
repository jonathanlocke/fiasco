package digital.fiasco.runtime.build.phases.standard;

import digital.fiasco.runtime.build.phases.BasePhase;

public class PhaseDocument extends BasePhase
{
    public PhaseDocument()
    {
        super("document");
    }

    @Override
    public String description()
    {
        return "builds documentation";
    }
}
