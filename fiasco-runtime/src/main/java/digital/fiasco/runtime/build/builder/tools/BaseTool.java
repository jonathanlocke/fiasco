package digital.fiasco.runtime.build.builder.tools;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.object.Copyable;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.execution.BuildExecutionStep;
import digital.fiasco.runtime.build.settings.BuildOption;
import digital.fiasco.runtime.build.settings.BuildProfile;
import digital.fiasco.runtime.build.settings.BuildSettings;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;
import digital.fiasco.runtime.librarian.Librarian;

import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static digital.fiasco.runtime.build.settings.BuildOption.VERBOSE;
import static digital.fiasco.runtime.build.settings.BuildProfile.DEFAULT;

/**
 * Base class for build {@link Tool}s. Build tools can be enabled or disabled under a given {@link BuildProfile}.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public abstract class BaseTool<TOOL extends BaseTool<TOOL, OUTPUT>, OUTPUT> extends BaseComponent implements
    BuildExecutionStep,
    Copyable<TOOL>,
    Tool<TOOL, OUTPUT>
{
    /** The builder associated with this tool */
    private final Builder builder;

    /** The profile under which this tool instance is enabled */
    private BuildProfile profile = DEFAULT;

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
    public BaseTool(TOOL that)
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

    /**
     * Returns the build using this tool
     *
     * @return The build
     */
    public Build build()
    {
        return builder.build();
    }

    /**
     * Returns the builder using this tool
     *
     * @return The builder
     */
    public Builder builder()
    {
        return builder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkConsistency()
    {
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public abstract TOOL copy();

    /**
     * Returns true if this tool is enabled under any of the profiles it is assigned to
     *
     * @return True if enabled
     */
    @Override
    public boolean isEnabled()
    {
        return builder.isEnabled(profile);
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
    public abstract OUTPUT onRun();

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
    public final OUTPUT run()
    {
        OUTPUT output = null;

        if (isEnabled())
        {
            onRunning();
            output = onRun();
            onRan();
        }

        return output;
    }

    public BuildSettings settings()
    {
        return builder.settings();
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

    /**
     * Returns a copy of this tool that only runs under the given profile
     *
     * @param profile The profile
     * @return The copy
     */
    public TOOL withProfile(BuildProfile profile)
    {
        return mutatedCopy(it -> ((BaseTool<?, ?>) it).profile = profile);
    }

    /**
     * Returns a listener based on the settings for the builder using this tool. For example, if
     * {@link BuildOption#VERBOSE} is not enabled, a null listener would be returned
     *
     * @return The listener to transmit to
     */
    protected Listener listener()
    {
        if (settings().isEnabled(VERBOSE))
        {
            return this;
        }
        else
        {
            return nullListener();
        }
    }
}
