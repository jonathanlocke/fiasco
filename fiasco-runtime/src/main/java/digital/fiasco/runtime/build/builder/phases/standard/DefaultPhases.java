package digital.fiasco.runtime.build.builder.phases.standard;

import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;

import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_ASSEMBLE;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_CLEAN;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_COMPILE;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_DEPLOY;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_DEPLOY_DOCUMENTATION;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_DOCUMENT;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_END;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_INSTALL;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_INTEGRATION_TEST;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_PREPARE;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_START;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_TEST;

/**
 * Defines a standard list of phases, each with a default implementation:
 *
 * <ol>
 *     <li>start - start of phases</li>
 *     <li>clean - removes target files</li>
 *     <li>prepare - prepares sources and resources for compilation</li>
 *     <li>compile - compiles sources</li>
 *     <li>test - runs unit tests</li>
 *     <li>document - creates documentation</li>
 *     <li>package - assembles targets into packages</li>
 *     <li>integration-test - runs integration tests</li>
 *     <li>install - installs packages in local repository</li>
 *     <li>deploy-packages - deploys packages to remote repositories</li>
 *     <li>deploy-documentation - deploys documentation</li>
 *     <li>end - end of phases</li>
 * </ol>
 *
 * <p>
 * The default implementation for each phase can be found in the method onX(), where X is the phase name.
 * To add new phases to this default list, use {@link #addPhaseBefore(String, Phase)} and {@link #addPhaseAfter(String, Phase)}.
 * To replace a phase with a new definition, use {@link #replacePhase(String, Phase)}.
 * </p>
 *
 * @author Jonathan Locke
 */
public class DefaultPhases extends PhaseList
{
    /**
     * Creates the default phase list
     */
    public DefaultPhases()
    {
        add(PHASE_START);
        add(PHASE_CLEAN);
        add(PHASE_PREPARE);
        add(PHASE_COMPILE);
        add(PHASE_TEST);
        add(PHASE_DOCUMENT);
        add(PHASE_ASSEMBLE);
        add(PHASE_INTEGRATION_TEST);
        add(PHASE_INSTALL);
        add(PHASE_DEPLOY);
        add(PHASE_DEPLOY_DOCUMENTATION);
        add(PHASE_END);

        enable(PHASE_START);
        enable(PHASE_END);
    }
}
