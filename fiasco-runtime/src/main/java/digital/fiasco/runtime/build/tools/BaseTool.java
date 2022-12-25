package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.phases.PhaseList;
import digital.fiasco.runtime.build.tools.builder.Builder;
import digital.fiasco.runtime.build.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.DependencyList;

import static digital.fiasco.runtime.build.BuildOption.DRY_RUN;

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
        associatedBuild().addListener(this);
    }

    @Override
    public Build associatedBuild()
    {
        return build;
    }

    public Builder builder()
    {
        return associatedBuild().builder();
    }

    public DependencyList dependencies()
    {
        return associatedBuild().dependencies();
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
        return associatedBuild().rootFolder();
    }

    @Override
    public final void run()
    {
        if (associatedBuild().isEnabled(DRY_RUN))
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
