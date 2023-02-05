//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.commandline.ArgumentParser;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.conversion.core.language.IdentityConverter;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.string.Described;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;
import digital.fiasco.runtime.build.builder.BuildAction;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.environment.BuildEnvironmentTrait;
import digital.fiasco.runtime.build.environment.BuildRepositoriesTrait;
import digital.fiasco.runtime.build.execution.BuildExecutor;
import digital.fiasco.runtime.build.metadata.BuildMetadata;
import digital.fiasco.runtime.build.settings.BuildOption;
import digital.fiasco.runtime.build.settings.BuildProfile;
import digital.fiasco.runtime.build.settings.BuildSettings;
import digital.fiasco.runtime.build.settings.BuildSettingsObject;
import digital.fiasco.runtime.dependency.collections.DependencyTree;
import digital.fiasco.runtime.repository.remote.server.serialization.FiascoGsonFactory;

import java.util.Set;

import static com.telenav.kivakit.commandline.ArgumentParser.argumentParser;
import static com.telenav.kivakit.commandline.SwitchParsers.countSwitchParser;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;
import static com.telenav.kivakit.core.value.count.Count._0;
import static com.telenav.kivakit.core.value.count.Count._16;
import static com.telenav.kivakit.core.vm.JavaVirtualMachine.javaVirtualMachine;
import static digital.fiasco.runtime.build.settings.BuildSettings.buildSettings;

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
 * Phases are enabled and disabled with {@link Builder#withEnabled(Phase)} and {@link Builder#withDisabled(Phase)}. Code can be
 * executed before, during or after a phase runs by calling {@link Builder#withActionBeforePhase(Phase, BuildAction)},
 * {@link Builder#withActionDuringPhase(Phase, BuildAction)}, and {@link Builder#withActionAfterPhase(Phase, BuildAction)}. The
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
@SuppressWarnings({ "SameParameterValue", "UnusedReturnValue", "unused" })
public abstract class BaseBuild extends Application implements Build
{
    /** Metadata associated with this build */
    private BuildMetadata metadata;

    /** Switch parser for -builder-threads=[count] */
    private final SwitchParser<Count> BUILDER_THREADS = countSwitchParser(this, "builder-threads",
        "The number of threads to use when building")
        .optional()
        .defaultValue(javaVirtualMachine().processors())
        .build();

    /** Switch parser for -artifact-resolver-threads=[count] */
    private final SwitchParser<Count> ARTIFACT_RESOLVER_THREADS = countSwitchParser(this, "artifact-resolver-threads",
        "The number of threads to use when resolving artifacts")
        .optional()
        .defaultValue(_16)
        .build();

    /** The dependency tree for this build */
    private final DependencyTree dependencyTree;

    /**
     * Creates a build
     */
    public BaseBuild()
    {
        var rootBuilder = onConfigureBuild(newBuilder());
        dependencyTree = new DependencyTree(rootBuilder);
    }

    /**
     * Creates a copy of the given build
     *
     * @param that The build to copy
     */
    protected BaseBuild(BaseBuild that)
    {
        this.metadata = that.metadata;
        this.dependencyTree = that.dependencyTree;
    }

    /**
     * Returns a copy of this build
     */
    @Override
    public BaseBuild copy()
    {
        var copy = typeForClass(getClass()).newInstance();
        copy.metadata = this.metadata;
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public DependencyTree dependencyTree()
    {
        return dependencyTree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description()
    {
        return """
            Commands
                            
              command               description
              -----------           ---------------------------------------------
              describe              describe the build rather than running it
                            
            """ + newBuilder().description();
    }

    /**
     * Executes this build using a {@link BuildExecutor}
     *
     * @return The builder results (one for each project)
     */
    public ObjectList<Result<Builder>> executeBuild()
    {
        // Create the root builder,
        var root = newBuilder().withArtifactDescriptor(metadata.descriptor());

        // configure and run the build,
        return new BuildExecutor(this).run();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BuildMetadata metadata()
    {
        return metadata;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Set<Project> projects()
    {
        return set(new GsonSerializationProject());
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean shouldDescribe()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean shouldDescribeAndExecute()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ObjectList<ArgumentParser<?>> argumentParsers()
    {
        return list(argumentParser(String.class)
            .oneOrMore()
            .converter(new IdentityConverter(this))
            .description("Build options and phases")
            .build());
    }

    /**
     * Creates a new builder with settings initialized to defaults and configured by the command line for this build
     *
     * @return The builder
     */
    protected Builder newBuilder()
    {
        var builder = new Builder(this);
        return builder
            .withSettings(buildSettings(builder)
                .withBuilderThreads(get(BUILDER_THREADS))
                .withArtifactResolverThreads(get(ARTIFACT_RESOLVER_THREADS)))
            .withParsedCommandLine(commandLine());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void onRun()
    {
        // Execute the build,
        var results = executeBuild();

        // then show the results.
        var problems = _0;
        for (var at : results)
        {
            at.messages().broadcastTo(this);
            problems = problems.plus(at.messages().problems());
        }

        information("Build completed with $ problems", problems);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSerializationInitialize()
    {
        super.onSerializationInitialize();

        register(new FiascoGsonFactory());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return super.switchParsers().with(
            BUILDER_THREADS,
            ARTIFACT_RESOLVER_THREADS);
    }
}
