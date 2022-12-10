package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import digital.fiasco.runtime.build.BaseBuild;

/**
 * Base class for {@link Tool}s.
 *
 * @author jonathan
 */
public abstract class BaseTool extends BaseRepeater implements Tool
{
    private final BaseBuild build;

    public BaseTool(BaseBuild build)
    {
        this.build = build;
    }

    @Override
    public BaseBuild build()
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
