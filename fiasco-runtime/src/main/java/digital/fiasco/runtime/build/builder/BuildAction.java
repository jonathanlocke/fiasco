package digital.fiasco.runtime.build.builder;

/**
 * Interface to code that can perform an action before, during or after a phase executes. The {@link Builder} parameter
 * allows construction of tools associated with the builder.
 *
 * @author Jonathan Locke
 */
@FunctionalInterface
public interface BuildAction
{
    /**
     * Executes this action
     *
     * @param builder The builder that is executing the action
     */
    void action(Builder builder);
}
