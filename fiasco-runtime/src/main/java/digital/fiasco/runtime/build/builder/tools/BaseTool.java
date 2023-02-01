package digital.fiasco.runtime.build.builder.tools;

import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.object.Copyable;
import digital.fiasco.runtime.build.execution.BuildExecutionStep;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.build.settings.BuildProfile;
import digital.fiasco.runtime.dependency.collections.ArtifactList;

/**
 * Base class for build {@link Tool}s. Build tools can be enabled or disabled under a given {@link BuildProfile}.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public abstract class BaseTool<T extends BaseTool<T>> extends BaseRepeater implements
    BuildExecutionStep,
    Copyable<T>,
    Tool<T>
{
    /** The builder associated with this tool */
    private final Builder builder;

    /**
     * Creates a tool associated with the given builder
     *
     * @param builder The builder
     */
    public BaseTool(Builder builder)
    {
        this.builder = builder;
    }

    /**
     * Creates a copy of the given tool subclass
     *
     * @param that The tool to copy
     */
    public BaseTool(T that)
    {
        this.builder = that.builder();
    }

    /**
     * Returns the list of dependencies from the builder associated with this tool
     *
     * @return The dependency list
     */
    @Override
    public ArtifactList artifactDependencies()
    {
        return builder.artifactDependencies();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Builder associatedBuilder()
    {
        return builder;
    }

    public Builder builder()
    {
        return builder;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public abstract T copy();

    /**
     * Returns true if this tool is enabled under any of the profiles it is assigned to
     *
     * @return True if enabled
     */
    @Override
    public boolean isEnabled()
    {
        for (var profile : builder.profiles())
        {
            if (builder.settings().isEnabled(profile))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the librarian used by the builder associated with this tool
     *
     * @return The librarian
     */
    @Override
    public Librarian librarian()
    {
        return builder.librarian();
    }

    /**
     * Called after this tool has been run
     */
    @Override
    public void onRan()
    {
    }

    /**
     * Called when this tool runs
     */
    @Override
    public abstract void onRun();

    /**
     * Called before this tool runs
     */
    @Override
    public void onRunning()
    {
    }

    /**
     * Returns the root folder for the builder associated with this tool
     */
    @Override
    public Folder rootFolder()
    {
        return builder.rootFolder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void run()
    {
        if (isEnabled())
        {
            onRunning();
            onRun();
            onRan();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean shouldDescribe()
    {
        return builder.shouldDescribe();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean shouldDescribeAndExecute()
    {
        return builder.shouldDescribeAndExecute();
    }
}
