package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import digital.fiasco.runtime.build.Build;

/**
 * Base class for {@link Tool}s.
 *
 * @author jonathan
 */
public abstract class BaseTool extends BaseRepeater implements Tool
{
    private final Build build;

    public BaseTool(Build build)
    {
        this.build = build;
    }

    @Override
    public Build build()
    {
        return build;
    }

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
}
