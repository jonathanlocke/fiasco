package digital.fiasco.runtime.build.settings;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.Rooted;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.execution.BuildExecutionStep;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
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
 *      <li>{@link #artifactResolverThreads()}</li>
 *      <li>{@link #builderThreads()}</li>
 *      <li>{@link #withArtifactResolverThreads(Count)}</li>
 *      <li>{@link #withBuilderThreads(Count)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
public interface BuildSettings extends
    BuildExecutionStep,
    Rooted
{
    /**
     * Returns the number of threads that should be used to resolve artifacts. This is particularly important for remote
     * repositories.
     *
     * @return The number of threads
     */
    Count artifactResolverThreads();

    /**
     * Returns a copy of the given settings, attached to this object (if it is a mixin)
     *
     * @return This object for method chaining
     */
    BuildSettings attachMixin(BuildSettings that);

    /**
     * Returns the number of threads to use when building
     *
     * @return The number of threads
     */
    Count builderThreads();

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
     * Returns the phase with the given name
     *
     * @param name The phase name to look up
     * @return The phase, or null if no phase can be found with the given name
     */
    Phase phase(String name);

    /**
     * Returns a list of all defined phases
     *
     * @return List of phases
     */
    @NotNull
    PhaseList phases();

    /**
     * Returns a copy of the set of enabled profiles for this build
     *
     * @return Set of build profiles
     */
    ObjectSet<BuildProfile> profiles();

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
        return withArtifactDescriptor(ArtifactDescriptor.descriptor(descriptor));
    }

    /**
     * Applies the given tranformation function to the {@link #descriptor()}, returning a copy of this settings object
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
     * Returns a copy of this settings object with the given thread count
     *
     * @param threads The number of threads
     * @return The copy of this settings object
     */
    BuildSettings withArtifactResolverThreads(Count threads);

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
     * @return The copy of this settings object
     */
    BuildSettings withBuilderThreads(Count threads);

    /**
     * Disables execution of the given phase
     *
     * @param phase The phase
     * @return This object for method chaining
     */
    BuildSettings withDisabled(Phase phase);

    /**
     * Disables the given build option
     *
     * @param option The option
     * @return This object for method chaining
     */
    BuildSettings withDisabled(BuildOption option);

    /**
     * Returns a copy of this settings object with the given profile disabled
     *
     * @param profile The profile to disable
     * @return The copy of this settings object
     */
    BuildSettings withDisabled(BuildProfile profile);

    /**
     * Enables execution of the given phase
     *
     * @param phase The phase
     * @return This object for method chaining
     */
    BuildSettings withEnabled(Phase phase);

    /**
     * Enables the given build option
     *
     * @param option The option
     * @return A copy of this object with the given option enabled
     */
    BuildSettings withEnabled(BuildOption option);

    /**
     * Returns a copy of this settings object with the given profile enabled
     *
     * @param profile The profile to enable
     * @return The copy of this settings object with the given build profile enabled
     */
    BuildSettings withEnabled(BuildProfile profile);

    /**
     * Returns a copy of this settings object with the given list of phases
     *
     * @param phases The phase list
     * @return The copy of this settings object
     */
    BuildSettings withPhases(PhaseList phases);

    /**
     * Returns a copy of this settings object with the given root folder
     *
     * @param rootFolder The root folder
     * @return The copy of this settings object
     */
    BuildSettings withRootFolder(Folder rootFolder);
}
