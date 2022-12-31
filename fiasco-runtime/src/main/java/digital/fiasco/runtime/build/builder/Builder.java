package digital.fiasco.runtime.build.builder;

import com.telenav.kivakit.commandline.CommandLine;
import com.telenav.kivakit.core.language.trait.TryCatchTrait;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.string.Described;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.BuildEnvironment;
import digital.fiasco.runtime.build.BuildStructured;
import digital.fiasco.runtime.build.builder.phases.BasePhase;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.builder.tools.ToolFactory;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.build.BuildOption;
import digital.fiasco.runtime.build.BuildSettings;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.string.AsciiArt.bannerLine;
import static com.telenav.kivakit.core.string.Paths.pathTail;
import static com.telenav.kivakit.core.version.Version.version;
import static digital.fiasco.runtime.build.BuildOption.DESCRIBE;

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
 *     <li>{@link #build()}</li>
 *     <li>{@link #build(Count)}</li>
 *     <li>{@link #disable(BuildOption)}</li>
 *     <li>{@link #enable(BuildOption)}</li>
 *     <li>{@link #isEnabled(BuildOption)}</li>
 *     <li>{@link #rootFolder()}</li>
 *     <li>{@link #withRootFolder(Folder)}</li>
 *     <li>{@link #withSettings(BuildSettings)}</li>
 *     <li>{@link #withPhases(PhaseList)}</li>
 *     <li>{@link #withThreads(Count)}</li>
 * </ul>
 *
 * <p><b>Artifact</b></p>
 *
 * <ul>
 *     <li>{@link #targetArtifactDescriptor()}</li>
 *     <li>{@link #targetArtifactName()}</li>
 *     <li>{@link #targetArtifactVersion()}</li>
 *     <li>{@link #withTargetArtifactIdentifier(String)}</li>
 *     <li>{@link #withTargetArtifactDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withTargetArtifactDescriptor(String)}</li>
 *     <li>{@link #withTargetArtifactVersion(String)}</li>
 *     <li>{@link #withTargetArtifactVersion(Version)}</li>
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
    BuildStructured,
    BuilderAssociated,
    BuildEnvironment,
    ToolFactory,
    TryCatchTrait
{
    /** The associated build */
    private final Build build;

    /** The settings for this builder */
    private BuildSettings settings;

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
        this(that.build);
        this.settings = that.settings;
    }

    /**
     * Runs the given code after the named phase runs
     *
     * @param name The phase
     * @param code The code to run
     * @return This builder, for chaining
     */
    public Builder afterPhase(String name, Runnable code)
    {
        phase(name).afterPhase(code);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Builder associatedBuilder()
    {
        return this;
    }

    /**
     * Runs the given code before the named phase runs
     *
     * @param name The phase
     * @param code The code to run
     * @return This builder, for chaining
     */
    public Builder beforePhase(String name, Runnable code)
    {
        phase(name).beforePhase(code);
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
    public final BuildResult build(Count threads)
    {
        // Listen to any problems broadcast by the build,
        var issues = new MessageList(message -> !message.status().succeeded());
        issues.listenTo(this);

        // go through each phase in order,
        for (var phase : settings.phases())
        {
            // and if the phase is enabled,
            if (settings.isEnabled(phase))
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

        // and return the result of the build.
        return new BuildResult(issues);
    }

    /**
     * Returns the child build at the given path
     *
     * @param path The path to the child build
     */
    public Builder childBuilder(String path)
    {
        return withRootFolder(rootFolder().folder(path))
            .withTargetArtifactIdentifier(pathTail(path, '/'));
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
    public DependencyList dependencies()
    {
        return settings.dependencies();
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

        for (var phase : settings.phases())
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
        settings.disable(phase);
    }

    /**
     * Disables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    public Builder disable(BuildOption option)
    {
        settings.disable(option);
        return this;
    }

    /**
     * Enables execution of the given phase
     *
     * @param phase The phase to enables
     */
    public void enable(Phase phase)
    {
        settings.enable(phase);
    }

    /**
     * Enables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    public Builder enable(BuildOption option)
    {
        settings.enable(option);
        return this;
    }

    /**
     * Returns true if the given phase is enabled for execution
     *
     * @param phase The phase
     */
    public boolean isEnabled(Phase phase)
    {
        return settings.isEnabled(phase);
    }

    /**
     * Returns true if the given option is enabled
     *
     * @param option The option
     * @return True if the option is enabled
     */
    public boolean isEnabled(BuildOption option)
    {
        return settings.isEnabled(option);
    }

    /**
     * Returns the librarian for this builder
     *
     * @return The librarian
     */
    public Librarian librarian()
    {
        return settings.librarian();
    }

    /**
     * Runs the given code during the named phase
     *
     * @param name The phase
     * @param code The code to run
     * @return This builder, for chaining
     */
    public Builder onPhase(String name, Runnable code)
    {
        phase(name).onPhase(code);
        return this;
    }

    /**
     * Parses the given command line, enabling and disabling relevant phases and build options
     *
     * @param commandLine The command line to process
     */
    public Builder parseCommandLine(CommandLine commandLine)
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
                    settings.enable(option);
                }
                else
                {
                    settings.disable(option);
                }
            }
            else
            {
                if (enable)
                {
                    settings.enable(phase(value));
                }
                else
                {
                    settings.disable(phase(value));
                }
            }
        }
        return this;
    }

    /**
     * Returns the phase with the given name
     *
     * @param name The phase name
     * @return The phase
     */
    public Phase phase(String name)
    {
        return settings.phase(name);
    }

    /**
     * Returns the list of phases in execution order for this build
     */
    public PhaseList phases()
    {
        return settings.phases();
    }

    /**
     * Globally pins all versions of the given artifact to the specified version
     *
     * @param artifact The artifact
     * @param version The version to use
     * @return The build for method chaining
     */
    public Builder pinVersion(Artifact<?> artifact, String version)
    {
        settings = settings.pinVersion(artifact, version(version));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Builder pinVersion(Artifact<?> artifact, Version version)
    {
        settings = settings.pinVersion(artifact, version);
        return this;
    }

    /**
     * Adds one or more build dependencies
     *
     * @param first The first dependency
     * @param rest Any further dependencies
     * @return The build for method chaining
     */
    public Builder requires(Artifact<?> first, Artifact<?>... rest)
    {
        settings = settings.requires(first, rest);
        return this;
    }

    /**
     * Adds the given dependencies to this build
     *
     * @param dependencies The dependencies to add
     */
    public Builder requires(DependencyList dependencies)
    {
        settings = settings.withAdditionalDependencies(dependencies);
        return this;
    }

    @Override
    public Folder rootFolder()
    {
        return settings.rootFolder();
    }

    /**
     * Returns the artifact descriptor for this build
     */
    public ArtifactDescriptor targetArtifactDescriptor()
    {
        return settings.targetArtifactDescriptor();
    }

    /**
     * Returns the name of this artifact
     *
     * @return The artifact name
     */
    @NotNull
    public String targetArtifactName()
    {
        return settings.targetArtifactName();
    }

    /**
     * Returns the artifact descriptor for this build
     */
    public Version targetArtifactVersion()
    {
        return targetArtifactDescriptor().version();
    }

    public Builder withAdditionalDependencies(DependencyList dependencies)
    {
        settings = settings.withAdditionalDependencies(dependencies);
        return this;
    }

    public Builder withAdditionalDependencies(Artifact<?> first, Artifact<?>... rest)
    {
        settings = settings.withAdditionalDependencies(first, rest);
        return this;
    }

    public Builder withDependencies(Artifact<?> first, Artifact<?>... rest)
    {
        settings = settings.withDependencies(first, rest);
        return this;
    }

    public Builder withDependencies(DependencyList dependencies)
    {
        settings = settings.withDependencies(dependencies);
        return this;
    }

    /**
     * Returns a copy of this builder with the given phases
     *
     * @param phases The list of phases this builder should execute
     * @return A copy of this builder with the given phases
     */
    public Builder withPhases(PhaseList phases)
    {
        return withSettings(settings.withPhases(phases));
    }

    /**
     * Returns a copy of this settings object with the given root folder
     *
     * @param rootFolder The root folder
     * @return The copy of this settings object
     */
    public Builder withRootFolder(Folder rootFolder)
    {
        settings = settings.withRootFolder(rootFolder);
        return this;
    }

    /**
     * Returns a copy of this builder with the given settings
     *
     * @param settings The settings
     * @return The new builder
     */
    public Builder withSettings(BuildSettings settings)
    {
        var copy = copy();
        copy.settings = settings;
        return copy;
    }

    /**
     * Returns a copy of this build with the given main artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    public Builder withTargetArtifactDescriptor(String descriptor)
    {
        settings = settings.withTargetArtifactDescriptor(ArtifactDescriptor.artifactDescriptor(descriptor));
        return this;
    }

    /**
     * Returns a copy of this build with the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    public Builder withTargetArtifactDescriptor(ArtifactDescriptor descriptor)
    {
        settings = settings.withTargetArtifactDescriptor(descriptor);
        return this;
    }

    /**
     * Returns a copy of this build with the given artifact identifier
     *
     * @param identifier The artifact identifier
     * @return The copy
     */
    public Builder withTargetArtifactIdentifier(String identifier)
    {
        return withTargetArtifactDescriptor(targetArtifactDescriptor().withIdentifier(identifier));
    }

    /**
     * Returns a copy of this build with the given main artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    public Builder withTargetArtifactVersion(String version)
    {
        return withTargetArtifactVersion(version(version));
    }

    /**
     * Returns a copy of this build with the given main artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    public Builder withTargetArtifactVersion(Version version)
    {
        return withTargetArtifactDescriptor(targetArtifactDescriptor().withVersion(version));
    }

    /**
     * Returns a copy of this builder with the given thread count
     *
     * @param threads The number of threads this builder should use when building
     * @return A copy of this builder with the given thread count
     */
    public Builder withThreads(Count threads)
    {
        return withSettings(settings.withThreads(threads));
    }
}
