package digital.fiasco.runtime.build;

import digital.fiasco.runtime.build.phases.Phase;
import digital.fiasco.runtime.build.phases.PhaseList;

public interface BuildPhased
{
    /**
     * Disables execution of the given phase
     *
     * @param phase The phase
     */
    void disable(Phase phase);

    /**
     * Enables execution of the given phase
     *
     * @param phase The phase
     */
    void enable(Phase phase);

    /**
     * Returns the phase with the given name
     *
     * @param name The phase name to look up
     * @return The phase, or null if no phase can be found with the given name
     */
    default Phase phase(String name)
    {
        return phases().phase(name);
    }

    /**
     * Returns the list of build phases
     *
     * @return The phases
     */
    PhaseList phases();
}