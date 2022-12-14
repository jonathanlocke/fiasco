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
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;
import digital.fiasco.runtime.build.builder.BuildAction;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.metadata.BuildMetadata;
import digital.fiasco.runtime.build.tasks.ArtifactResolverTask;
import digital.fiasco.runtime.build.tasks.BuilderTask;
import digital.fiasco.runtime.build.tasks.ResolvedArtifacts;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.processing.TaskExecutor;
import digital.fiasco.runtime.dependency.processing.TaskList;
import digital.fiasco.runtime.dependency.processing.TaskResult;

import java.util.Set;

import static com.telenav.kivakit.commandline.ArgumentParser.argumentParser;
import static com.telenav.kivakit.commandline.SwitchParsers.countSwitchParser;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;
import static com.telenav.kivakit.core.value.count.Count._16;
import static com.telenav.kivakit.core.vm.JavaVirtualMachine.javaVirtualMachine;
import static digital.fiasco.runtime.dependency.DependencyTree.dependencyTree;

/**
 * Base {@link Application} for Fiasco command-line builds.
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
 *     <li>{@link #newBuilder()}</li>
 *     <li>{@link #onBuild(Builder)}</li>
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
 * @author Jonathan Locke
 */
@SuppressWarnings({ "SameParameterValue", "UnusedReturnValue", "unused" })
public abstract class BaseBuild extends Application implements Build
{
    /** Metadata associated with this build */
    private BuildMetadata metadata;

    /** Switch parser for -builder-threads=[count] */
    private final SwitchParser<Count> BUILDER_THREADS = countSwitchParser(this, "builder-threads",
        "The number of threads to use when building")
        .defaultValue(javaVirtualMachine().processors().dividedBy(2))
        .build();

    /** Switch parser for -artifact-resolver-threads=[count] */
    private final SwitchParser<Count> ARTIFACT_RESOLVER_THREADS = countSwitchParser(this, "artifact-resolver-threads",
        "The number of threads to use when resolving artifacts")
        .defaultValue(_16)
        .build();

    /**
     * Creates a build
     */
    public BaseBuild()
    {
    }

    /**
     * Creates a copy of the given build
     *
     * @param that The build to copy
     */
    protected BaseBuild(BaseBuild that)
    {
        copyFrom(that);
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

    @Override
    public BuildMetadata metadata()
    {
        return metadata;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract ObjectList<Builder> onBuild(Builder rootBuilder);

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Project> projects()
    {
        return set(new GsonSerializationProject());
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
     * {@inheritDoc}
     */
    @Override
    protected final void onRun()
    {
        // Create the root builder,
        var rootBuilder = listenTo(newBuilder()
            .withArtifactDescriptor(metadata.artifactDescriptor()));

        // resolve artifacts and run builders,
        var results = build(rootBuilder);

        // then show the results.
        results.matching(at -> at.issues().hasProblems()).forEach(TaskResult::showResult);
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

    /**
     * Performs a build of the dependency tree with the given root builder
     *
     * @param rootBuilder The root builder
     * @return List of results
     */
    private ObjectList<TaskResult<Builder>> build(Builder rootBuilder)
    {
        // Get build settings,
        var settings = rootBuilder.settings();

        // configure the root builder and create any child builders,
        var builders = onBuild(rootBuilder);

        // start resolving artifacts in parallel groups and marking them as resolved,
        var resolved = new ResolvedArtifacts();
        resolveArtifacts(settings, rootBuilder, resolved);

        // start running builders in parallel groups as soon as their required artifacts are resolved.
        return executeBuilders(settings, rootBuilder, resolved);
    }

    /**
     * Returns a copy of this build
     */
    private BaseBuild copy()
    {
        var copy = typeForClass(getClass()).newInstance();
        copy.copyFrom(this);
        return copy;
    }

    /**
     * Copies the values of the given build into this build
     *
     * @param that The build to copy
     */
    private void copyFrom(BaseBuild that)
    {
        this.metadata = that.metadata;
    }

    /**
     * Executes all builders from the root in parallel groups.
     */
    private ObjectList<TaskResult<Builder>> executeBuilders(BuildSettings settings,
                                                            Builder rootBuilder,
                                                            ResolvedArtifacts resolved)
    {
        var executor = listenTo(new TaskExecutor());
        var results = new ObjectList<TaskResult<Builder>>();

        // For each group of builders that can be executed in parallel,
        for (var group : dependencyTree(rootBuilder, Builder.class).grouped())
        {
            // turn them into a list of tasks to run,
            var tasks = new TaskList<Builder>();
            for (var builder : group)
            {
                resolved.waitFor(builder.artifactDependencies());
                resolved.waitFor(builder.libraryDependencies().asArtifactList());

                var task = new BuilderTask(builder, resolved);
                tasks.add(task);
            }

            // then execute the tasks.
            results.addAll(executor.process(settings.builderThreads(), tasks));
        }

        return results;
    }

    private Builder newBuilder()
    {
        var builder = new Builder(this);
        return builder
            .withSettings(new BuildSettings(builder)
                .withBuilderThreads(get(BUILDER_THREADS))
                .withArtifactResolverThreads(get(ARTIFACT_RESOLVER_THREADS)))
            .parseCommandLine(commandLine());
    }

    /**
     * Resolves artifacts in parallel groups, marking them as resolved when completed.
     *
     * @param settings The build settings, to obtain the number of threads to use, and the librarian to resolve
     * artifacts
     * @param root The root of the builder tree
     * @param resolved The set of resolved artifacts to update
     */
    private void resolveArtifacts(BuildSettings settings, Dependency root, ResolvedArtifacts resolved)
    {
        var executor = listenTo(new TaskExecutor());
        var grouped = dependencyTree(root, Artifact.class).grouped();
        for (var group : grouped)
        {
            var task = new ArtifactResolverTask(settings.librarian(), group.asArtifactList(), resolved);
            var results = executor.process(settings.artifactResolverThreads(), new TaskList<>(list(task)));
            results.matching(at -> at.issues().hasProblems()).forEach(TaskResult::showResult);
        }
    }
}
