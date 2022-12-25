package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.messaging.Repeater;

@SuppressWarnings("UnusedReturnValue")
public interface Build extends
        Repeater,
        Buildable,
        BuildStructured,
        BuildDependencies,
        BuildArtifact,
        BuildPhased
{
    /**
     * Returns the metadata for this build
     */
    BuildMetadata metadata();
}
