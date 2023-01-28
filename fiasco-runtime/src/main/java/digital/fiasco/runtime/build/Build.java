package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.string.Described;
import digital.fiasco.runtime.build.builder.BuildAction;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.metadata.BuildMetadata;
import digital.fiasco.runtime.build.settings.BuildOption;
import digital.fiasco.runtime.build.settings.BuildProfile;
import digital.fiasco.runtime.build.settings.BuildSettings;
import digital.fiasco.runtime.build.settings.BuildSettingsMixin;
import digital.fiasco.runtime.build.settings.BuildSettingsObject;

/**
 * Defines a Fiasco build.
 *
 * <p>
 * The {@link #onConfigureBuild(Builder)} method is called to obtain a list of configured {@link Builder}s to be
 * executed to perform the build. Typically, each builder builds a single project. Multi-project builds are possible
 * simply by returning more than one builder (child builders can be created with {@link Builder#deriveBuilder(String)}).
 * Dependencies between builders and artifacts are used to determine the order of execution of builders and the order in
 * which artifacts are resolved from repositories.
 * </p>
 *
 * <p><b>Build Settings</b></p>
 *
 * <p>
 * The {@link BuildSettingsObject} class holds configuration information for the build. Some of this information is used
 * to initialize the root builder passed to {@link #onConfigureBuild(Builder)}. Some build settings are switched on and
 * off by the {@link BaseBuild} application's built-in switches:
 * </p>
 *
 * <ul>
 *     <li>-builder-threads=[count] - sets the number of threads to use for executing {@link Builder}s</li>
 *     <li>-artifact-resolver-threads=[count] - sets the number of threads to use for resolving artifacts</li>
 * </ul>
 *
 * <p>
 * In addition, options, phases (below) and user-defined profiles can be enabled or disabled from the command line as well:
 * </p>
 *
 * <i>Options</i>
 *
 * <ul>
 *     <li>describe</li>
 *     <li>quiet</li>
 *     <li>debug</li>
 *     <li>help</li>
 * </ul>
 *
 * <p><b>Build Phases</b></p>
 *
 * <p>
 * Build phases are executed in a pre-defined order, as below. Each phase is defined by a class that extends
 * {@link Phase}. Additional phases can be inserted into {@link Builder#phases()} with
 * {@link PhaseList#addPhaseAfter(String, Phase)} and {@link PhaseList#addPhaseBefore(String, Phase)}.
 * </p>
 *
 * <p>
 * Phases are enabled and disabled with {@link Builder#enable(Phase)} and {@link Builder#disable(Phase)}. Code can be
 * executed before, during or after a phase runs by calling {@link Builder#beforePhase(String, BuildAction)},
 * {@link Builder#onPhase(String, BuildAction)}, and {@link Builder#afterPhase(String, BuildAction)}. The
 * {@link BaseBuild} application enables and disables any phase names that were passed from the command line. The phase
 * name itself enables the phase and any dependent phases (for example, "compile" enables the "build-start", "prepare"
 * and "compile" phases). If the phase name is preceded by a dash (for example, -test), the phase is disabled (but not
 * its dependent phases).
 * </p>
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
 * <p><b>Examples</b></p>
 *
 * <table>
 *     <caption>Examples</caption>
 *     <tr>
 *         <td>fiasco</td> <td>&nbsp;&nbsp;</td> <td>show help</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco clean</td> <td>&nbsp;&nbsp;</td> <td>clean targets</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco compile</td> <td>&nbsp;&nbsp;</td> <td>prepare sources and compile</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco clean compile</td> <td>&nbsp;&nbsp;</td> <td>clean targets and compile</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco package</td> <td>&nbsp;&nbsp;</td> <td>compile, test, document and build packages</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco package -test</td> <td>&nbsp;&nbsp;</td> <td>build packages but don't run tests</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco install -test -document</td> <td>&nbsp;&nbsp;</td> <td>install packages but don't run tests or document</td>
 *     </tr>
 * </table>
 *
 * <p><b>Building</b></p>
 *
 * <ul>
 *     <li>{@link #onConfigureBuild(Builder)}</li>
 * </ul>
 *
 * <p><b>Build Metadata</b></p>
 *
 * <ul>
 *     <li>{@link #name()}</li>
 *     <li>{@link #description()}</li>
 *     <li>{@link #metadata()}</li>
 * </ul>
 *
 * <p><b>Build Environment</b></p>
 *
 * <ul>
 *     <li>{@link #isMac()}</li>
 *     <li>{@link #isUnix()}</li>
 *     <li>{@link #isWindows()}</li>
 *     <li>{@link #operatingSystemType()}</li>
 *     <li>{@link #environmentVariables()}</li>
 *     <li>{@link #property(String)}</li>
 *     <li>{@link #processorArchitecture()}</li>
 *     <li>{@link #processors()}</li>
 *     <li>{@link #javaHome()}</li>
 *     <li>{@link #mavenHome()}</li>
 * </ul>
 *
 * <p><b>Build Repositories</b></p>
 *
 * <ul>
 *     <li>{@link BuildRepositoriesTrait#MAVEN_CENTRAL}</li>
 *     <li>{@link BuildRepositoriesTrait#MAVEN_CENTRAL_STAGING}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see BuildSettings
 * @see BuildOption
 * @see BuildProfile
 * @see BuildEnvironmentTrait
 * @see BuildRepositoriesTrait
 * @see Named
 * @see Described
 */
@SuppressWarnings("UnusedReturnValue")
public interface Build extends
    Named,
    Described,
    Repeater,
    BuildEnvironmentTrait,
    BuildRepositoriesTrait,
    BuildSettingsMixin
{

    /**
     * Returns a copy of this object
     *
     * @return This object for method chaining
     */
    Build copy();

    /**
     * Returns the metadata for this build
     */
    BuildMetadata metadata();

    /**
     * Called to configure the root builder for a build. In the case of multi-project builds, this may involve adding
     * child builders to the root builder. T
     *
     * @param root The root builder for the build, from which child builders can be derived
     * @return A newly configured root builder based on the given root builder
     */
    Builder onConfigureBuild(Builder root);
}
