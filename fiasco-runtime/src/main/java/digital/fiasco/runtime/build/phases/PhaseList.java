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
 * @author Jonathan Locke
 */
public class PhaseList implements Iterable<Phase>
{
    /** All defined phases, in order of execution */
    private final ObjectList<Phase> phases = list();

    /** Maps from the name of a phase to the {@link Phase} object */
    private final ObjectMap<String, Phase> nameToPhase = new ObjectMap<>();

    /**
     * Adds the given phase to this list
     *
     * @param phase The phase
     */
    public void add(Phase phase)
    {
        var name = phase.name();
        ensure(nameToPhase.get(name) == null, "Phase with name '$' already exists", name);
        phases.add(phase);
        nameToPhase.put(name, phase);
    }

    /**
     * Inserts the given phase after the named phase
     *
     * @param name The name of the phase to insert after
     * @param phase The phase to insert
     */
    public void addPhaseAfter(String name, Phase phase)
    {
        var at = phases.indexOf(phase(name));
        phases.add(at + 1, phase);
        nameToPhase.put(phase.name(), phase);
    }

    /**
     * Inserts the given phase before the named phase
     *
     * @param name The name of the phase to insert before
     * @param phase The phase to insert
     */
    public void addPhaseBefore(String name, Phase phase)
    {
        var at = phases.indexOf(phase(name));
        phases.add(at, phase);
        nameToPhase.put(phase.name(), phase);
    }

    /**
     * Returns a copy of this phase list
     */
    public PhaseList copy()
    {
        var copy = new PhaseList();
        for (var at : phases())
        {
            copy.add(at);
        }
        return copy;
    }

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
    public Phase phase(String name)
    {
        return nameToPhase.get(name);
    }

    /**
     * Returns the set of all defined phases
     */
    public ObjectList<Phase> phases()
    {
        return phases;
    }
}
