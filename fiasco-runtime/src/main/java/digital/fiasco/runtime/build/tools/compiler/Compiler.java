package digital.fiasco.runtime.build.tools.compiler;

import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.resource.ResourceList;
import digital.fiasco.runtime.build.tools.BaseTool;
import digital.fiasco.runtime.build.BaseBuild;

@SuppressWarnings({ "FieldCanBeLocal", "unused" })
public class Compiler extends BaseTool
{
    private Version sourceVersion;

    private Version targetVersion;

    private ResourceList sources;

    public Compiler(BaseBuild build)
    {
        super(build);
    }

    public Compiler sourceVersion(Version version)
    {
        sourceVersion = version;
        return this;
    }

    public Compiler sources(ResourceList sources)
    {
        this.sources = sources;
        return this;
    }

    public Compiler targetVersion(Version version)
    {
        targetVersion = version;
        return this;
    }

    @Override
    protected void onRun()
    {
    }
}
