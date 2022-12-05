package fiasco.tools;

import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import fiasco.BaseBuild;
import fiasco.Tool;

/**
 * @author jonathan
 */
public abstract class BaseTool extends BaseRepeater implements Tool
{
    private final BaseBuild build;

    public BaseTool(BaseBuild build)
    {
        this.build = build;
    }

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
