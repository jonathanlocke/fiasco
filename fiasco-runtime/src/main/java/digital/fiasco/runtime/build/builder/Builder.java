package digital.fiasco.runtime.build.builder;

import com.telenav.kivakit.commandline.CommandLine;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.language.trait.TryCatchTrait;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.object.Copyable;
import com.telenav.kivakit.interfaces.string.Described;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.BuildEnvironmentTrait;
import digital.fiasco.runtime.build.Stepped;
import digital.fiasco.runtime.build.builder.phases.BasePhase;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.builder.tools.Tool;
import digital.fiasco.runtime.build.builder.tools.ToolFactory;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.build.settings.BuildOption;
import digital.fiasco.runtime.build.settings.BuildProfile;
import digital.fiasco.runtime.build.settings.BuildSettings;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactGroup;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName;
import digital.fiasco.runtime.dependency.collections.ArtifactList;
import digital.fiasco.runtime.dependency.collections.BuilderList;
import digital.fiasco.runtime.repository.Repository;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.function.Result.result;
import static com.telenav.kivakit.core.string.AsciiArt.bannerLine;
import static com.telenav.kivakit.core.string.Paths.pathTail;
import static com.telenav.kivakit.core.version.Version.version;
import static digital.fiasco.runtime.build.settings.BuildOption.DESCRIBE;
import static digital.fiasco.runtime.build.settings.BuildOption.HELP;
import static digital.fiasco.runtime.build.settings.BuildOption.VERBOSE;

