package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.Build;

/**
 * Base class for {@link Tool}s.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public abstract class BaseTool extends BaseRepeater implements Tool
{
    private final Build build;

    public BaseTool(Build build)
    {
        this.build = build;
        addListener(build);
    }

    @Override
    public Build associatedBuild()
    {
        return build;
    }

    @Override
    public final void run()
    {
        if (describe())
        {
            onDescribe();
        }
        else
        {
            onRunning();
            onRun();
            onRan();
        }
    }

    protected boolean describe()
    {
        return build.dryRun();
    }

    protected abstract String description();

    protected void onDescribe()
    {
        announce(" \n" + description());
    }

    protected void onRan()
    {
    }

    protected abstract void onRun();

    protected void onRunning()
    {
    }

    protected Folder rootFolder()
    {
        return build.rootFolder();
    }
}
