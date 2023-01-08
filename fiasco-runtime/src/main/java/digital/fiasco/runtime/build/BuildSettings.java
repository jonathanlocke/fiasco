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
import digital.fiasco.runtime.dependency.artifact.ArtifactGroup;
import digital.fiasco.runtime.dependency.artifact.ArtifactIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.version.Version.version;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactGroup.group;

/**
 * Settings used by {@link Builder} to modify the way that it builds.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #phases()}</li>
 *     <li>{@link #rootFolder()}</li>
 *     <li>{@link #threads()}</li>
 *     <li>{@link #withPhases(PhaseList)}</li>
 *     <li>{@link #withRootFolder(Folder)}</li>
 *     <li>{@link #withThreads(Count)}</li>
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
 *     <li>{@link #requires(DependencyList)}</li>
 * </ul>
 *
 * <p><b>Build Phases</b></p>
 *
 * <ul>
 *     <li>{@link #disable(Phase)}</li>
 *     <li>{@link #enable(Phase)}</li>
 *     <li>{@link #isEnabled(Phase)}</li>
 *     <li>{@link #phase(String)}</li>
 *     <li>{@link #phases()}</li>
 *     <li>{@link #withPhases(PhaseList)}</li>
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
 *     <li>{@link #artifactDescriptor()}</li>
 *     <li>{@link #withArtifactDescriptor(String)}</li>
 *     <li>{@link #withArtifactDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withArtifactGroup(String)}</li>
 *     <li>{@link #withArtifactGroup(ArtifactGroup)}</li>
 *     <li>{@link #withArtifact(String)}</li>
 *     <li>{@link #withArtifact(ArtifactIdentifier)}</li>
 *     <li>{@link #withArtifactVersion(String)}</li>
 *     <li>{@link #withArtifactVersion(Version)}</li>
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
    private ArtifactDescriptor artifactDescriptor;

    /** Libraries to compile with */
    private DependencyList<Artifact> dependencies;

    /** The librarian to manage libraries */
    private final Librarian librarian;

    /** Set of profiles to enable for this build */
    private ObjectSet<BuildProfile> enabledProfiles = set();

    public BuildSettings(Builder builder)
    {
        this.builder = builder;
        this.phases = new StandardPhases(builder);
        this.dependencies = dependencyList();
        this.librarian = builder.newLibrarian();
        this.rootFolder = currentFolder();
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
        this.artifactDescriptor = that.artifactDescriptor;
        this.dependencies = that.dependencies().copy();
        this.librarian = that.librarian;
        this.enabledProfiles = that.enabledProfiles.copy();
    }

    /**
     * Returns the artifact descriptor for this build
     */
    public ArtifactDescriptor artifactDescriptor()
    {
        return artifactDescriptor;
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
    public DependencyList<Artifact> dependencies()
    {
        return dependencies;
    }

    /**
     * Disables execution of the given phase
     *
     * @param phase The phase
     */
    public BuildSettings disable(Phase phase)
    {
        var copy = copy();
        copy.phases.disable(phase);
        return copy;
    }

    /**
     * Disables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    public BuildSettings disable(BuildOption option)
    {
        var copy = copy();
        copy.enabledOptions.remove(option);
        return copy;
    }

    /**
     * Returns a copy of this settings object with the given profile disabled
     *
     * @param profile The profile to disable
     * @return The copy of this settings object
     */
    public BuildSettings disable(BuildProfile profile)
    {
        var copy = copy();
        copy.enabledProfiles.remove(profile);
        return copy;
    }

    /**
     * Enables execution of the given phase
     *
     * @param phase The phase
     */
    public BuildSettings enable(Phase phase)
    {
        var copy = copy();
        copy.phases.enable(phase);
        return copy;
    }

    /**
     * Enables the given build option
     *
     * @param option The option
     * @return A copy of this object with the given option enabled
     */
    public BuildSettings enable(BuildOption option)
    {
        var copy = copy();
        copy.enabledOptions.add(option);
        return copy;
    }

    /**
     * Returns a copy of this settings object with the given profile enabled
     *
     * @param profile The profile to enable
     * @return The copy of this settings object with the given build profile enabled
     */
    public BuildSettings enable(BuildProfile profile)
    {
        var copy = copy();
        copy.enabledProfiles.add(profile);
        return copy;
    }

    /**
     * Returns a copy of the set of enabled profiles for this build
     */
    public ObjectSet<BuildProfile> enabledProfiles()
    {
        return enabledProfiles.copy();
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
     * Returns true if the given profile is enabled
     *
     * @param profile The profile
     * @return True if the profile is enabled
     */
    public boolean isEnabled(BuildProfile profile)
    {
        return enabledProfiles.contains(profile);
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
    public BuildSettings pinVersion(Artifact artifact, String version)
    {
        return pinVersion(artifact, version(version));
    }

    /**
     * Globally pins all versions of the given artifact to the specified version
     *
     * @param artifact The artifact
     * @param version The version to use
     * @return The build for method chaining
     */
    public BuildSettings pinVersion(Artifact artifact, Version version)
    {
        librarian().pinVersion(artifact, version);
        return this;
    }

    /**
     * Returns a copy of this build settings object with more build dependencies added
     *
     * @param first The first dependency
     * @param rest Any further dependencies
     * @return The build for method chaining
     */
    public BuildSettings requires(Artifact first, Artifact... rest)
    {
        var copy = copy();
        copy.dependencies = dependencies.with(first, rest);
        return copy;
    }

    /**
     * Returns a copy of this build settings object with more build dependencies added
     *
     * @param dependencies The dependencies to add
     */
    public BuildSettings requires(DependencyList<Artifact> dependencies)
    {
        var copy = copy();
        copy.dependencies = this.dependencies.with(dependencies);
        return copy;
    }

    /**
     * Returns the root folder for this build
     */
    public Folder rootFolder()
    {
        return rootFolder;
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

    /**
     * Returns a copy of this settings object with the given artifact
     *
     * @param artifact The artifact identifier
     * @return The copy
     */
    public BuildSettings withArtifact(String artifact)
    {
        return withArtifact(new ArtifactIdentifier(artifact));
    }

    /**
     * Returns a copy of this settings object with the given artifact
     *
     * @param artifact The artifact
     * @return The copy
     */
    public BuildSettings withArtifact(ArtifactIdentifier artifact)
    {
        return withArtifactDescriptor(descriptor -> descriptor.withArtifactIdentifier(artifact));
    }

    /**
     * Returns a copy of this settings object with the given main artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    public BuildSettings withArtifactDescriptor(String descriptor)
    {
        return withArtifactDescriptor(ArtifactDescriptor.artifactDescriptor(descriptor));
    }

    /**
     * Applies the given tranformation function to the {@link #artifactDescriptor()}, returning a copy of this settings
     * object with the transformed descriptor.
     *
     * @param function The function to apply
     * @return The copy
     */
    public BuildSettings withArtifactDescriptor(Function<ArtifactDescriptor, ArtifactDescriptor> function)
    {
        return withArtifactDescriptor(function.apply(artifactDescriptor()));
    }

    /**
     * Returns a copy of this settings object with the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    public BuildSettings withArtifactDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copy();
        copy.artifactDescriptor = descriptor;
        return copy;
    }

    /**
     * Returns a copy of this settings object with the given artifact group
     *
     * @param group The artifact group
     * @return The copy
     */
    public BuildSettings withArtifactGroup(String group)
    {
        return withArtifactGroup(group(group));
    }

    /**
     * Returns a copy of this settings object with the given artifact group
     *
     * @param group The artifact group
     * @return The copy
     */
    public BuildSettings withArtifactGroup(ArtifactGroup group)
    {
        return withArtifactDescriptor(descriptor -> descriptor.withGroup(group));
    }

    /**
     * Returns a copy of this settings object with the given main artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    public BuildSettings withArtifactVersion(String version)
    {
        return withArtifactVersion(version(version));
    }

    /**
     * Returns a copy of this settings object with the given main artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    public BuildSettings withArtifactVersion(Version version)
    {
        return withArtifactDescriptor(descriptor -> descriptor.withVersion(version));
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
