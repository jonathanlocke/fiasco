package digital.fiasco.runtime.build.phases;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * Maintains a list of phases in execution order.
 *
 * <p><b>Phases</b></p>
 *
 * <ul>
 *     <li>{@link #phase(String)}</li>
 *     <li>{@link #iterator()}</li>
 * </ul>
 *
 * <p><b>Phase Enable State</b></v></p>
 *
 * <ul>
 *     <li>{@link #disable(Phase)}</li>
 *     <li>{@link #enable(Phase)}</li>
 *     <li>{@link #isEnabled(Phase)}</li>
 * </ul>
 *
 * <p><b>Adding and Replacing Phases</b></p>
 *
 * <ul>
 *     <li>{@link #add(Phase)}</li>
 *     <li>{@link #addPhaseAfter(String, Phase)}</li>
 *     <li>{@link #addPhaseBefore(String, Phase)}</li>
 *     <li>{@link #replacePhase(String, Phase)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class PhaseList implements Phased
{
    /** All defined phases, in order of execution */
    private final ObjectList<Phase> phases = list();

    /** Enable state of each phase */
    private final ObjectMap<Class<? extends Phase>, Boolean> phaseEnabled = new ObjectMap<>();

    /** Maps from the name of a phase to the {@link Phase} object */
    private final ObjectMap<String, Phase> nameToPhase = new ObjectMap<>();

    /**
     * Adds the given phase to this list
     *
     * @param phase The phase
     * @return This phase list, for chaining
     */
    public PhaseList add(Phase phase)
    {
        var name = phase.name();
        ensure(nameToPhase.get(name) == null, "Phase with name '$' already exists", name);
        phases.add(phase);
        nameToPhase.put(name, phase);
        return this;
    }

    /**
     * Inserts the given phase after the named phase
     *
     * @param name The name of the phase to insert after
     * @param phase The phase to insert
     * @return This phase list, for chaining
     */
    public PhaseList addPhaseAfter(String name, Phase phase)
    {
        var at = phases.indexOf(phase(name));
        phases.add(at + 1, phase);
        nameToPhase.put(phase.name(), phase);
        return this;
    }

    /**
     * Inserts the given phase before the named phase
     *
     * @param name The name of the phase to insert before
     * @param phase The phase to insert
     * @return This phase list, for chaining
     */
    public PhaseList addPhaseBefore(String name, Phase phase)
    {
        var at = phases.indexOf(phase(name));
        phases.add(at, phase);
        nameToPhase.put(phase.name(), phase);
        return this;
    }

    /**
     * Returns a copy of this phase list
     */
    public PhaseList copy()
    {
        var copy = new PhaseList();
        for (var at : phases)
        {
            copy.add(at);
        }
        return copy;
    }

    /**
     * Disables the given phase from execution during a build
     *
     * @param phase The phase to disable
     */
    @Override
    public void disable(Phase phase)
    {
        phaseEnabled.put(phase.getClass(), false);
    }

    /**
     * Enables the given phase for execution during a build
     *
     * @param phase The phase to enable
     */
    @Override
    public void enable(Phase phase)
    {
        for (var at : phase.dependsOnPhases())
        {
            phaseEnabled.put(at, true);
        }
        phaseEnabled.put(phase.getClass(), true);
    }

    @Override
    public boolean isEnabled(Phase phase)
    {
        return phaseEnabled.getOrDefault(phase.getClass(), false);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<Phase> iterator()
    {
        return phases.iterator();
    }

    /**
     * Returns any phase with the given name
     *
     * @param name The name of the phase to look up
     * @return The phase, or null if no phase with the given name can be found
     */
    @Override
    public Phase phase(String name)
    {
        return nameToPhase.get(name);
    }

    /**
     * Replaces the named phase with a new definition
     *
     * @param name The name of the phase to replace
     * @param phase The phase to use instead
     * @return This phase list, for chaining
     */
    public PhaseList replacePhase(String name, Phase phase)
    {
        var at = phases.indexOf(phase(name));
        phases.set(at, phase);
        nameToPhase.put(phase.name(), phase);
        return this;
    }
}
