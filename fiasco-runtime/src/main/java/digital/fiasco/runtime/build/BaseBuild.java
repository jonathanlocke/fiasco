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
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;
import digital.fiasco.runtime.build.builder.BuildAction;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.build.metadata.BuildMetadata;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.processing.TaskExecutor;
import digital.fiasco.runtime.dependency.processing.Task;
import digital.fiasco.runtime.dependency.processing.TaskResult;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.telenav.kivakit.commandline.ArgumentParser.argumentParser;
import static com.telenav.kivakit.commandline.SwitchParsers.threadCountSwitchParser;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;
import static digital.fiasco.runtime.dependency.DependencyTree.dependencyTree;
import static digital.fiasco.runtime.dependency.processing.TaskResult.taskResult;

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
    private record BuildTask(Builder builder) implements Task
    {
        @Override
        public TaskResult call()
        {
            return builder.build();
        }

        @Override
        public String name()
        {
            return builder.name();
        }
    }

    private record ResolveArtifactTask(Librarian librarian, Artifact artifact) implements Task
    {
        @Override
        public TaskResult call()
        {
            var issues = new MessageList();
            issues.capture(() -> librarian.resolve(artifact.artifactDescriptor()),
                "Unable to resolve artifact: $", artifact);
            return taskResult(artifact, issues);
        }

        @Override
        public String name()
        {
            return artifact.name();
        }
    }

    /** Metadata associated with this build */
    private BuildMetadata metadata;

    /** Switch parser for --threads=[count], with a maximum of 64 threads */
    private final SwitchParser<Count> THREAD_COUNT = threadCountSwitchParser(this, Count._64);

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

        var results = build(rootBuilder);

        for (var result : results)
        {
            result.showResult();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return set(THREAD_COUNT);
    }

    /**
     * Performs a build of the dependency tree with the given root builder
     *
     * @param rootBuilder The root builder
     * @return List of results
     */
    private ObjectList<TaskResult> build(Builder rootBuilder)
    {
        // Call the build subclass to configure the root builder and create any child builders,
        var builders = onBuild(rootBuilder);

        // get the dependency tree from the root builder,
        var dependencyTree = dependencyTree(rootBuilder);

        // then process the dependency.
        var settings = rootBuilder.settings();
        return listenTo(new TaskExecutor(dependencyTree))
            .execute(settings.threads(), dependency ->
            {
                if (dependency instanceof Builder builder)
                {
                    return new BuildTask(builder);
                }
                if (dependency instanceof Artifact artifact)
                {
                    return new ResolveArtifactTask(settings.librarian(), artifact);
                }
                return fail("Unknown dependency type");
            });
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

    private Builder newBuilder()
    {
        var builder = new Builder(this);
        return builder
            .withSettings(new BuildSettings(builder)
                .withThreads(get(THREAD_COUNT)))
            .parseCommandLine(commandLine());
    }

    @NotNull
    private TaskResult resolve(BuildSettings settings, Dependency dependency)
    {
        var issues = new MessageList();
        try
        {
            settings.librarian().resolve(dependency.artifactDescriptor());
        }
        catch (Exception e)
        {
            issues.problem(e, "Unable to resolve: $", dependency);
        }
        return taskResult(dependency, issues);
    }
}
