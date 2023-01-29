package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.messages.status.activity.Step;
import com.telenav.kivakit.interfaces.code.Code;

/**
 * Adds the ability to describe steps in a build or in the invocation of a tool, either in place of executing the step
 * (dry run), or in addition to executing it (verbose)
 *
 * @author Jonathan Locke
 */
public interface Stepped extends Repeater
{
    /**
     * Returns true if steps should describe what they would do rather than actually doing it
     *
     * @return True if steps should be described
     */
    boolean shouldDescribe();

    /**
     * Returns true if steps should be described as well as executed
     *
     * @return True if steps should be described and executed
     */
    boolean shouldDescribeAndExecute();

    /**
     * Returns true if steps should be executed
     *
     * @return True if steps should be executed
     */
    default boolean shouldExecute()
    {
        return !shouldDescribe();
    }

    /**
     * Executes the given code if
     *
     * @param code The code to run
     * @return The result of the operation, or null if the operation was not executed
     */
    default <T> T step(Code<T> code, String message, Object... arguments)
    {
        if (shouldDescribe() || shouldDescribeAndExecute())
        {
            step(message, arguments);
        }
        if (shouldExecute())
        {
            return code.run();
        }
        return null;
    }

    /**
     * Broadcasts a {@link Step} message
     *
     * @param message The message
     * @param arguments The message arguments
     */
    default void step(String message, Object... arguments)
    {
        transmit(new Step(message, arguments));
    }

    /**
     * Executes the given code if
     *
     * @param code The code to run
     */
    default void step(Runnable code, String message, Object... arguments)
    {
        if (shouldDescribe() || shouldDescribeAndExecute())
        {
            step(message, arguments);
        }
        if (shouldExecute())
        {
            code.run();
        }
    }
}
