package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;

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
    private final ObjectList<Runnable> runBefore = list();

    /** The code to run when the phase executes */
    private final ObjectList<Runnable> run = list();

    /** The code to run after the phase executes */
    private final ObjectList<Runnable> runAfter = list();

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
    public Phase afterPhase(Runnable code)
    {
        runAfter.add(code);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Phase beforePhase(Runnable code)
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
    public final void internalOnAfter()
    {
        runAfter.forEach(Runnable::run);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void internalOnBefore()
    {
        runBefore.forEach(Runnable::run);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void internalOnRun()
    {
        run.forEach(Runnable::run);
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
     * {@inheritDoc}
     */
    @Override
    public Phase onPhase(Runnable code)
    {
        run.add(code);
        return this;
    }
}
