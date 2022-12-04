package fiasco.plugins.compiler;

import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.FileList;
import fiasco.Module;
import fiasco.plugins.Plugin;

@SuppressWarnings({ "FieldCanBeLocal", "unused" })
public class Compiler extends Plugin
{
    private Version sourceVersion;

    private Version targetVersion;

    private FileList sources;

    public Compiler(Module module)
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
