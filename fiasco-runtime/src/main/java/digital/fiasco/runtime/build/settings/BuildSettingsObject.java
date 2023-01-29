package digital.fiasco.runtime.build.settings;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.builder.phases.standard.StandardPhases;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactGroup;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName;
import digital.fiasco.runtime.dependency.artifact.collections.ArtifactList;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;

/**
 * Settings used by {@link Builder} to modify the way that it builds.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #phases()}</li>
 *     <li>{@link #rootFolder()}</li>
 *     <li>{@link #builderThreads()}</li>
 *     <li>{@link #withPhases(PhaseList)}</li>
 *     <li>{@link #withRootFolder(Folder)}</li>
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
 *     <li>{@link #withDisabled(Phase)}</li>
 *     <li>{@link #withEnabled(Phase)}</li>
 *     <li>{@link #isEnabled(Phase)}</li>
 *     <li>{@link #phase(String)}</li>
 *     <li>{@link #phases()}</li>
 *     <li>{@link #withPhases(PhaseList)}</li>
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
 *     <li>{@link #withArtifact(String)}</li>
 *     <li>{@link #withArtifact(ArtifactName)}</li>
 *     <li>{@link #withArtifactVersion(String)}</li>
 *     <li>{@link #withArtifactVersion(Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class BuildSettingsObject implements BuildSettingsMixin
{
    /** The builder for these settings */
    private Builder builder;

    /** The number of threads to use when building */
    private Count builderThreads;

    /** The number of threads to use when building */
    private Count artifactResolverThreads;

    /** The set of enabled build options */
    private ObjectSet<BuildOption> options = set();

    /** The list of build phases to execute */
    private PhaseList phases;

    /** The root folder for the build */
    private Folder rootFolder;

    /** The primary artifact being built */
    private ArtifactDescriptor descriptor;

    /** Libraries to compile with */
    private ArtifactList artifacts;

    /** The librarian to manage libraries */
    private Librarian librarian;

    /** Set of profiles to enable for this build */
    private ObjectSet<BuildProfile> profiles = set();

    public BuildSettingsObject()
    {
    }

    public BuildSettingsObject(Builder builder)
    {
        this.builder = builder;
        this.phases = new StandardPhases(builder);
        this.artifacts = ArtifactList.artifacts();
        this.librarian = builder.newLibrarian();
        this.rootFolder = currentFolder();
    }

    /**
     * Creates a copy of the given settings object
     *
     * @param that The settings to copy
     */
    public BuildSettingsObject(BuildSettingsObject that)
    {
        this.builder = that.builder;
        this.rootFolder = that.rootFolder;
        this.phases = that.phases.copy();
        this.artifactResolverThreads = that.artifactResolverThreads;
        this.builderThreads = that.builderThreads;
        this.options = that.options.copy();
        this.descriptor = that.descriptor;
        this.artifacts = that.dependencies().copy();
        this.librarian = that.librarian;
        this.profiles = that.profiles.copy();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Count artifactResolverThreads()
    {
        return artifactResolverThreads;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Count builderThreads()
    {
        return builderThreads;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public BuildSettingsObject copySettings()
    {
        return new BuildSettingsObject(this);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public ArtifactList dependencies()
    {
        return artifacts;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public ArtifactDescriptor descriptor()
    {
        return descriptor;
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
        return phases.isEnabled(phase);
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
        return options.contains(option);
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
        return profiles.contains(profile);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Librarian librarian()
    {
        return librarian;
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
        return phases.phase(name);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    @NotNull
    public PhaseList phases()
    {
        return phases;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public ObjectSet<BuildProfile> profiles()
    {
        return profiles.copy();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Folder rootFolder()
    {
        return rootFolder;
    }

    /**
     * {@inheritDoc}
     *
     * @param descriptor {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withArtifactDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copySettings();
        copy.descriptor = descriptor;
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param threads {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withArtifactResolverThreads(Count threads)
    {
        var copy = copySettings();
        copy.artifactResolverThreads = threads;
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param threads {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withBuilderThreads(Count threads)
    {
        var copy = copySettings();
        copy.builderThreads = threads;
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param first {@inheritDoc}
     * @param rest {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withDependencies(Artifact<?> first, Artifact<?>... rest)
    {
        var copy = copySettings();
        copy.artifacts = artifacts.with(first, rest);
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param dependencies {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withDependencies(ArtifactList dependencies)
    {
        var copy = copySettings();
        copy.artifacts = this.artifacts.with(dependencies);
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param phase {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withDisabled(Phase phase)
    {
        var copy = copySettings();
        copy.phases.disable(phase);
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param option {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withDisabled(BuildOption option)
    {
        var copy = copySettings();
        copy.options.remove(option);
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param profile {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withDisabled(BuildProfile profile)
    {
        var copy = copySettings();
        copy.profiles.remove(profile);
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param phase {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withEnabled(Phase phase)
    {
        var copy = copySettings();
        copy.phases.enable(phase);
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param option {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withEnabled(BuildOption option)
    {
        var copy = copySettings();
        copy.options.add(option);
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param profile {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withEnabled(BuildProfile profile)
    {
        var copy = copySettings();
        copy.profiles.add(profile);
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param librarian {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withLibrarian(Librarian librarian)
    {
        var copy = copySettings();
        copy.librarian = librarian;
        return copy;
    }

    /**
     * {@inheritDoc}
     *
     * @param phases {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withPhases(PhaseList phases)
    {
        var copy = copySettings();
        copy.phases = phases.copy();
        return copy;
    }

    /**
     * Returns a copy of this settings object with the given root folder
     *
     * @param rootFolder The root folder
     * @return The copy of this settings object
     */
    @Override
    public BuildSettingsObject withRootFolder(Folder rootFolder)
    {
        var copy = copySettings();
        copy.rootFolder = rootFolder;
        return copy;
    }
}
