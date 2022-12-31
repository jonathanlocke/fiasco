package digital.fiasco.runtime.build.builder.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.string.Described;

/**
 * Base class for phases. A phase has a name, a description, and a set of dependent phases, accessed with
 * {@link #dependsOnPhases()}. Code can be attached to a phase with:
 *
 * <p><b>Phase Actions</b></p>
 *
 * <ul>
 *     <li>{@link #beforePhase(Runnable)}</li>
 *     <li>{@link #onPhase(Runnable)}</li>
 *     <li>{@link #afterPhase(Runnable)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface Phase extends
    Named,
    Described,
    Runnable
{
    /**
     * Runs the given code <i>after</i> this phase runs
     *
     * @param code The code to run
     * @return This phase for chaining
     */
    Phase afterPhase(Runnable code);

    /**
     * Runs the given code <i>before</i> this phase runs
     *
     * @param code The code to run
     * @return This phase for chaining
     */
    Phase beforePhase(Runnable code);

    /**
     * Returns the list of phases that must complete before this phase can be run
     */
    ObjectList<Class<? extends Phase>> dependsOnPhases();

    /**
     * <p><b>Internal API</b></p>
     *
     * <p>
     * Called after this phase runs
     * </p>
     */
    void internalOnAfter();

    /**
     * <p><b>Internal API</b></p>
     *
     * <p>
     * Called before this phase runs
     * </p>
     */
    void internalOnBefore();

    /**
     * Runs the given code <i>when</i> this phase runs
     *
     * @param code The code to run
     */
    Phase onPhase(Runnable code);

    /**
     * <p><b>Internal API</b></p>
     *
     * <p>
     * Called when this phase runs
     * </p>
     */
    void internalOnRun();

    /**
     * Runs this phase
     */
    @Override
    default void run()
    {
        internalOnRun();
    }
}