/**
 * <p>
 * Builds a project by executing a series of {@link Phase}s in a defined order. A {@link Build} may use multiple
 * {@link Builder}s, to perform a multi-project build or to achieve different kinds of builds.
 * </p>
 *
 * <p><b>Build Phases</b></p>
 *
 * <p>
 * By default, phases are executed in this order:
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
 * <p>
 * Each phase is defined by a class that implements {@link Phase} (and normally extends {@link BasePhase}). Additional
 * phases can be inserted into the mutable {@link #phases()} list for this builder with
 * {@link PhaseList#addPhaseAfter(String, Phase)} and {@link PhaseList#addPhaseBefore(String, Phase)}. A specific phase
 * can be replaced with a new definition with {@link PhaseList#replacePhase(String, Phase)}. Alternatively, an entirely
 * new phase list can be installed with {@link #withPhases(PhaseList)}.
 * </p>
 *
 * <p><b>Attaching Build Actions to Phases</b></p>
 *
 * <p>
 * A phase can be retrieved by name with {@link #phase(String)} and an arbitrary {@link Runnable}
 * can be attached to run before, during or after the phase executes with {@link #beforePhase(String, BuildAction)},
 * {@link #onPhase(String, BuildAction)}, and {@link #afterPhase(String, BuildAction)}. This allows {@link Tool}s to
 * be used at the right time to achieve different build effects.
 * </p>
 *
 * <p>
 * Phases are enabled and disabled with {@link #withEnabled(Phase)} and {@link #withDisabled(Phase)}. The {@link #parseCommandLine(CommandLine)}
 * method enables and disables any phases or build options that were passed in the command line. The phase name itself
 * enables the phase and any dependent phases (for example, "compile" enables the "start", "prepare" and "compile" phases).
 * If the phase name is preceded by a dash (for example, -test), the phase is disabled (but not its dependent phases).
 * </p>
 *
 * <p><b>Building</b></p>
 *
 * <ul>
 *     <li>{@link #build()}</li>
 *     <li>{@link #deriveBuilder(String)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #description()}</li>
 *     <li>{@link #phases()}</li>
 *     <li>{@link #rootFolder()}</li>
 *     <li>{@link #settings()}</li>
 *     <li>{@link #threads()}</li>
 *     <li>{@link #withPhases(PhaseList)}</li>
 *     <li>{@link #withRootFolder(Folder)}</li>
 *     <li>{@link #withSettings(BuildSettings)}</li>
 *     <li>{@link #withBuilderThreads(Count)}</li>
 * </ul>
 *
 * <p><b>Build Options</b></p>
 *
 * <ul>
 *     <li>{@link #withDisabled(BuildOption)}</li>
 *     <li>{@link #withEnabled(BuildOption)}</li>
 *     <li>{@link #isEnabled(BuildOption)}</li>
 * </ul>
 *
 * <p><b>Build Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #artifactDependencies()}</li>
 *     <li>{@link #builderDependencies()}</li>
 *     <li>{@link #librarian()}</li>
 *     <li>{@link #withPinnedVersion(Artifact, String)}</li>
 *     <li>{@link #withPinnedVersion(Artifact, Version)}</li>
 *     <li>{@link #withDependencies(Artifact, Artifact[])}</li>
 *     <li>{@link #withDependencies(ArtifactList)}</li>
 * </ul>
 *
 * <p><b>Build Phases</b></p>
 *
 * <ul>
 *     <li>{@link #afterPhase(String, BuildAction)}</li>
 *     <li>{@link #beforePhase(String, BuildAction)}</li>
 *     <li>{@link #withDisabled(Phase)}</li>
 *     <li>{@link #withEnabled(Phase)}</li>
 *     <li>{@link #isEnabled(Phase)}</li>
 *     <li>{@link #onPhase(String, BuildAction)}</li>
 *     <li>{@link #phase(String)}</li>
 *     <li>{@link #phases()}</li>
 *     <li>{@link #withPhases(PhaseList)}</li>
 * </ul>
 *
 * <p><b>Build Actions</b></p>
 *
 * <ul>
 *     <li>{@link #beforePhase(String, BuildAction)}</li>
 *     <li>{@link #onPhase(String, BuildAction)}</li>
 *     <li>{@link #afterPhase(String, BuildAction)}</li>
 * </ul>
 *
 * <p><b>Build Profiles</b></p>
 *
 * <ul>
 *     <li>{@link #withEnabled(BuildProfile)}</li>
 *     <li>{@link #isEnabled(BuildProfile)}</li>
 *     <li>{@link #profiles()}</li>
 * </ul>
 *
 * <p><b>Artifact Descriptors</b></p>
 *
 * <ul>
 *     <li>{@link #descriptor()}</li>
 *     <li>{@link #withArtifactDescriptor(String)}</li>
 *     <li>{@link #withArtifactDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withArtifactGroup(String)}</li>
 *     <li>{@link #withArtifactGroup(ArtifactGroup)}</li>
 *     <li>{@link #withArtifactName(String)}</li>
 *     <li>{@link #withArtifactName(ArtifactName)}</li>
 *     <li>{@link #withArtifactVersion(String)}</li>
 *     <li>{@link #withArtifactVersion(Version)}</li>
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
 * <p><b>Build Tools</b></p>
 *
 * <ul>
 *     <li>{@link #newArchiver()}</li>
 *     <li>{@link #newCleaner()}</li>
 *     <li>{@link #newCompiler()}</li>
 *     <li>{@link #newCopier()}</li>
 *     <li>{@link #newGit()}</li>
 *     <li>{@link #newLibrarian()}</li>
 *     <li>{@link #newShader()}</li>
 *     <li>{@link #newStamper()}</li>
 *     <li>{@link #newTester()}</li>
 *     <li>{@link #newTool(Class)}</li>
 * </ul>
 *
 * <p><b>Miscellaneous</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #parseCommandLine(CommandLine)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class Builder extends BaseRepeater implements
    Described,
    Stepped,
    Copyable<Builder>,
    BuildSettings,
    BuildStructured,
    BuilderAssociated,
    BuildEnvironmentTrait,
    ToolFactory,
    TryCatchTrait,
    Dependency
{
    /** The associated build */
    private final Build build;

    /** The settings for this builder */
    private BuildSettings settings;

    /** The librarian to resolve dependencies for this builder */
    private Librarian librarian;

    /**
     * The dependencies that must be resolved before this builder can run.
     */
    private ArtifactList artifactDependencies;

    /**
     * The builders that this artifact is dependent on
     */
    private BuilderList builderDependencies;

    /**
     * Creates a builder for the given build
     *
     * @param build The associated build for this builder
     */
    public Builder(Build build)
    {
        this.build = build;
        artifactDependencies = ArtifactList.artifacts();
        builderDependencies = BuilderList.builders();
    }

    /**
     * Creates a copy of the give builder
     *
     * @param that The builder to copy
     */
    protected Builder(Builder that)
    {
        this(that.build);
        this.librarian = that.librarian;
        this.settings = that.settings;
        this.artifactDependencies = that.artifactDependencies;
        this.builderDependencies = that.builderDependencies;
    }

    /**
     * Runs the given code after the named phase runs
     *
     * @param name The phase
     * @param code The code to run
     * @return This builder, for chaining
     */
    public Builder afterPhase(String name, BuildAction code)
    {
        phase(name).afterPhase(code);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public ArtifactList artifactDependencies()
    {
        return artifactDependencies;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Count artifactResolverThreads()
    {
        return settings().artifactResolverThreads();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Builder associatedBuilder()
    {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param that {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettings attachMixin(BuildSettings that)
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @param code {@inheritDoc}
     * @return {@inheritDoc}
     */
    public Builder beforePhase(String name, BuildAction code)
    {
        phase(name).beforePhase(code);
        return this;
    }

    /**
     * Runs this builder, returning a result. The result contains any captured messages, and a reference to the builder
     * to signify which builder the result is for.
     *
     * @return Returns the build result
     */
    public final Result<Builder> build()
    {
        return result(() ->
        {
            // go through each phase in order,
            for (var phase : settings.phases())
            {
                // and if the phase is enabled,
                if (isEnabled(phase))
                {
                    // notify that the phase has started,
                    phase.internalOnBefore(this);

                    // run the phase calling all listeners,
                    if (shouldDescribe())
                    {
                        announce(" \n$", bannerLine(phase.name()));
                    }
                    else
                    {
                        announce(bannerLine(phase.name()));
                    }
                    phase.internalOnRun(this);

                    // notify that the phase has ended,
                    phase.internalOnAfter(this);
                }
            }
            return this;
        });
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public BuilderList builderDependencies()
    {
        return builderDependencies;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Count builderThreads()
    {
        return settings.builderThreads();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Builder copy()
    {
        return new Builder(this);
    }

    /**
     * Returns a child builder at the given path with an empty dependency list. If the artifact descriptor for a builder
     * "x" is "apache.stuff:double-stuff:1.5.0", then x.childBuilder("stuff-utilities") will return a new builder with
     * the artifact descriptor "apache.stuff:stuff-utilities:1.5.0". The root folder will be
     * [root-folder]/stuff-utilities and there will be no dependencies.
     *
     * @param path The path to the child build
     */
    public Builder deriveBuilder(String path)
    {
        return withSettings(withRootFolder(rootFolder().folder(path))
            .withArtifactName(pathTail(path, '/')));
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String description()
    {
        var description = stringList();
        description.add("""
            Commands
                            
              command               description
              -----------           ---------------------------------------------""");

        for (var option : BuildOption.values())
        {
            description.add(String.format("  %-22s%s\n", option.name().toLowerCase(), option.help()));
        }

        description.add("""
                    
            Phases (those preceded by a dash will be disabled)
                        
              phase                 description
              -----------           ---------------------------------------------
            """);

        for (var phase : phases())
        {
            description.add(String.format("  %-22s%s\n", phase.name(), phase.description()));
        }

        return description.titledBox("Fiasco Help");
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public ArtifactDescriptor descriptor()
    {
        return settings.descriptor();
    }

    /**
     * {@inheritDoc}
     *
     * @param phase {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isEnabled(Phase phase)
    {
        return settings.isEnabled(phase);
    }

    /**
     * {@inheritDoc}
     *
     * @param option {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isEnabled(BuildOption option)
    {
        return settings.isEnabled(option);
    }

    /**
     * {@inheritDoc}
     *
     * @param profile {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isEnabled(BuildProfile profile)
    {
        return settings.isEnabled(profile);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public Librarian librarian()
    {
        return librarian;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String name()
    {
        return descriptor().name();
    }

    /**
     * Runs the given code during the named phase
     *
     * @param name The phase
     * @param code The code to run
     * @return This builder, for chaining
     */
    public Builder onPhase(String name, BuildAction code)
    {
        phase(name).duringPhase(code);
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
                    return withEnabled(option);
                }
                else
                {
                    return withDisabled(option);
                }
            }
            else
            {
                var phase = phase(value);
                if (phase != null)
                {
                    if (enable)
                    {
                        return withEnabled(phase(value));
                    }
                    else
                    {
                        settings.withDisabled(phase(value));
                    }
                }
                else
                {
                    var profile = new BuildProfile(value);
                    if (enable)
                    {
                        settings.withEnabled(profile);
                    }
                    else
                    {
                        settings.withDisabled(profile);
                    }
                }
            }
        }

        if (settings.isEnabled(HELP))
        {
            information(description());
        }

        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Phase phase(String name)
    {
        return settings.phase(name);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public PhaseList phases()
    {
        return settings.phases();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public ObjectSet<BuildProfile> profiles()
    {
        return settings.profiles();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Repository repository()
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Folder rootFolder()
    {
        return settings.rootFolder();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public BuildSettings settings()
    {
        return settings;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean shouldDescribe()
    {
        return isEnabled(DESCRIBE);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean shouldDescribeAndExecute()
    {
        return isEnabled(VERBOSE);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public Count threads()
    {
        return settings.builderThreads();
    }

    /**
     * {@inheritDoc}
     *
     * @param descriptor {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withArtifactDescriptor(ArtifactDescriptor descriptor)
    {
        return withSettings(settings.withArtifactDescriptor(descriptor));
    }

    /**
     * Returns a copy of this builder with the given thread count
     *
     * @param threads The number of threads this builder should use when building
     * @return A copy of this builder with the given thread count
     */
    @Override
    public Builder withArtifactResolverThreads(Count threads)
    {
        return withSettings(settings.withArtifactResolverThreads(threads));
    }

    /**
     * Returns a copy of this builder with the given thread count
     *
     * @param threads The number of threads this builder should use when building
     * @return A copy of this builder with the given thread count
     */
    @Override
    public Builder withBuilderThreads(Count threads)
    {
        return withSettings(settings.withBuilderThreads(threads));
    }

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    public Builder withDependencies(Builder... dependencies)
    {
        return copy(it -> it.builderDependencies = builderDependencies.with(dependencies));
    }

    public Builder withDependencies(ArtifactList dependencies)
    {
        return copy(it -> it.artifactDependencies = artifactDependencies.with(dependencies));
    }

    /**
     * Adds one or more artifact dependencies to this builder
     *
     * @param first The first dependency
     * @param rest Any further dependencies
     * @return The build for method chaining
     */
    public Builder withDependencies(Artifact<?> first, Artifact<?>... rest)
    {
        return copy(it -> it.artifactDependencies = artifactDependencies.with(first, rest));
    }

    /**
     * Disables execution of the given phase
     *
     * @param phase The phase to disable
     */
    @Override
    public Builder withDisabled(Phase phase)
    {
        return withSettings(settings.withDisabled(phase));
    }

    /**
     * Disables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    @Override
    public Builder withDisabled(BuildOption option)
    {
        return withSettings(settings.withDisabled(option));
    }

    /**
     * Disables the given build profile
     *
     * @param profile The profile
     * @return This build for method chaining
     */
    @Override
    public Builder withDisabled(BuildProfile profile)
    {
        return withSettings(settings.withDisabled(profile));
    }

    /**
     * Enables execution of the given phase
     *
     * @param phase The phase to enables
     */
    @Override
    public Builder withEnabled(Phase phase)
    {
        return withSettings(settings.withEnabled(phase));
    }

    /**
     * Enables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    @Override
    public Builder withEnabled(BuildOption option)
    {
        return withSettings(settings.withEnabled(option));
    }

    /**
     * Enables the given build profile
     *
     * @param profile The profile
     * @return This build for method chaining
     */
    @Override
    public Builder withEnabled(BuildProfile profile)
    {
        return withSettings(settings.withEnabled(profile));
    }

    public Builder withLibrarian(Librarian librarian)
    {
        return copy(it -> it.librarian = librarian);
    }

    /**
     * Returns a copy of this builder with the given phases
     *
     * @param phases The list of phases this builder should execute
     * @return A copy of this builder with the given phases
     */
    @Override
    public Builder withPhases(PhaseList phases)
    {
        return withSettings(settings.withPhases(phases));
    }

    /**
     * {@inheritDoc}
     *
     * @param artifact {@inheritDoc}
     * @param version {@inheritDoc}
     * @return {@inheritDoc}
     */
    public Builder withPinnedVersion(Artifact<?> artifact, String version)
    {
        return withPinnedVersion(artifact, version(version));
    }

    /**
     * {@inheritDoc}
     *
     * @param artifact {@inheritDoc}
     * @param version {@inheritDoc}
     * @return {@inheritDoc}
     */
    public Builder withPinnedVersion(Artifact<?> artifact, Version version)
    {
        return copy(it -> it.librarian = it.librarian.withPinnedVersion(artifact, version));
    }

    /**
     * {@inheritDoc}
     *
     * @param rootFolder {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withRootFolder(Folder rootFolder)
    {
        return withSettings(settings.withRootFolder(rootFolder));
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public Builder withSettings(BuildSettings settings)
    {
        return copy(it -> it.settings = settings);
    }
}
