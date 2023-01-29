package digital.fiasco.runtime.build.builder;

import com.telenav.kivakit.commandline.CommandLine;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.language.trait.TryCatchTrait;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
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
import digital.fiasco.runtime.dependency.artifact.collections.ArtifactList;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactGroup;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName;
import digital.fiasco.runtime.dependency.collections.DependencyList;
import digital.fiasco.runtime.repository.Repository;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.function.Result.result;
import static com.telenav.kivakit.core.string.AsciiArt.bannerLine;
import static com.telenav.kivakit.core.string.Paths.pathTail;
import static com.telenav.kivakit.core.version.Version.version;
import static digital.fiasco.runtime.build.settings.BuildOption.DESCRIBE;
import static digital.fiasco.runtime.build.settings.BuildOption.HELP;
import static digital.fiasco.runtime.build.settings.BuildOption.VERBOSE;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactGroup.group;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName.artifactName;

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
 * Phases are enabled and disabled with {@link #enable(Phase)} and {@link #disable(Phase)}. The {@link #parseCommandLine(CommandLine)}
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
 *     <li>{@link #disable(BuildOption)}</li>
 *     <li>{@link #enable(BuildOption)}</li>
 *     <li>{@link #isEnabled(BuildOption)}</li>
 * </ul>
 *
 * <p><b>Build Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies()}</li>
 *     <li>{@link #librarian()}</li>
 *     <li>{@link #pinVersion(Artifact, String)}</li>
 *     <li>{@link #pinVersion(Artifact, Version)}</li>
 *     <li>{@link #requires(Artifact, Artifact[])}</li>
 *     <li>{@link #requires(ArtifactList)}</li>
 * </ul>
 *
 * <p><b>Build Phases</b></p>
 *
 * <ul>
 *     <li>{@link #afterPhase(String, BuildAction)}</li>
 *     <li>{@link #beforePhase(String, BuildAction)}</li>
 *     <li>{@link #disable(Phase)}</li>
 *     <li>{@link #enable(Phase)}</li>
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
 *     <li>{@link #enable(BuildProfile)}</li>
 *     <li>{@link #isEnabled(BuildProfile)}</li>
 *     <li>{@link #enabledProfiles()}</li>
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

    /**
     * The dependencies that must be resolved before this builder can run. Dependencies can include both artifacts and
     * other builders.
     */
    private DependencyList<?, ?> dependencies;

    /**
     * Creates a builder for the given build
     *
     * @param build The associated build for this builder
     */
    public Builder(Build build)
    {
        this.build = build;
        this.dependencies = DependencyList.dependencies();
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
        this.dependencies = that.dependencies;
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
                if (settings.isEnabled(phase))
                {
                    // notify that the phase has started,
                    phase.internalOnBefore(this);

                    // run the phase calling all listeners,
                    if (isEnabled(DESCRIBE))
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
    public DependencyList<?, ?> dependencies()
    {
        return settings.dependencies();
    }

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    public Builder dependsOn(Dependency... dependencies)
    {
        return withDependencies(DependencyList.dependencies(dependencies));
    }

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    public <D extends Dependency, L extends DependencyList<D, L>> Builder dependsOn(L dependencies)
    {
        return withDependencies(dependencies);
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
        return withRootFolder(rootFolder().folder(path))
            .withArtifactName(pathTail(path, '/'));
    }

    /**
     * {@inheritDoc}
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

        for (var phase : settings.phases())
        {
            description.add(String.format("  %-22s%s\n", phase.name(), phase.description()));
        }

        return description.titledBox("Fiasco Help");
    }

    /**
     * Returns the artifact descriptor for this builder
     */
    @Override
    public ArtifactDescriptor descriptor()
    {
        return settings.descriptor();
    }

    /**
     * Disables execution of the given phase
     *
     * @param phase The phase to disable
     */
    public void disable(Phase phase)
    {
        settings = settings.withDisabled(phase);
    }

    /**
     * Disables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    public Builder disable(BuildOption option)
    {
        settings = settings.withDisabled(option);
        return this;
    }

    /**
     * Disables the given build profile
     *
     * @param profile The profile
     * @return This build for method chaining
     */
    public Builder disable(BuildProfile profile)
    {
        settings = settings.withDisabled(profile);
        return this;
    }

    /**
     * Enables execution of the given phase
     *
     * @param phase The phase to enables
     */
    public void enable(Phase phase)
    {
        settings = settings.withEnabled(phase);
    }

    /**
     * Enables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    public Builder enable(BuildOption option)
    {
        settings = settings.withEnabled(option);
        return this;
    }

    /**
     * Enables the given build profile
     *
     * @param profile The profile
     * @return This build for method chaining
     */
    public Builder enable(BuildProfile profile)
    {
        settings.withEnabled(profile);
        return this;
    }

    /**
     * Returns the set of enabled build profiles
     */
    public ObjectSet<BuildProfile> enabledProfiles()
    {
        return settings.profiles();
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
     * Returns true if the given profile is enabled
     *
     * @param profile The profile
     * @return True if the profile is enabled
     */
    public boolean isEnabled(BuildProfile profile)
    {
        return settings.isEnabled(profile);
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
                    settings.withEnabled(option);
                }
                else
                {
                    settings.withDisabled(option);
                }
            }
            else
            {
                var phase = phase(value);
                if (phase != null)
                {
                    if (enable)
                    {
                        settings.withEnabled(phase(value));
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
        settings.withPinnedVersion(artifact, version(version));
        return this;
    }

    /**
     * Globally pins all versions of the given artifact to the specified version
     *
     * @param artifact The artifact
     * @param version The version to use
     * @return The build for method chaining
     */
    public Builder pinVersion(Artifact<?> artifact, Version version)
    {
        settings.withPinnedVersion(artifact, version);
        return this;
    }

    /**
     * There is no repository for a {@link Builder} dependency
     *
     * @return Always null
     */
    @Override
    public Repository repository()
    {
        return null;
    }

    /**
     * Adds one or more build dependencies to this builder
     *
     * @param first The first dependency
     * @param rest Any further dependencies
     * @return The build for method chaining
     */
    public Builder requires(Artifact<?> first, Artifact<?>... rest)
    {
        settings = settings.withDependencies(first, rest);
        return this;
    }

    /**
     * Adds the given dependencies to this builder
     *
     * @param dependencies The dependencies to add
     */
    public Builder requires(ArtifactList dependencies)
    {
        settings = settings.withDependencies(dependencies);
        return this;
    }

    @Override
    public Folder rootFolder()
    {
        return settings.rootFolder();
    }

    /**
     * Returns the settings for this builder
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
     * Returns the number of threads used to build
     */
    public Count threads()
    {
        return settings.builderThreads();
    }

    /**
     * Returns a copy of this builder with the given main artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    public Builder withArtifactDescriptor(String descriptor)
    {
        var copy = copy();
        copy.settings = settings.withArtifactDescriptor(ArtifactDescriptor.descriptor(descriptor));
        return copy;
    }

    /**
     * Returns a copy of this builder with the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    public Builder withArtifactDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copy();
        copy.settings = settings.withArtifactDescriptor(descriptor);
        return copy;
    }

    /**
     * Returns a copy of this builder with the given artifact group
     *
     * @param group The artifact group
     * @return The copy
     */
    public Builder withArtifactGroup(ArtifactGroup group)
    {
        return withArtifactDescriptor(descriptor().withGroup(group));
    }

    /**
     * Returns a copy of this builder with the given artifact group
     *
     * @param group The artifact group
     * @return The copy
     */
    public Builder withArtifactGroup(String group)
    {
        return withArtifactGroup(group(group));
    }

    /**
     * Returns a copy of this builder with the given artifact name
     *
     * @param artifact The artifact name
     * @return The copy
     */
    public Builder withArtifactName(ArtifactName artifact)
    {
        return withArtifactDescriptor(descriptor().withArtifact(artifact));
    }

    /**
     * Returns a copy of this builder with the given artifact name
     *
     * @param artifact The artifact name
     * @return The copy
     */
    public Builder withArtifactName(String artifact)
    {
        return withArtifactName(artifactName(artifact));
    }

    /**
     * Returns a copy of this builder with the given thread count
     *
     * @param threads The number of threads this builder should use when building
     * @return A copy of this builder with the given thread count
     */
    public Builder withArtifactResolverThreads(Count threads)
    {
        return withSettings(settings.withArtifactResolverThreads(threads));
    }

    /**
     * Returns a copy of this builder with the given artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    public Builder withArtifactVersion(String version)
    {
        return withArtifactVersion(version(version));
    }

    /**
     * Returns a copy of this builder with the given artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    public Builder withArtifactVersion(Version version)
    {
        return withArtifactDescriptor(descriptor().withVersion(version));
    }

    /**
     * Returns a copy of this builder with the given thread count
     *
     * @param threads The number of threads this builder should use when building
     * @return A copy of this builder with the given thread count
     */
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
    public Builder withDependencies(DependencyList<?, ?> dependencies)
    {
        var copy = copy();
        copy.dependencies = dependencies.copy();
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
        var copy = copy();
        copy.settings = settings.withRootFolder(rootFolder);
        return copy;
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
}
