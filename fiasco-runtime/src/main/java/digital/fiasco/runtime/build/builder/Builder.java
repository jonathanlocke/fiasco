package digital.fiasco.runtime.build.builder;

import com.telenav.kivakit.commandline.CommandLine;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.language.trait.TryCatchTrait;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.string.Described;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.BuildArtifact;
import digital.fiasco.runtime.build.BuildAssociated;
import digital.fiasco.runtime.build.BuildEnvironment;
import digital.fiasco.runtime.build.BuildStructured;
import digital.fiasco.runtime.build.phases.BasePhase;
import digital.fiasco.runtime.build.phases.Phase;
import digital.fiasco.runtime.build.phases.PhaseList;
import digital.fiasco.runtime.build.phases.standard.StandardPhases;
import digital.fiasco.runtime.build.tools.ToolFactory;
import digital.fiasco.runtime.build.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;

import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.string.AsciiArt.bannerLine;
import static com.telenav.kivakit.core.string.Paths.pathTail;
import static digital.fiasco.runtime.build.builder.BuildOption.DESCRIBE;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;

/**
 * Builds a project by executing a series of phases. A {@link Build} may have multiple {@link Builder}s, to achieve a
 * multi-project build or to achieve two different kinds of builds.
 *
 * <p><b>Building</b></p>
 *
 * <ul>
 *     <li>{@link #build()}</li>
 *     <li>{@link #build(Count)}</li>
 * </ul>
 *
 * <p><b>Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #librarian()}</li>
 * </ul>
 *
 * <p><b>Build Phases</b></p>
 *
 * <p>
 * By default, phases are executed in a pre-defined order, as shown below. Each phase is defined by a class that implements
 * {@link Phase} (and normally extends {@link BasePhase}). Additional phases can be inserted into the {@link #phases()} list with
 * {@link PhaseList#addPhaseAfter(String, Phase)} and {@link PhaseList#addPhaseBefore(String, Phase)}. A specific
 * phase can be replaced with a new definition with {@link PhaseList#replacePhase(String, Phase)}. Alternatively, an
 * entirely new phase list can be installed with {@link #withPhases(PhaseList)}.
 * </p>
 *
 * <ul>
 *     <li>{@link #phases()}</li>
 *     <li>{@link #withPhases(PhaseList)}</li>
 * </ul>
 *
 * <p><b>Attaching Actions to Phases</b></p>
 *
 * <p>
 * A phase can be retrieved by name with {@link #phase(String)} and an arbitrary {@link Runnable}
 * can be attached to run before, during or after the phase executes.
 * </p>
 *
 * <p>
 * Phases are enabled and disabled with {@link #enable(Phase)} and {@link #disable(Phase)}. The {@link #parseCommandLine(CommandLine)}
 * method enables and disables any phases or build options that were passed in the command line. The phase name itself
 * enables the phase and any dependent phases (for example, "compile" enables the "start", "prepare" and "compile" phases).
 * If the phase name is preceded by a dash (for example, -test), the phase is disabled (but not its dependent phases).
 * </p>
 *
 * <ul>
 *     <li>{@link #phase(String)}</li>
 *     <li>{@link #isEnabled(Phase)}</li>
 *     <li>{@link #enable(Phase)}</li>
 *     <li>{@link #disable(Phase)}</li>
 * </ul>
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
 * <p><b>Building</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #disable(BuildOption)}</li>
 *     <li>{@link #enable(BuildOption)}</li>
 *     <li>{@link #isEnabled(BuildOption)}</li>
 *     <li>{@link #rootFolder()}</li>
 * </ul>
 *
 * <p><b>Artifact</b></p>
 *
 * <ul>
 *     <li>{@link #artifactDescriptor()}</li>
 *     <li>{@link #artifactName()}</li>
 *     <li>{@link #artifactVersion()}</li>
 *     <li>{@link #withArtifactDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withArtifactDescriptor(String)}</li>
 *     <li>{@link #withArtifactIdentifier(String)}</li>
 *     <li>{@link #withArtifactVersion(String)}</li>
 *     <li>{@link #withArtifactVersion(Version)}</li>
 * </ul>
 *
 * <p><b>Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies()}</li>
 *     <li>{@link #pinVersion(Artifact, String)}</li>
 *     <li>{@link #pinVersion(Artifact, Version)}</li>
 *     <li>{@link #requires(Artifact, Artifact...)}</li>
 *     <li>{@link #requires(DependencyList)}</li>
 * </ul>
 *
 * <p><b>Build Structure</b></p>
 *
 * <ul>
 *     <li>{@link #sourceFolder()} - src</li>
 *     <li>{@link #sourceMainJavaFolder()} - src/main/java</li>
 *     <li>{@link #sourceMainResourcesFolder()} - src/main/resources</li>
 *     <li>{@link #sourceTestFolder()} - src/test</li>
 *     <li>{@link #sourceTestJavaFolder()} - src/test/java</li>
 *     <li>{@link #sourceTestResourcesFolder()} - src/test/resources</li>
 *     <li>{@link #targetClassesFolder()} - target/classes</li>
 *     <li>{@link #targetTestClassesFolder()} - target/test-classes</li>
 *     <li>{@link #sourceMainJavaSources()} - src/main/java/**.java</li>
 *     <li>{@link #sourceMainResources()} - src/main/resources/**</li>
 *     <li>{@link #sourceTestJavaSources()} - src/test/java/**.java</li>
 *     <li>{@link #sourceTestResources()} - src/test/resources/**</li>
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
@SuppressWarnings({ "unused", "StringConcatenationInLoop", "UnusedReturnValue" })
public class Builder extends BaseRepeater implements
    Described,
    BuildArtifact,
    BuildAssociated,
    BuilderAssociated,
    BuildOptioned,
    BuildStructured,
    BuildEnvironment,
    BuildDependencies,
    ToolFactory,
    TryCatchTrait
{
    /** The associated build */
    private final Build build;

    /** The primary artifact being built */
    private ArtifactDescriptor artifactDescriptor;

    /** Phases in order of execution */
    private PhaseList phases = new StandardPhases(this);

    /** The librarian to manage libraries */
    private Librarian librarian = listenTo(newLibrarian());

    /** Root folder to build */
    private Folder rootFolder;

    /** The set of enabled build options */
    private final ObjectSet<BuildOption> enabledOptions = set();

    /** Libraries to compile with */
    private DependencyList dependencies = dependencyList();

    /** The settings for this builder */
    private BuilderSettings settings;

    /**
     * Creates a builder for the given build
     *
     * @param build The associated build for this builder
     */
    public Builder(Build build)
    {
        this.build = build;
    }

    /**
     * Creates a copy of the give builder
     *
     * @param that The builder to copy
     */
    protected Builder(Builder that)
    {
        this(that.associatedBuild());
        this.settings = that.settings;
        this.phases = that.phases.copy();
        this.librarian = that.librarian;
        this.rootFolder = that.rootFolder;
        this.artifactDescriptor = that.artifactDescriptor;
        this.dependencies = that.dependencies.copy();
    }

    /**
     * Returns the primary artifact descriptor for this build
     */
    @Override
    public ArtifactDescriptor artifactDescriptor()
    {
        return artifactDescriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Build associatedBuild()
    {
        return build;
    }

    @Override
    public Builder associatedBuilder()
    {
        return this;
    }

    /**
     * Builds with one thread for each processor
     *
     * @return True if the build succeeded without any problems
     */
    public final BuildResult build()
    {
        return build(settings.threads());
    }

    /**
     * Builds with the given number of worker threads
     *
     * @return True if the build succeeded without any problems
     */
    @SuppressWarnings("unchecked")
    public final BuildResult build(Count threads)
    {
        // Listen to any problems broadcast by the build,
        var issues = new MessageList(message -> !message.status().succeeded());
        issues.listenTo(this);

        // go through each phase in order,
        for (var phase : phases)
        {
            // and if the phase is enabled,
            if (phases.isEnabled(phase))
            {
                // notify that the phase has started,
                phase.internalOnBefore();

                // run the phase calling all listeners,
                if (isEnabled(DESCRIBE))
                {
                    announce(" \n$", bannerLine(phase.name()));
                }
                else
                {
                    announce(bannerLine(phase.name()));
                }
                phase.run();

                // notify that the phase has ended,
                phase.internalOnAfter();
            }
        }

        // then collect statistics and display them,
        var statistics = issues.statistics(Problem.class, Warning.class, Quibble.class);
        information(statistics.titledBox("Build Results"));

        // and return true if there are no problems (or worse).
        return new BuildResult(issues);
    }

    /**
     * Returns the child build at the given path
     *
     * @param path The path to the child build
     */
    public Builder childBuilder(String path)
    {
        return withRootFolder(rootFolder.folder(path))
            .withArtifactIdentifier(pathTail(path, '/'));
    }

    /**
     * Returns a copy of this builder
     */
    public Builder copy()
    {
        return new Builder(this);
    }

    /**
     * The libraries required by this build
     *
     * @return The libraries to compile against
     */
    @Override
    public DependencyList dependencies()
    {
        return dependencies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description()
    {
        var description = """
            Commands
                            
              command               description
              -----------           ---------------------------------------------
              describe              describe the build rather than running it
                            
            Phases (those preceded by a dash will be disabled)
                        
              phase                 description
              -----------           ---------------------------------------------
            """;

        for (var phase : phases)
        {
            description += String.format("  %-22s%s\n", phase.name(), phase.description());
        }

        return description;
    }

    /**
     * Disables execution of the given phase
     *
     * @param phase The phase to disable
     */
    public void disable(Phase phase)
    {
        phases.disable(phase);
    }

    @Override
    public Builder disable(BuildOption option)
    {
        enabledOptions.remove(option);
        return this;
    }

    /**
     * Enables execution of the given phase
     *
     * @param phase The phase to enables
     */
    public void enable(Phase phase)
    {
        phases.enable(phase);
    }

    @Override
    public Builder enable(BuildOption option)
    {
        enabledOptions.add(option);
        return this;
    }

    /**
     * Returns true if the given phase is enabled for execution
     *
     * @param phase The phase
     */
    public boolean isEnabled(Phase phase)
    {
        return phases.isEnabled(phase);
    }

    @Override
    public boolean isEnabled(BuildOption option)
    {
        return enabledOptions.contains(option);
    }

    /**
     * Returns the librarian for this build
     *
     * @return The librarian
     */
    public Librarian librarian()
    {
        return librarian;
    }

    /**
     * Parses the given command line, enabling and disabling relevant phases and build options
     *
     * @param commandLine The command line to process
     */
    public void parseCommandLine(CommandLine commandLine)
    {
        for (var argument : commandLine.argumentValues())
        {
            var value = argument.value();
            var enable = !value.startsWith("-");
            var option = tryCatch(() -> BuildOption.valueOf(value));

            if (option != null)
            {
                if (enable)
                {
                    enable(option);
                }
                else
                {
                    disable(option);
                }
            }
            else
            {
                if (enable)
                {
                    enable(phase(value));
                }
                else
                {
                    disable(phase(value));
                }
            }
        }
    }

    /**
     * Returns the phase with the given name
     *
     * @param name The phase name
     * @return The phase
     */
    public Phase phase(String name)
    {
        return phases.phase(name);
    }

    /**
     * Returns the list of phases in execution order for this build
     */
    public PhaseList phases()
    {
        return phases;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Builder pinVersion(Artifact<?> artifact, Version version)
    {
        librarian().pinVersion(artifact, version);
        return this;
    }

    /**
     * Adds the given artifact to this build's dependencies
     *
     * @param first The artifact to add
     * @param rest The rest of the artifacts to add
     */
    @Override
    public Builder requires(Artifact<?> first, Artifact<?>... rest)
    {
        dependencies = dependencies.withAdditionalDependencies(first, rest);
        return this;
    }

    /**
     * Adds the given dependencies to this build
     *
     * @param dependencies The dependencies to add
     */
    @Override
    public Builder requires(DependencyList dependencies)
    {
        this.dependencies = this.dependencies.withAdditionalDependencies(dependencies);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Folder rootFolder()
    {
        return rootFolder;
    }

    public Builder withAdditionalDependencies(Artifact<?> first, Artifact<?>... rest)
    {
        var copy = copy();
        copy.dependencies = dependencies.withAdditionalDependencies(first, rest);
        return copy;
    }

    public Builder withAdditionalDependencies(DependencyList dependencies)
    {
        var copy = copy();
        copy.dependencies = dependencies.withAdditionalDependencies(dependencies);
        return copy;
    }

    /**
     * Returns a copy of this build with the given main artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    @Override
    public Builder withArtifactDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copy();
        copy.artifactDescriptor = descriptor;
        return copy;
    }

    public Builder withDependencies(Artifact<?> first, Artifact<?>... rest)
    {
        var copy = copy();
        copy.dependencies = dependencies.withDependencies(first, rest);
        return copy;
    }

    public Builder withDependencies(DependencyList dependencies)
    {
        var copy = copy();
        copy.dependencies = dependencies.withDependencies(dependencies);
        return copy;
    }

    /**
     * Returns a copy of this builder with the given phases
     *
     * @param phases The list of phases this builder should execute
     * @return A copy of this builder with the given phases
     */
    public Builder withPhases(PhaseList phases)
    {
        var copy = copy();
        copy.phases = phases;
        return copy;
    }

    /**
     * Returns a copy of this builder with the given root folder
     *
     * @param rootFolder The new root folder for this builder
     * @return A copy of this builder with the given root folder
     */
    public Builder withRootFolder(Folder rootFolder)
    {
        var copy = copy();
        copy.rootFolder = rootFolder;
        return copy;
    }

    public Builder withSettings(BuilderSettings settings)
    {
        var copy = copy();
        copy.settings = settings;
        return copy;
    }
}
