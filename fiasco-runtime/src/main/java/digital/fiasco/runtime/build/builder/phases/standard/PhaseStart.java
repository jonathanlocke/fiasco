package digital.fiasco.runtime.build.builder.phases.standard;

import digital.fiasco.runtime.build.builder.phases.BasePhase;

@SuppressWarnings("unused")
public class PhaseStart extends BasePhase
{
    public PhaseStart()
    {
        super("start");
    }

    @Override
    public String description()
    {
        return "starts phase execution";
    }
}
