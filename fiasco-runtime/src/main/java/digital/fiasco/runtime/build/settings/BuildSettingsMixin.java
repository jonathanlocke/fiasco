package digital.fiasco.runtime.build.settings;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.mixins.Mixin;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public interface BuildSettingsMixin extends Mixin, BuildSettings
{
    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    default Count artifactResolverThreads()
    {
        return settings().artifactResolverThreads();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    default BuildSettings attachMixin(BuildSettings that)
    {
        return mixin(BuildSettingsMixin.class, () -> (BuildSettingsObject) that);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    default Count builderThreads()
    {
        return settings().builderThreads();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    default ArtifactDescriptor descriptor()
    {
        return settings().descriptor();
    }

    /**
     * {@inheritDoc}
     *
     * @param phase {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default boolean isEnabled(Phase phase)
    {
        return settings().isEnabled(phase);
    }

    /**
     * {@inheritDoc}
     *
     * @param option {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default boolean isEnabled(BuildOption option)
    {
        return settings().isEnabled(option);
    }

    /**
     * {@inheritDoc}
     *
     * @param profile {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default boolean isEnabled(BuildProfile profile)
    {
        return settings().isEnabled(profile);
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default Phase phase(String name)
    {
        return settings().phase(name);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    @NotNull
    default PhaseList phases()
    {
        return settings().phases();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    default ObjectSet<BuildProfile> profiles()
    {
        return settings().profiles();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    default Folder rootFolder()
    {
        return settings().rootFolder();
    }

    /**
     * Accessor for mixin state
     *
     * @return The {@link HashMap} associated with this mixin
     */
    default BuildSettingsObject settings()
    {
        return mixin(BuildSettingsMixin.class, BuildSettingsObject::new);
    }

    /**
     * {@inheritDoc}
     *
     * @param descriptor {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default BuildSettingsObject withArtifactDescriptor(ArtifactDescriptor descriptor)
    {
        return settings().withArtifactDescriptor(descriptor);
    }

    /**
     * {@inheritDoc}
     *
     * @param threads {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default BuildSettingsObject withArtifactResolverThreads(Count threads)
    {
        return settings().withArtifactResolverThreads(threads);
    }

    /**
     * {@inheritDoc}
     *
     * @param threads {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default BuildSettingsObject withBuilderThreads(Count threads)
    {
        return settings().withBuilderThreads(threads);
    }

    /**
     * {@inheritDoc}
     *
     * @param phase {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default BuildSettingsObject withDisabled(Phase phase)
    {
        return settings().withDisabled(phase);
    }

    /**
     * {@inheritDoc}
     *
     * @param option {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default BuildSettingsObject withDisabled(BuildOption option)
    {
        return settings().withDisabled(option);
    }

    /**
     * {@inheritDoc}
     *
     * @param profile {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default BuildSettingsObject withDisabled(BuildProfile profile)
    {
        return settings().withDisabled(profile);
    }

    /**
     * {@inheritDoc}
     *
     * @param phase {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default BuildSettingsObject withEnabled(Phase phase)
    {
        return settings().withEnabled(phase);
    }

    /**
     * {@inheritDoc}
     *
     * @param option {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default BuildSettingsObject withEnabled(BuildOption option)
    {
        return settings().withEnabled(option);
    }

    /**
     * {@inheritDoc}
     *
     * @param profile {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default BuildSettingsObject withEnabled(BuildProfile profile)
    {
        return settings().withEnabled(profile);
    }

    /**
     * {@inheritDoc}
     *
     * @param phases {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    default BuildSettingsObject withPhases(PhaseList phases)
    {
        return settings().withPhases(phases);
    }

    /**
     * Returns a copy of this settings object with the given root folder
     *
     * @param rootFolder The root folder
     * @return The copy of this settings object
     */
    @Override
    default BuildSettingsObject withRootFolder(Folder rootFolder)
    {
        return settings().withRootFolder(rootFolder);
    }
}
