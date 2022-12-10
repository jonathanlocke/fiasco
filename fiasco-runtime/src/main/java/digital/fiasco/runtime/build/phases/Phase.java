package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.interfaces.naming.Named;
import digital.fiasco.runtime.build.BuildListener;

public abstract class Phase implements Named
{
    private final String name;

    public Phase(String name)
    {
        this.name = name;
    }

    /**
     * Returns the description of this phase
     */
    public abstract String description();

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Phase that)
        {
            return name().equals(that.name());
        }
        return false;
    }

    public abstract ObjectList<Phase> requiredPhases();

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

    public abstract void run(BuildListener listener);
}
