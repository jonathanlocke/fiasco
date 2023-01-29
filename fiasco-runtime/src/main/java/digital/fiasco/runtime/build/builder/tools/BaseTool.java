package digital.fiasco.runtime.build.builder.tools;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.messages.status.activity.Step;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.code.Code;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.build.settings.BuildProfile;
import digital.fiasco.runtime.dependency.collections.DependencyList;

import static digital.fiasco.runtime.build.settings.BuildOption.DESCRIBE;
import static digital.fiasco.runtime.build.settings.BuildOption.VERBOSE;

/**
 * Base class for build {@link Tool}s. Build tools can be enabled or disabled under a given {@link BuildProfile} by
 * calling {@link #enableForProfile(BuildProfile)} or .
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public abstract class BaseTool extends BaseRepeater implements Tool
{
    /** The builder associated with this tool */
    private final Builder builder;

    /** Any profile that this tool instance should be restricted to */
    private ObjectSet<BuildProfile> profiles;

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
    public BaseTool(BaseTool that)
    {
        this.builder = that.builder;
        this.profiles = that.profiles.copy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Builder associatedBuilder()
    {
        return builder;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public abstract BaseTool copy();

    /**
     * Returns the list of dependencies from the builder associated with this tool
     *
     * @return The dependency list
     */
    @Override
    public DependencyList<?, ?> dependencies()
    {
        return builder.dependencies();
    }

    /**
     * Enables this tool for the given profile
     *
     * @param profile The profile
     * @return This tool for method chaining
     */
    @Override
    public BaseTool enableForProfile(BuildProfile profile)
    {
        this.profiles.add(profile);
        return this;
    }

    /**
     * Returns true if this tool is enabled under any of the profiles it is assigned to
     *
     * @return True if enabled
     */
    @Override
    public boolean isEnabled()
    {
        for (var profile : profiles)
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
     * Called to describe what this tool does (without doing it)
     */
    @Override
    public void onDescribe()
    {
        announce(" \n" + description());
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
    }

    /**
     * Returns true if this tool should describe what it would do rather than actually doing it
     *
     * @return True if the tool should describe its action
     */
    protected boolean describe()
    {
        return builder.isEnabled(DESCRIBE);
    }

    /**
     * Returns true if the tool should execute actions
     *
     * @return True if execution should occur
     */
    protected boolean execute()
    {
        return !describe();
    }

    /**
     * Executes the given code if
     *
     * @param code The code to run
     */
    protected void step(Runnable code, String message, Object... arguments)
    {
        if (describe() || verbose())
        {
            step(message, arguments);
        }
        if (execute())
        {
            code.run();
        }
    }

    /**
     * Executes the given code if
     *
     * @param code The code to run
     * @return The result of the operation, or null if the operation was not executed
     */
    protected <T> T step(Code<T> code, String message, Object... arguments)
    {
        if (describe() || verbose())
        {
            step(message, arguments);
        }
        if (execute())
        {
            return code.run();
        }
        return null;
    }

    /**
     * Broadcasts a {@link Step} message
     *
     * @param message The message
     * @param arguments The message arguments
     */
    protected void step(String message, Object... arguments)
    {
        transmit(new Step(message, arguments));
    }

    protected boolean verbose()
    {
        return builder.isEnabled(VERBOSE);
    }
}
