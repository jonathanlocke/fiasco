package fiasco.tools.compiler;

import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.FileList;
import fiasco.tools.Tools;
import fiasco.tools.BaseTool;

@SuppressWarnings({ "FieldCanBeLocal", "unused" })
public class Compiler extends BaseTool
{
    private Version sourceVersion;

    private Version targetVersion;

    private FileList sources;

    public Compiler(Tools module)
    {
        super(module);
    }

    public Compiler sourceVersion(Version version)
    {
        sourceVersion = version;
        return this;
    }

    public Compiler sources(FileList sources)
    {
        this.sources = sources;
        return null;
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
