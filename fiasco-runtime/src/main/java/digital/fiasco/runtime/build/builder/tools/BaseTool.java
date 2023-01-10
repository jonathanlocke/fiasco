package digital.fiasco.runtime.build.builder.tools;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.BuildProfile;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.DependencyList;

import static digital.fiasco.runtime.build.BuildOption.DESCRIBE;

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
     * {@inheritDoc}
     */
    @Override
    public Builder associatedBuilder()
    {
        return builder;
    }

    /**
     * Returns the list of dependencies from the builder associated with this tool
     *
     * @return The dependency list
     */
    @Override
    public DependencyList<?> dependencies()
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
    }
}
