package digital.fiasco.runtime.build.settings;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.Rooted;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.execution.BuildExecutionStep;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactGroup;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.telenav.kivakit.core.version.Version.version;
import static digital.fiasco.runtime.build.settings.BuildOption.DESCRIBE;
import static digital.fiasco.runtime.build.settings.BuildOption.VERBOSE;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName.artifactName;

/**
 * Settings that define how a {@link Build} should proceed.
 *
 * <p><b>Target Artifact</b></p>
 *
 * <ul>
 *     <li>{@link #withArtifact(String)}</li>
 *     <li>{@link #withArtifactName(ArtifactName)}</li>
 *     <li>{@link #withArtifactDescriptor(String)}</li>
 *     <li>{@link #withArtifactDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withArtifactDescriptor(Function)}</li>
 *     <li>{@link #withArtifactGroup(String)}</li>
 *     <li>{@link #withArtifactGroup(ArtifactGroup)}</li>
 *     <li>{@link #withArtifactVersion(String)}</li>
 *     <li>{@link #withArtifactVersion(Version)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #descriptor()}</li>
 *     <li>{@link #profiles()}</li>
 *     <li>{@link #rootFolder()}</li>
 *     <li>{@link #withRootFolder(Folder)}</li>
 * </ul>
 *
 * <p><b>Build Options</b></p>
 *
 * <ul>
 *     <li>{@link #withEnabled(BuildOption)}</li>
 *     <li>{@link #withDisabled(BuildOption)}</li>
 *     <li>{@link #isEnabled(BuildOption)}</li>
 * </ul>
 *
 * <p><b>Build Profiles</b></p>
 *
 * <ul>
 *     <li>{@link #profiles()}</li>
 *     <li>{@link #withEnabled(BuildProfile)}</li>
 *     <li>{@link #withDisabled(BuildProfile)}</li>
 *     <li>{@link #isEnabled(BuildProfile)}</li>
 * </ul>
 *
 * <p><b>Build Phases</b></p>
 *
 * <ul>
 *     <li>{@link #phases()}</li>
 *     <li>{@link #phase(String)}</li>
 *     <li>{@link #withEnabled(Phase)}</li>
 *     <li>{@link #withDisabled(Phase)}</li>
 *     <li>{@link #isEnabled(Phase)}</li>
 *     <li>{@link #withPhases(PhaseList)}</li>
 * </ul>
 *
 * <p><b>Threading</b></p>
 *
 * <ul>
 *      <li>{@link #resolverThreads()}</li>
 *      <li>{@link #builderThreads()}</li>
 *      <li>{@link #withResolverThreads(Count)}</li>
 *      <li>{@link #withBuilderThreads(Count)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
public interface BuildSettings extends
    BuildExecutionStep,
    Rooted
{
    static BuildSettings buildSettings()
    {
        return new BuildSettingsObject();
    }

    static BuildSettings buildSettings(Builder builder)
    {
        return new BuildSettingsObject(builder);
    }

    /**
     * Returns the number of threads to use when building
     *
     * @return The number of threads
     */
    Count builderThreads();

    /**
     * Returns a copy of this build settings object
     *
     * @return The copy
     */
    BuildSettings copy();

    /**
     * Returns the artifact descriptor build setting
     *
     * @return The artifact descriptor
     */
    ArtifactDescriptor descriptor();

    /**
     * Returns true if the given phase is enabled
     *
     * @param phase The phase
     * @return The enable state
     */
    boolean isEnabled(Phase phase);

    /**
     * Returns true if the given option is enabled
     *
     * @param option The option
     * @return True if the option is enabled
     */
    boolean isEnabled(BuildOption option);

    /**
     * Returns true if the given profile is enabled
     *
     * @param profile The profile
     * @return True if the profile is enabled
     */
    boolean isEnabled(BuildProfile profile);

    /**
     * Returns a list of all build options
     *
     * @return The options
     */
    ObjectList<BuildOption> options();

    /**
     * Returns the phase with the given name
     *
     * @param name The phase name to look up
     * @return The phase, or null if no phase can be found with the given name
     */
    Phase phase(String name);

    /**
     * Returns the phase with the name of the given phase
     *
     * @param phase The phase to look up
     * @return The phase, or null if no phase can be found with the given name
     */
    Phase phase(Phase phase);

    /**
     * Returns a list of all defined phases
     *
     * @return List of phases
     */
    @NotNull
    PhaseList phases();

    /**
     * Returns the profile with the given name
     *
     * @param name The profile name
     * @return The profile
     */
    BuildProfile profile(String name);

    /**
     * Returns a copy of the set of enabled profiles for this build
     *
     * @return Set of build profiles
     */
    ObjectSet<BuildProfile> profiles();

    /**
     * Returns the number of threads that should be used to resolve artifacts. This is particularly important for remote
     * repositories.
     *
     * @return The number of threads
     */
    Count resolverThreads();

    /**
     * Returns the root folder for this build
     *
     * @return The root folder
     */
    @Override
    Folder rootFolder();

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    default boolean shouldDescribe()
    {
        return isEnabled(DESCRIBE);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    default boolean shouldDescribeAndExecute()
    {
        return isEnabled(VERBOSE);
    }

    /**
     * Returns a copy of this settings object with the given artifact
     *
     * @param artifact The artifact name
     * @return The copy
     */
    default BuildSettings withArtifact(String artifact)
    {
        return withArtifactName(artifactName(artifact));
    }

    /**
     * Returns a copy of this settings object with the given main artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    default BuildSettings withArtifactDescriptor(String descriptor)
    {
        return withArtifactDescriptor(ArtifactDescriptor.artifactDescriptor(descriptor));
    }

    /**
     * Applies the given transformation function to the {@link #descriptor()}, returning a copy of this settings object
     * with the transformed descriptor.
     *
     * @param function The function to apply
     * @return The copy
     */
    default BuildSettings withArtifactDescriptor(Function<ArtifactDescriptor, ArtifactDescriptor> function)
    {
        return withArtifactDescriptor(function.apply(descriptor()));
    }

    /**
     * Returns a copy of this settings object with the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    BuildSettings withArtifactDescriptor(ArtifactDescriptor descriptor);

    /**
     * Returns a copy of this settings object with the given artifact group
     *
     * @param group The artifact group
     * @return The copy
     */
    default BuildSettings withArtifactGroup(String group)
    {
        return withArtifactDescriptor(descriptor -> descriptor.withGroup(group));
    }

    /**
     * Returns a copy of this settings object with the given artifact group
     *
     * @param group The artifact group
     * @return The copy
     */
    default BuildSettings withArtifactGroup(ArtifactGroup group)
    {
        return withArtifactDescriptor(descriptor -> descriptor.withGroup(group));
    }

    /**
     * Returns a copy of this settings object with the given artifact name
     *
     * @param artifactName The artifact name
     * @return The copy
     */
    default BuildSettings withArtifactName(String artifactName)
    {
        return withArtifactName(artifactName(artifactName));
    }

    /**
     * Returns a copy of this settings object with the given artifact name
     *
     * @param artifactName The artifact name
     * @return The copy
     */
    default BuildSettings withArtifactName(ArtifactName artifactName)
    {
        return withArtifactDescriptor(descriptor -> descriptor.withArtifact(artifactName));
    }

    /**
     * Returns a copy of this settings object with the given main artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    default BuildSettings withArtifactVersion(String version)
    {
        return withArtifactVersion(version(version));
    }

    /**
     * Returns a copy of this settings object with the given main artifact version
     *
     * @param version The artifact version
     * @return The copy
     */
    default BuildSettings withArtifactVersion(Version version)
    {
        return withArtifactDescriptor(descriptor -> descriptor.withVersion(version));
    }

    /**
     * Returns a copy of this settings object with the given thread count
     *
     * @param threads The number of threads
     * @return The copy
     */
    BuildSettings withBuilderThreads(Count threads);

    /**
     * Returns a copy of this object with execution of the given phase disabled
     *
     * @param phase The phase
     * @return The copy
     */
    BuildSettings withDisabled(Phase phase);

    /**
     * Returns a copy of this object with given build option disabled
     *
     * @param option The option
     * @return The copy
     */
    BuildSettings withDisabled(BuildOption option);

    /**
     * Returns a copy of this settings object with the given profile disabled
     *
     * @param profile The profile to disable
     * @return The copy
     */
    BuildSettings withDisabled(BuildProfile profile);

    /**
     * Returns a copy of this object with execution of the given phase enabled
     *
     * @param phase The phase
     * @return The copy
     */
    BuildSettings withEnabled(Phase phase);

    /**
     * Returns a copy of this object with given build option enabled
     *
     * @param option The option
     * @return The copy
     */
    BuildSettings withEnabled(BuildOption option);

    /**
     * Returns a copy of this settings object with the given profile enabled
     *
     * @param profile The profile to disable
     * @return The copy
     */
    BuildSettings withEnabled(BuildProfile profile);

    /**
     * Returns a copy of this settings object with the given list of phases
     *
     * @param phases The phase list
     * @return The copy
     */
    BuildSettings withPhases(PhaseList phases);

    /**
     * Returns this settings object with the given profile added
     *
     * @param profile The profile to add
     * @return The copy
     */
    BuildSettings withProfile(BuildProfile profile);

    /**
     * Returns a copy of this settings object with the given thread count
     *
     * @param threads The number of threads
     * @return The copy
     */
    BuildSettings withResolverThreads(Count threads);

    /**
     * Returns a copy of this settings object with the given root folder
     *
     * @param rootFolder The root folder
     * @return The copy
     */
    BuildSettings withRootFolder(Folder rootFolder);
}
