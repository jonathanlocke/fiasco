package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.filesystem.Folder;

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

    /**
     * Returns a copy of this build with the given root folder
     *
     * @param root The new root folder
     * @return The copy
     */
    Build withRootFolder(Folder root);
}
