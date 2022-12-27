package digital.fiasco.runtime.build.phases;

/**
 * Allows access to build phases
 *
 * <p><b>Phases</b></p>
 *
 * <ul>
 *     <li>{@link #phase(String)}</li>
 * </ul>
 *
 * <p><b>Phase Enable State</b></p>
 *
 * <ul>
 *     <li>{@link #disable(Phase)}</li>
 *     <li>{@link #enable(Phase)}</li>
 *     <li>{@link #isEnabled(Phase)}</li>
 * </ul>
 *
 * @author Joonathan Locke
 */
public interface Phased extends Iterable<Phase>
{
    /**
     * Disables execution of the given phase
     *
     * @param phase The phase
     */
    void disable(Phase phase);

    /**
     * Enables execution of the given phase
     *
     * @param phase The phase
     */
    void enable(Phase phase);

    /**
     * Returns true if the given phase is enabled
     *
     * @param phase The phase
     * @return The enable state
     */
    boolean isEnabled(Phase phase);

    /**
     * Returns the phase with the given name
     *
     * @param name The phase name to look up
     * @return The phase, or null if no phase can be found with the given name
     */
    Phase phase(String name);
}
