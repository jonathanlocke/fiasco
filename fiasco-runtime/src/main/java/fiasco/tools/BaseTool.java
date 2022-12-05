package fiasco.tools;

import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.filesystem.Folder;
import fiasco.Tool;

/**
 * @author jonathan
 */
public abstract class BaseTool extends BaseRepeater implements Tool
{
    @Override
    public final void run()
    {
        onRunning();
        onRun();
        onRan();
    }

    protected void onRan()
    {
    }

    protected abstract void onRun();

    protected void onRunning()
    {
    }

    protected Folder resolveFolder(Folder folder)
    {
        if (folder.path().isRelative())
        {
            return module().folder().folder(folder);
        }
        else
        {
            return folder;
        }
    }
}
