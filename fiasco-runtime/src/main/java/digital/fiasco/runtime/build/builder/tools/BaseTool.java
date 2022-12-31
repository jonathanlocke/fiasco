package digital.fiasco.runtime.build.builder.tools;

import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.DependencyList;

import static digital.fiasco.runtime.build.BuildOption.DESCRIBE;

/**
 * Base class for {@link Tool}s.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public abstract class BaseTool extends BaseRepeater implements Tool
{
    private final Builder builder;

    public BaseTool(Builder builder)
    {
        this.builder = builder;
    }

    @Override
    public Builder associatedBuilder()
    {
        return builder;
    }

    public Builder builder()
    {
        return builder;
    }

    public DependencyList dependencies()
    {
        return builder().dependencies();
    }

    public Librarian librarian()
    {
        return builder().librarian();
    }

    public PhaseList phases()
    {
        return builder().phases();
    }

    @Override
    public Folder rootFolder()
    {
        return builder().rootFolder();
    }

    @Override
    public final void run()
    {
        if (builder.isEnabled(DESCRIBE))
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
}
