package digital.fiasco.runtime.build;

import digital.fiasco.runtime.build.tools.builder.Builder;

/**
 * An object that can be built.
 *
 * <p><b>Build Tools</b></p>
 *
 * <ul>
 *     <li>{@link #builder()}</li>
 * </ul>
 *
 * <p><b>Build Options</b></p>
 *
 * <ul>
 *     <li>{@link #disable(BuildOption)}</li>
 *     <li>{@link #enable(BuildOption)}</li>
 *     <li>{@link #isEnabled(BuildOption)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
public interface Buildable
{
    /**
     * Returns the {@link Builder} for this build
     */
    Builder builder();

    /**
     * Disables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    Build disable(BuildOption option);

    /**
     * Enables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    Build enable(BuildOption option);

    /**
     * Returns true if the given option is enabled
     *
     * @param option The option
     * @return True if the option is enabled
     */
    boolean isEnabled(BuildOption option);
}
