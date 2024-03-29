package digital.fiasco.runtime.build.builder;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.commandline.CommandLine;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.language.trait.TryCatchTrait;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.data.formats.yaml.model.YamlNode;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.object.Copyable;
import com.telenav.kivakit.interfaces.string.Described;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.builder.phases.BasePhase;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.builder.tools.Tool;
import digital.fiasco.runtime.build.builder.tools.ToolFactory;
import digital.fiasco.runtime.build.environment.BuildEnvironmentTrait;
import digital.fiasco.runtime.build.environment.BuildStructure;
import digital.fiasco.runtime.build.execution.BuildExecutionStep;
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
import digital.fiasco.runtime.librarian.Librarian;
import digital.fiasco.runtime.librarian.RepositorySearchLibrarian;
import digital.fiasco.runtime.repository.Repository;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.function.Result.result;
import static com.telenav.kivakit.core.string.AsciiArt.bannerLine;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.string.Paths.pathOptionalSuffix;
import static com.telenav.kivakit.core.version.Version.version;
import static com.telenav.kivakit.interfaces.comparison.Matcher.matchAll;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_TYPE;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_CLEAN;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_COMPILE;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_PREPARE;
import static digital.fiasco.runtime.build.settings.BuildOption.VERBOSE;
import static digital.fiasco.runtime.build.settings.BuildSettings.buildSettings;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptorList.descriptors;
import static digital.fiasco.runtime.dependency.collections.ArtifactList.artifacts;
import static digital.fiasco.runtime.dependency.collections.BuilderList.builders;

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
 * can be attached to run before, during or after the phase executes with {@link #withActionBeforePhase(Phase, BuildAction)},
 * {@link #withActionDuringPhase(Phase, BuildAction)}, and {@link #withActionAfterPhase(Phase, BuildAction)}. This allows {@link Tool}s to
 * be used at the right time to achieve different build effects.
 * </p>
 *
 * <p>
 * Phases are enabled and disabled with {@link #withEnabled(Phase)} and {@link #withDisabled(Phase)}. The {@link #withParsedCommandLine(CommandLine)}
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
 *     <li>{@link #dependencies()}</li>
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
 *     <li>{@link #withActionBeforePhase(Phase, BuildAction)}</li>
 *     <li>{@link #withActionDuringPhase(Phase, BuildAction)}</li>
 *     <li>{@link #withActionAfterPhase(Phase, BuildAction)}</li>
 *     <li>{@link #withDisabled(Phase)}</li>
 *     <li>{@link #withEnabled(Phase)}</li>
 *     <li>{@link #isEnabled(Phase)}</li>
 *     <li>{@link #phase(String)}</li>
 *     <li>{@link #phases()}</li>
 *     <li>{@link #withPhases(PhaseList)}</li>
 * </ul>
 *
 * <p><b>Build Actions</b></p>
 *
 * <ul>
 *     <li>{@link #withActionBeforePhase(Phase, BuildAction)}</li>
 *     <li>{@link #withActionDuringPhase(Phase, BuildAction)}</li>
 *     <li>{@link #withActionAfterPhase(Phase, BuildAction)}</li>
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
 *     <li>{@link #newShader()}</li>
 *     <li>{@link #newStamper()}</li>
 *     <li>{@link #newTester()}</li>
 *     <li>{@link #newTool(Class, Class)}</li>
 * </ul>
 *
 * <p><b>Miscellaneous</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #withParsedCommandLine(CommandLine)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class Builder extends BaseRepeater implements
    Described,
    BuildExecutionStep,
    Copyable<Builder>,
    BuildStructure,
    BuilderAssociated,
    BuildSettings,
    BuildEnvironmentTrait,
    ToolFactory,
    TryCatchTrait,
    Dependency
{
    /** The associated build */
    private final Build build;

    /** The librarian to resolve dependencies for this builder */
    private Librarian librarian;

    /** The dependencies that must be resolved before this builder can run. */
    @Expose
    private ArtifactList artifactDependencies;

    /** The builders that this artifact is dependent on */
    @Expose
    private BuilderList builderDependencies;

    /** The build settings for this builder */
    @Expose
    private BuildSettings settings;

    /**
     * Creates a builder for the given build
     *
     * @param build The associated build for this builder
     */
    public Builder(Build build)
    {
        build.listenTo(this);

        this.build = build;

        librarian = new RepositorySearchLibrarian();
        artifactDependencies = artifacts();
        builderDependencies = builders();
        settings = buildSettings(this);

        phase(PHASE_CLEAN).duringPhase(Builder::onClean);
        phase(PHASE_COMPILE).duringPhase(Builder::onCompile);
        phase(PHASE_PREPARE).duringPhase(Builder::onPrepare);
    }

    /**
     * Creates a copy of the give builder
     *
     * @param that The builder to copy
     */
    protected Builder(Builder that)
    {
        ensure(that.hasListeners());
        copyListenersFrom(that);

        this.build = that.build;
        this.librarian = that.librarian.copy();
        this.artifactDependencies = that.artifactDependencies.copy();
        this.builderDependencies = that.builderDependencies.copy();
        this.settings = that.settings.copy();
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
     * Returns the build for this builder
     *
     * @return The build
     */
    public Build build()
    {
        return build;
    }

    public Builder builder(ArtifactDescriptor descriptor)
    {
        for (var at : builderDependencies)
        {
            if (descriptor.matches(at.descriptor()))
            {
                return at;
            }
        }
        return null;
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

    @Override
    public Count builderThreads()
    {
        return settings().builderThreads();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Builder copy()
    {
        ensure(hasListeners());
        return new Builder(this);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public ArtifactList dependencies()
    {
        return artifactDependencies;
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
        var name = pathOptionalSuffix(path, '/');
        return withRootFolder(rootFolder().folder(path))
            .withArtifactName(name)
            .withNoDependencies();
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
                        
            Fiasco is a pure-Java build tool designed to enable complex, flexible builds.
            The phases below can be enabled by passing the phase name as an argument. If
            a phase name starts with a minus sign (-), the phase will be disabled. For
            example, the command line "fiasco assemble -test" will run all of the phases
            required to assemble the project's packages, but will skip any tests.

            Fiasco is named after the science fiction novel Fiasko, by Stanislaw Lem.
            """);
        description.add("""
            Commands
                            
              command               description
              -----------           ---------------------------------------------""");

        for (var option : BuildOption.values())
        {
            description.add(String.format("  %-22s%s", option.name().toLowerCase(), option.help()));
        }

        description.add("""
                    
            Phases
                        
              phase                 description
              -----------           ---------------------------------------------""");

        for (var phase : phases())
        {
            description.add(String.format("  %-22s%s", phase.name(), phase.description()));
        }

        description.add("""
                    
            Profiles
                        
              profile               description
              -----------           ---------------------------------------------""");

        for (var profile : profiles())
        {
            description.add(String.format("  %-22s%s", profile.name(), profile.description()));
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

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Builder that)
        {
            return name().equals(that.name());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return name().hashCode();
    }

    @Override
    public boolean isEnabled(Phase phase)
    {
        return settings.isEnabled(phase);
    }

    @Override
    public boolean isEnabled(BuildOption option)
    {
        return settings.isEnabled(option);
    }

    @Override
    public boolean isEnabled(BuildProfile profile)
    {
        return settings.isEnabled(profile);
    }

    /**
     * Returns the librarian used to resolve dependencies for this builder
     *
     * @return The librarian
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

    public void onClean()
    {
        newCleaner()
            .withFiles(targetFolder()
                .nestedFiles(matchAll()))
            .run();
    }

    public void onCompile()
    {
        newCompiler()
            .withSources(sourceMainJavaSources())
            .run();

        newStamper().run();
    }

    public void onPrepare()
    {
        newCopier()
            .withTargetFolder(targetClassesFolder())
            .withFiles(sourceMainResourcesFolder().nestedFiles())
            .run();

        newCopier()
            .withTargetFolder(targetTestClassesFolder())
            .withFiles(sourceTestResourcesFolder().nestedFiles())
            .run();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public ObjectList<BuildOption> options()
    {
        return settings.options();
    }

    @Override
    public Phase phase(String name)
    {
        return settings.phase(name);
    }

    @Override
    public Phase phase(Phase phase)
    {
        return settings.phase(phase.name());
    }

    @Override
    public @NotNull PhaseList phases()
    {
        return settings.phases();
    }

    @Override
    public BuildProfile profile(String name)
    {
        return settings.profile(name);
    }

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

    @Override
    public Count resolverThreads()
    {
        return settings().resolverThreads();
    }

    @Override
    public Folder rootFolder()
    {
        return settings().rootFolder();
    }

    /**
     * Runs this builder, returning a result. The result contains any captured messages, and a reference to the builder
     * to signify which builder the result is for.
     *
     * @return Returns the build result
     */
    public final Result<Builder> run()
    {
        return result(() ->
        {
            // go through each phase in order,
            for (var phase : settings().phases())
            {
                // and if the phase is enabled,
                if (isEnabled(phase))
                {
                    trace("Phase $", phase);

                    // notify that the phase has started,
                    phase.internalOnBefore(this);

                    // run the phase calling all listeners,
                    if (settings.isEnabled(VERBOSE))
                    {
                        announce(bannerLine(phase.name() + " (" + descriptor().groupAndName() + ")"));
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
     * Returns the settings for this builder
     *
     * @return The settings
     */
    public BuildSettings settings()
    {
        return settings;
    }

    @Override
    public boolean shouldDescribe()
    {
        return settings().shouldDescribe();
    }

    @Override
    public boolean shouldDescribeAndExecute()
    {
        return settings().shouldDescribeAndExecute();
    }

    /**
     * Returns this artifact in JSON form
     */
    @Override
    public String toJson()
    {
        var serializer = new GsonObjectSerializer();
        var serialized = new StringOutputResource();
        serializer.writeObject(serialized, new SerializableObject<>(this), METADATA_OBJECT_TYPE);
        return serialized.string();
    }

    @Override
    public String toString()
    {
        return format("builder:$:$", descriptor().groupAndName(), descriptor().version());
    }

    @Override
    public YamlNode toYaml()
    {
        return unsupported();
    }

    /**
     * Returns a copy of this object that runs the given code after the given phase runs
     *
     * @param phase The phase
     * @param code The code to run
     * @return This builder, for chaining
     */
    public Builder withActionAfterPhase(Phase phase, BuildAction code)
    {
        return mutated(it -> it.phase(phase.name()).afterPhase(code));
    }

    /**
     * Returns a copy of this builder with the given action to be executed before the given phase
     *
     * @param phase The phase
     * @param code The action to execute
     * @return The copy
     */
    public Builder withActionBeforePhase(Phase phase, BuildAction code)
    {
        return mutated(it -> it.phase(phase.name()).beforePhase(code));
    }

    /**
     * Runs the given code during the given phase
     *
     * @param phase The phase
     * @param code The code to run
     * @return This builder, for chaining
     */
    public Builder withActionDuringPhase(Phase phase, BuildAction code)
    {
        return mutated(it -> it.phase(phase.name()).duringPhase(code));
    }

    /**
     * {@inheritDoc}
     *
     * @param artifact {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withArtifact(String artifact)
    {
        return withSettings(it -> it.withArtifact(artifact));
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
        return withSettings(it -> it.withArtifactDescriptor(descriptor));
    }

    /**
     * {@inheritDoc}
     *
     * @param descriptor {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withArtifactDescriptor(String descriptor)
    {
        return withSettings(it -> it.withArtifactDescriptor(descriptor));
    }

    /**
     * {@inheritDoc}
     *
     * @param function {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withArtifactDescriptor(Function<ArtifactDescriptor, ArtifactDescriptor> function)
    {
        return withSettings(it -> it.withArtifactDescriptor(function));
    }

    /**
     * {@inheritDoc}
     *
     * @param group {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withArtifactGroup(ArtifactGroup group)
    {
        return withSettings(it -> it.withArtifactGroup(group));
    }

    /**
     * {@inheritDoc}
     *
     * @param group {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withArtifactGroup(String group)
    {
        return withSettings(it -> it.withArtifactGroup(group));
    }

    /**
     * {@inheritDoc}
     *
     * @param artifactName {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withArtifactName(ArtifactName artifactName)
    {
        return withSettings(it -> it.withArtifactName(artifactName));
    }

    /**
     * {@inheritDoc}
     *
     * @param artifactName {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withArtifactName(String artifactName)
    {
        return withSettings(it -> it.withArtifactName(artifactName));
    }

    /**
     * {@inheritDoc}
     *
     * @param version {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withArtifactVersion(String version)
    {
        return withSettings(it -> it.withArtifactVersion(version));
    }

    /**
     * {@inheritDoc}
     *
     * @param version {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withArtifactVersion(Version version)
    {
        return withSettings(it -> it.withArtifactVersion(version));
    }

    /**
     * {@inheritDoc}
     *
     * @param threads {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withBuilderThreads(Count threads)
    {
        return withSettings(it -> it.withBuilderThreads(threads));
    }

    /**
     * Returns a copy of this builder with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    public Builder withDependencies(Builder... dependencies)
    {
        return mutated(it -> it.builderDependencies = builderDependencies.with(dependencies).deduplicated());
    }

    /**
     * Returns a copy of this builder with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    public Builder withDependencies(ArtifactList dependencies)
    {
        return mutated(it -> it.artifactDependencies = artifactDependencies.with(dependencies).deduplicated());
    }

    /**
     * Returns a copy of this builder with one or more artifact dependencies added
     *
     * @param artifactDescriptors The artifact descriptors
     * @return The copy
     */
    public Builder withDependencies(String... artifactDescriptors)
    {
        return mutated(it -> it.artifactDependencies = artifactDependencies
            .with(descriptors(artifactDescriptors).asArtifacts()).deduplicated());
    }

    /**
     * Returns a copy of this builder with one or more artifact dependencies added
     *
     * @param first The first dependency
     * @param rest Any further dependencies
     * @return The copy
     */
    public Builder withDependencies(Artifact<?> first, Artifact<?>... rest)
    {
        return mutated(it -> it.artifactDependencies = artifactDependencies.with(first, rest).deduplicated());
    }

    /**
     * {@inheritDoc}
     *
     * @param phase {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withDisabled(Phase phase)
    {
        return withSettings(it -> withDisabled(phase));
    }

    /**
     * {@inheritDoc}
     *
     * @param profile {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withDisabled(BuildProfile profile)
    {
        return withSettings(it -> withDisabled(profile));
    }

    /**
     * {@inheritDoc}
     *
     * @param option {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withDisabled(BuildOption option)
    {
        return withSettings(it -> it.withDisabled(option));
    }

    /**
     * {@inheritDoc}
     *
     * @param phase {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withEnabled(Phase phase)
    {
        return withSettings(it -> it.withEnabled(phase));
    }

    /**
     * {@inheritDoc}
     *
     * @param option {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withEnabled(BuildOption option)
    {
        return withSettings(it -> it.withEnabled(option));
    }

    /**
     * {@inheritDoc}
     *
     * @param profile {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withEnabled(BuildProfile profile)
    {
        return withSettings(it -> withEnabled(profile));
    }

    /**
     * Returns a copy of this builder using the given librarian
     *
     * @param librarian The librarian to use
     * @return The copy
     */
    public Builder withLibrarian(Librarian librarian)
    {
        return mutated(it -> it.librarian = librarian);
    }

    /**
     * Returns a copy of this builder with no dependencies
     *
     * @return The copy
     */
    public Builder withNoDependencies()
    {
        return mutated(it ->
        {
            it.builderDependencies = builders();
            it.artifactDependencies = artifacts();
        });
    }

    /**
     * Makes a copy of this builder, transformed by parsing the given command line, enabling and disabling relevant
     * phases, profiles and build options. Giving names to phases or profiles that are not unique is discouraged, but in
     * that event, options have higher priority than phases, and phases have higher priority than profiles.
     *
     * @param commandLine The command line to process
     * @return A copy of this builder transformed by the given command line
     */
    public Builder withParsedCommandLine(CommandLine commandLine)
    {
        var builder = this;
        for (var argument : commandLine.argumentValues())
        {
            var value = argument.value();
            var enable = !value.startsWith("-");
            var option = tryCatch(() -> BuildOption.valueOf(value.toUpperCase()));

            if (option != null)
            {
                if (enable)
                {
                    builder = builder.withSettings(it -> it.withEnabled(option));
                }
                else
                {
                    builder = builder.withSettings(it -> it.withDisabled(option));
                }
            }
            else
            {
                var phase = phase(value);
                if (phase != null)
                {
                    if (enable)
                    {
                        builder = builder.withSettings(it -> it.withEnabled(phase));
                    }
                    else
                    {
                        builder = builder.withSettings(it -> it.withDisabled(phase));
                    }
                }
                else
                {
                    var profile = profile(value);
                    if (profile != null)
                    {
                        if (enable)
                        {
                            builder = builder.withSettings(it -> it.withEnabled(profile));
                        }
                        else
                        {
                            builder = builder.withSettings(it -> it.withDisabled(profile));
                        }
                    }
                    else
                    {
                        ((Application) build).exit("Unrecognized argument: $", argument);
                    }
                }
            }
        }

        return builder;
    }

    /**
     * {@inheritDoc}
     *
     * @param phases {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withPhases(PhaseList phases)
    {
        return withSettings(it -> it.withPhases(phases));
    }

    /**
     * Returns a copy of this builder with the given artifact globally "pinned" to the given version.
     *
     * @param artifact The artifact
     * @param version The version to enforce
     * @return The copy
     */
    public Builder withPinnedVersion(Artifact<?> artifact, String version)
    {
        return withPinnedVersion(artifact, version(version));
    }

    /**
     * Returns a copy of this builder with the given artifact globally "pinned" to the given version.
     *
     * @param artifact The artifact
     * @param version The version to enforce
     * @return The copy
     */
    public Builder withPinnedVersion(Artifact<?> artifact, Version version)
    {
        return mutated(it -> it.librarian = librarian.withPinnedVersion(artifact, version));
    }

    /**
     * {@inheritDoc}
     *
     * @param profile {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withProfile(BuildProfile profile)
    {
        return withSettings(it -> it.withProfile(profile));
    }

    /**
     * {@inheritDoc}
     *
     * @param threads {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Builder withResolverThreads(Count threads)
    {
        return withSettings(it -> it.withResolverThreads(threads));
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
        return withSettings(it -> it.withRootFolder(rootFolder));
    }

    /**
     * Returns a copy of this builder with the given settings
     *
     * @param settings The settings
     * @return The copy
     */
    public Builder withSettings(BuildSettings settings)
    {
        return mutated(it -> it.settings = settings);
    }

    /**
     * Returns a copy of this builder without any actions defined for the given phase
     *
     * @param phase The phase
     * @return The copy
     */
    public Builder withoutActions(Phase phase)
    {
        return mutated(it -> it.phase(phase.name()).clearActions());
    }

    /**
     * Returns a copy of this builder with its build settings transformed by the given function
     *
     * @param function The transformation function
     * @return The copy
     */
    private Builder withSettings(Function<BuildSettings, BuildSettings> function)
    {
        return withSettings(function.apply(settings()));
    }
}
