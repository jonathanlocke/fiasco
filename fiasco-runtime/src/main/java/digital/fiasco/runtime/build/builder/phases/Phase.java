package digital.fiasco.runtime.build.builder.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.string.Described;
import digital.fiasco.runtime.build.builder.BuildAction;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.standard.PhaseClean;
import digital.fiasco.runtime.build.builder.phases.standard.PhaseCompile;
import digital.fiasco.runtime.build.builder.phases.standard.PhaseDeployDocumentation;
import digital.fiasco.runtime.build.builder.phases.standard.PhaseDeployPackages;
import digital.fiasco.runtime.build.builder.phases.standard.PhaseDocument;
import digital.fiasco.runtime.build.builder.phases.standard.PhaseEnd;
import digital.fiasco.runtime.build.builder.phases.standard.PhaseInstall;
import digital.fiasco.runtime.build.builder.phases.standard.PhaseIntegrationTest;
import digital.fiasco.runtime.build.builder.phases.standard.PhasePackage;
import digital.fiasco.runtime.build.builder.phases.standard.PhasePrepare;
import digital.fiasco.runtime.build.builder.phases.standard.PhaseStart;
import digital.fiasco.runtime.build.builder.phases.standard.PhaseTest;

/**
 * Base class for phases. A phase has a name, a description, and a set of dependent phases, accessed with
 * {@link #dependsOnPhases()}. Code can be attached to a phase with:
 *
 * <p><b>Build Actions</b></p>
 *
 * <ul>
 *     <li>{@link #beforePhase(BuildAction)}</li>
 *     <li>{@link #duringPhase(BuildAction)}</li>
 *     <li>{@link #afterPhase(BuildAction)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface Phase extends
    Named,
    Described
{
    Phase PHASE_START = new PhaseStart();

    Phase PHASE_CLEAN = new PhaseClean();

    Phase PHASE_PREPARE = new PhasePrepare();

    Phase PHASE_COMPILE = new PhaseCompile();

    Phase PHASE_TEST = new PhaseTest();

    Phase PHASE_DOCUMENT = new PhaseDocument();

    Phase PHASE_PACKAGE = new PhasePackage();

    Phase PHASE_INTEGRATION_TEST = new PhaseIntegrationTest();

    Phase PHASE_INSTALL = new PhaseInstall();

    Phase PHASE_DEPLOY_PACKAGES = new PhaseDeployPackages();

    Phase PHASE_DEPLOY_DOCUMENTATION = new PhaseDeployDocumentation();

    Phase PHASE_END = new PhaseEnd();

    /**
     * Runs the given code <i>after</i> this phase runs
     *
     * @param code The code to run
     * @return This phase for chaining
     */
    Phase afterPhase(BuildAction code);

    /**
     * Runs the given code <i>before</i> this phase runs
     *
     * @param code The code to run
     * @return This phase for chaining
     */
    Phase beforePhase(BuildAction code);

    /**
     * Returns the list of phases that must complete before this phase can be run
     */
    ObjectList<Class<? extends Phase>> dependsOnPhases();

    /**
     * Runs the given code <i>when</i> this phase runs
     *
     * @param code The code to run
     */
    Phase duringPhase(BuildAction code);

    /**
     * <p><b>Internal API</b></p>
     *
     * <p>
     * Called after this phase runs
     * </p>
     */
    void internalOnAfter(Builder builder);

    /**
     * <p><b>Internal API</b></p>
     *
     * <p>
     * Called before this phase runs
     * </p>
     */
    void internalOnBefore(Builder builder);

    /**
     * <p><b>Internal API</b></p>
     *
     * <p>
     * Called when this phase runs
     * </p>
     */
    void internalOnRun(Builder builder);
}
