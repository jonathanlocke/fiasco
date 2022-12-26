package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.string.Described;
import digital.fiasco.runtime.build.tools.builder.BuildListener;

/**
 * Base class for phases. A phase has a name, a description, and a set of dependent phases, accessed with
 * {@link #requiredPhases()}.
 *
 * @author Jonathan Locke
 */
public abstract class Phase implements
    Named,
    Described
{
    /** The name of this phase */
    private final String name;

    /**
     * Creates a phase
     *
     * @param name The name of the phase
     */
    public Phase(String name)
    {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Phase that)
        {
            return name().equals(that.name());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    /**
     * Returns the name of this phase
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * Returns the list of phases that must be run before this phase
     */
    public abstract ObjectList<Phase> requiredPhases();

    /**
     * Runs this phase of a build
     *
     * @param listener The build listener to call
     */
    public abstract void run(BuildListener listener);
}
