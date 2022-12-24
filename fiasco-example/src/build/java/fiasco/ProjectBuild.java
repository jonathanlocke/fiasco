package fiasco;

import digital.fiasco.libraries.Libraries;
import digital.fiasco.runtime.build.BaseBuild;

/**
 * Example Fiasco build.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused" })
public abstract class ProjectBuild extends BaseBuild implements
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
