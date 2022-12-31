package digital.fiasco.runtime.build.builder.phases.standard;

import digital.fiasco.runtime.build.builder.phases.BasePhase;

@SuppressWarnings("unused")
public class PhaseEnd extends BasePhase
{
    public PhaseEnd()
    {
        super("end");
    }

    @Override
    public String description()
    {
        return "ends phase execution";
    }
}
