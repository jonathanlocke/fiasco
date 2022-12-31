package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.builder.phases.standard.StandardPhases;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.version.Version.version;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;

/**
 * Settings used by {@link Builder} to modify the way that it builds.
 *
 * <p><b>Build Options</b></p>
 *
 * <ul>
 *     <li>{@link #enable(BuildOption)}</li>
 *     <li>{@link #disable(BuildOption)}</li>
 *     <li>{@link #isEnabled(BuildOption)}</li>
 *     <li>{@link #threads()}</li>
 *     <li>{@link #withThreads(Count)}</li>
 * </ul>
 * <p><b>Build Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies()}</li>
 *     <li>{@link #requires(DependencyList)}</li>
 *     <li>{@link #requires(Artifact, Artifact[])}</li>
 *     <li>{@link #pinVersion(Artifact, String)}</li>
 *     <li>{@link #pinVersion(Artifact, Version)}</li>
 * </ul>
 *
 * <p><b>Phases</b></p>
 *
 * <ul>
 *     <li>{@link #phase(String)}</li>
 *     <li>{@link #phases()}</li>
 *     <li>{@link #disable(Phase)}</li>
 *     <li>{@link #enable(Phase)}</li>
 *     <li>{@link #isEnabled(Phase)}</li>
 * </ul>
 *
 * <p><b>Artifact Descriptors</b></p>
 *
 * <ul>
 *     <li>{@link #targetArtifactDescriptor()}</li>
 *     <li>{@link #targetArtifactName()}</li>
 *     <li>{@link #targetArtifactVersion()}</li>
 *     <li>{@link #withTargetArtifactDescriptor(String)}</li>
 *     <li>{@link #withTargetArtifactDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withTargetArtifactVersion(String)}</li>
 *     <li>{@link #withTargetArtifactVersion(Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class BuildSettings
{
    /** The builder for these settings */
    private final Builder builder;

    /** The number of threads to use when building */
    private Count threads;

    /** The set of enabled build options */
    private ObjectSet<BuildOption> enabledOptions = set();

    /** The list of build phases to execute */
    private PhaseList phases;

    /** The root folder for the build */
    private Folder rootFolder;

    /** The primary artifact being built */
    private ArtifactDescriptor targetArtifactDescriptor;

    /** Libraries to compile with */
    private DependencyList dependencies;

    /** The librarian to manage libraries */
    private final Librarian librarian;

    public BuildSettings(Builder builder)
    {
        this.builder = builder;
        this.phases = new StandardPhases(builder);
        this.dependencies = dependencyList();
        this.librarian = builder.newLibrarian();
    }

    /**
     * Creates a copy of the given settings object
     *
     * @param that The settings to copy
     */
    public BuildSettings(BuildSettings that)
    {
        this.builder = that.builder;
        this.rootFolder = that.rootFolder;
        this.phases = that.phases.copy();
        this.threads = that.threads;
        this.enabledOptions = that.enabledOptions.copy();
        this.targetArtifactDescriptor = that.targetArtifactDescriptor;
        this.dependencies = that.dependencies().copy();
        this.librarian = that.librarian;
    }

    /**
     * Returns a copy of this object
     */
    public BuildSettings copy()
    {
        return new BuildSettings(this);
    }

    /**
     * The libraries required by this build
     *
     * @return The libraries to compile against
     */
    public DependencyList dependencies()
    {
        return dependencies;
    }

    /**
     * Disables execution of the given phase
     *
     * @param phase The phase
     */
    public void disable(Phase phase)
    {
        phases.disable(phase);
    }

    /**
     * Disables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    public BuildSettings disable(BuildOption option)
    {
        enabledOptions.remove(option);
        return this;
    }

    /**
     * Enables execution of the given phase
     *
     * @param phase The phase
     */
    public void enable(Phase phase)
    {
        phases.enable(phase);
    }

    /**
     * Enables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    public BuildSettings enable(BuildOption option)
    {
        enabledOptions.add(option);
        return this;
    }

    /**
     * Returns true if the given phase is enabled
     *
     * @param phase The phase
     * @return The enable state
     */
    public boolean isEnabled(Phase phase)
    {
        return phases.isEnabled(phase);
    }

    /**
     * Returns true if the given option is enabled
     *
     * @param option The option
     * @return True if the option is enabled
     */
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
     * Returns the phase with the given name
     *
     * @param name The phase name to look up
     * @return The phase, or null if no phase can be found with the given name
     */
    public Phase phase(String name)
    {
        return phases.phase(name);
    }

    @NotNull
    public PhaseList phases()
    {
        return phases;
    }

    /**
     * Globally pins all versions of the given artifact to the specified version
     *
     * @param artifact The artifact
     * @param version The version to use
     * @return The build for method chaining
     */
    public BuildSettings pinVersion(Artifact<?> artifact, String version)
    {
        return pinVersion(artifact, version(version));
    }

    /**
     * {@inheritDoc}
     */
    public BuildSettings pinVersion(Artifact<?> artifact, Version version)
    {
        librarian().pinVersion(artifact, version);
        return this;
    }

    /**
     * Adds one or more build dependencies
     *
     * @param first The first dependency
     * @param rest Any further dependencies
     * @return The build for method chaining
     */
    public BuildSettings requires(Artifact<?> first, Artifact<?>... rest)
    {
        dependencies = dependencies.withAdditionalDependencies(first, rest);
        return this;
    }

    /**
     * Adds the given dependencies to this build
     *
     * @param dependencies The dependencies to add
     */
    public BuildSettings requires(DependencyList dependencies)
    {
        this.dependencies = this.dependencies.withAdditionalDependencies(dependencies);
        return this;
    }

    /**
     * Returns the root folder for this build
     */
    public Folder rootFolder()
    {
        return rootFolder;
    }

    /**
     * Returns the artifact descriptor for this build
     */
    public ArtifactDescriptor targetArtifactDescriptor()
    {
        return targetArtifactDescriptor;
    }

    /**
     * Returns the name of this artifact
     *
     * @return The artifact name
     */
    @NotNull
    public String targetArtifactName()
    {
        return targetArtifactDescriptor.name();
    }

    /**
     * Returns the artifact descriptor for this build
     */
    public Version targetArtifactVersion()
    {
        return targetArtifactDescriptor().version();
    }

    /**
     * Returns the number of threads to use when building
     *
     * @return The thread count
     */
    public Count threads()
    {
        return threads;
    }

    public BuildSettings withAdditionalDependencies(Artifact<?> first, Artifact<?>... rest)
    {
        var copy = copy();
        copy.dependencies = dependencies.withAdditionalDependencies(first, rest);
        return this;
    }

    public BuildSettings withAdditionalDependencies(DependencyList dependencies)
    {
        var copy = copy();
        copy.dependencies = dependencies.withAdditionalDependencies(dependencies);
        return this;
    }

    public BuildSettings withDependencies(Artifact<?> first, Artifact<?>... rest)
    {
        var copy = copy();
        copy.dependencies = dependencies.withDependencies(first, rest);
        return this;
    }

    public BuildSettings withDependencies(DependencyList dependencies)
    {
        var copy = copy();
        copy.dependencies = dependencies.withDependencies(dependencies);
        return this;
    }

    /**
     * Returns a copy of this settings object with the given list of phases
     *
     * @param phases The phase list
     * @return The copy of this settings object
     */
    public BuildSettings withPhases(PhaseList phases)
    {
        var copy = copy();
        copy.phases = phases.copy();
        return copy;
    }

    /**
     * Returns a copy of this settings object with the given root folder
     *
     * @param rootFolder The root folder
     * @return The copy of this settings object
     */
    public BuildSettings withRootFolder(Folder rootFolder)
    {
        var copy = copy();
        copy.rootFolder = rootFolder;
        return copy;
    }

    /**
     * Returns a copy of this build with the given main artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    public BuildSettings withTargetArtifactDescriptor(String descriptor)
    {
        return withTargetArtifactDescriptor(ArtifactDescriptor.artifactDescriptor(descriptor));
    }

    /**
     * Returns a copy of this build with the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    public BuildSettings withTargetArtifactDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copy();
        copy.targetArtifactDescriptor = descriptor;
        return copy;
    }

    /**
     * Returns a copy of this build with the given artifact identifier
     *
     * @param identifier The artifact identifier
     * @return The copy
     */
    public BuildSettings withTargetArtifactIdentifier(String identifier)
    {
        return withTargetArtifactDescriptor(targetArtifactDescriptor().withIdentifier(identifier));
    }

    /**
     * Returns a copy of this build with the given main artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    public BuildSettings withTargetArtifactVersion(String version)
    {
        return withTargetArtifactVersion(version(version));
    }

    /**
     * Returns a copy of this build with the given main artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    public BuildSettings withTargetArtifactVersion(Version version)
    {
        return withTargetArtifactDescriptor(targetArtifactDescriptor().withVersion(version));
    }

    /**
     * Returns a copy of this settings object with the given thread count
     *
     * @param threads The number of threads
     * @return The copy of this settings object
     */
    public BuildSettings withThreads(Count threads)
    {
        var copy = copy();
        copy.threads = threads;
        return copy;
    }
}
