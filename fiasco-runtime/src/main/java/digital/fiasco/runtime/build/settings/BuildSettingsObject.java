package digital.fiasco.runtime.build.settings;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.object.Copyable;
import com.telenav.kivakit.interfaces.value.Source;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.builder.phases.standard.DefaultPhases;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactGroup;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName;
import digital.fiasco.runtime.librarian.Librarian;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.value.count.Count._8;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.build.settings.BuildProfile.DEFAULT;

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
 *     <li></li>
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
 *     <li>{@link #withArtifactName(ArtifactName)}</li>
 *     <li>{@link #withArtifactVersion(String)}</li>
 *     <li>{@link #withArtifactVersion(Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class BuildSettingsObject extends BaseRepeater implements
    BuildSettings,
    Copyable<BuildSettingsObject>,
    Source<Builder>
{
    /** The builder for these settings */
    private Builder builder;

    /** The number of threads to use when building */
    private Count builderThreads = _8;

    /** The number of threads to use when building */
    private Count resolverThreads = _8;

    /** The set of enabled build options */
    private ObjectSet<BuildOption> enabledOptions = set();

    /** The list of build phases to execute */
    private PhaseList phases;

    /** The root folder for the build */
    private Folder rootFolder;

    /** The primary artifact being built */
    private ArtifactDescriptor descriptor;

    /** The librarian to manage libraries */
    private Librarian librarian;

    /** Set of profiles for this build */
    private ObjectSet<BuildProfile> profiles = set(DEFAULT);

    /** Set of profiles to enable for this build */
    private ObjectSet<BuildProfile> enabledProfiles = set(DEFAULT);

    public BuildSettingsObject(Builder builder)
    {
        this.builder = builder;
        this.phases = new DefaultPhases();
        this.librarian = builder.librarian();
        this.rootFolder = currentFolder();
    }

    BuildSettingsObject()
    {
    }

    /**
     * Creates a copy of the given settings object
     *
     * @param that The settings to copy
     */
    protected BuildSettingsObject(BuildSettingsObject that)
    {
        this.builder = that.builder;
        this.rootFolder = that.rootFolder;
        this.phases = that.phases.copy();
        this.resolverThreads = that.resolverThreads;
        this.builderThreads = that.builderThreads;
        this.enabledOptions = that.enabledOptions.copy();
        this.descriptor = that.descriptor;
        this.librarian = that.librarian.copy();
        this.profiles = that.profiles.copy();
        this.enabledProfiles = that.enabledProfiles.copy();
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
     * Returns a copy of this settings object
     *
     * @return The copy
     */
    @Override
    public BuildSettingsObject copy()
    {
        return new BuildSettingsObject(this);
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

    @Override
    public Builder get()
    {
        return builder;
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
        return enabledOptions.contains(option);
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
        ensure(profiles.contains(profile), "Unknown profile $", profile);

        return enabledProfiles.contains(profile);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public ObjectList<BuildOption> options()
    {
        return list(BuildOption.values());
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
        return phases().phase(name);
    }

    @Override
    public Phase phase(Phase phase)
    {
        return phase(phase.name());
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
     * Returns the profile with the given name
     *
     * @param name The profile name
     * @return The profile
     */
    @Override
    public BuildProfile profile(String name)
    {
        for (var at : profiles)
        {
            if (at.name().equals(name))
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
    public Count resolverThreads()
    {
        return resolverThreads;
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
        var copy = copy();
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
    public BuildSettingsObject withBuilderThreads(Count threads)
    {
        var copy = copy();
        copy.builderThreads = threads;
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
        var copy = copy();
        copy.phases().disable(phase);
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
        var copy = copy();
        copy.enabledOptions.remove(option);
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
        ensure(profiles.contains(profile), "Unknown profile $", profile);

        var copy = copy();
        copy.enabledProfiles.remove(profile);
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
        var copy = copy();
        copy.phases().enable(phase);
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
        var copy = copy();
        copy.enabledOptions.add(option);
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
        ensure(profiles.contains(profile), "Unknown profile $", profile);

        var copy = copy();
        copy.enabledProfiles.add(profile);
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
        var copy = copy();
        copy.phases = phases.copy();
        return copy;
    }

    /**
     * Returns this settings object with the given profile added
     *
     * @param profile The profile to add
     * @return The copy
     */
    @Override
    public BuildSettingsObject withProfile(BuildProfile profile)
    {
        return mutatedCopy(it -> it.profiles.add(profile));
    }

    /**
     * {@inheritDoc}
     *
     * @param threads {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BuildSettingsObject withResolverThreads(Count threads)
    {
        var copy = copy();
        copy.resolverThreads = threads;
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
        var copy = copy();
        copy.rootFolder = rootFolder;
        return copy;
    }
}
