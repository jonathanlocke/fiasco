package digital.fiasco.runtime.build.builder.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.builder.BuildAction;
import digital.fiasco.runtime.build.builder.Builder;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * Base class for phases. A phase has a name, a description, and a set of dependent phases, accessed with
 * {@link #dependsOnPhases()}.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public abstract class BasePhase implements Phase
{
    /** The name of this phase */
    private final String name;

    /** The code to run before the phase executes */
    private final ObjectList<BuildAction> runBefore = list();

    /** The code to run when the phase executes */
    private final ObjectList<BuildAction> run = list();

    /** The code to run after the phase executes */
    private final ObjectList<BuildAction> runAfter = list();

    /**
     * Creates a phase
     *
     * @param name The name of the phase
     */
    public BasePhase(String name)
    {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Phase afterPhase(BuildAction code)
    {
        runAfter.add(code);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Phase beforePhase(BuildAction code)
    {
        runBefore.add(code);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Class<? extends Phase>> dependsOnPhases()
    {
        return list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Phase duringPhase(BuildAction code)
    {
        run.add(code);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof BasePhase that)
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
     * {@inheritDoc}
     */
    @Override
    public final void internalOnAfter(Builder builder)
    {
        runAfter.forEach(it -> it.action(builder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void internalOnBefore(Builder builder)
    {
        runBefore.forEach(it -> it.action(builder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void internalOnRun(Builder builder)
    {
        run.forEach(it -> it.action(builder));
    }

    /**
     * Returns the name of this phase
     */
    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
