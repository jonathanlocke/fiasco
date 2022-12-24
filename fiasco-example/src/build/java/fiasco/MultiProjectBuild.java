package fiasco;

import digital.fiasco.libraries.Libraries;
import digital.fiasco.runtime.build.MultiBuild;

/**
 * Example Fiasco build.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused" })
public abstract class MultiProjectBuild extends MultiBuild implements
        Libraries,
        ProjectLibraries,
        ProjectMetadata
{
    @Override
    protected void onInitialize()
    {
        pinVersions();
    }
}
