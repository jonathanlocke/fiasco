package digital.fiasco.runtime.dependency.processing;

import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.value.count.Count;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.os.Console.console;

/**
 * Holds the result of a task execution. Any warning or problem messages are contained in the {@link MessageList}
 * returned by {@link #issues()}. The number of problems and warnings can be retrieved with {@link #problems()} and
 * {@link #warnings()}, respectively. Statistics for the result can be retrieved by calling {@link #toString()}, or
 * shown on the console with {@link #showResult()}.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unchecked", "unused" })
public class TaskResult<T>
{
    /**
     * Creates a result for the processing of some object
     *
     * @param object The object
     * @param issues Any issues that occurred while processing the dependency
     * @return The result
     */
    public static <T> TaskResult<T> taskResult(T object, MessageList issues)
    {
        return new TaskResult<>(object, issues);
    }

    /** The dependency that was processed */
    private final T object;

    /** The list of issues encountered during the build */
    private final MessageList issues;

    private TaskResult(T object, MessageList issues)
    {
        this.issues = issues;
        this.object = object;
    }

    /**
     * Returns the messages captured during the build
     */
    public MessageList issues()
    {
        return issues;
    }

    public T object()
    {
        return object;
    }

    /**
     * Returns the number of problems (or greater) during the build
     */
    public Count problems()
    {
        return issues.countWorseThanOrEqualTo(Problem.class);
    }

    /**
     * Shows build statistics on the console
     */
    public void showResult()
    {
        console().println(toString());
    }

    @Override
    public String toString()
    {
        return stringList()
            .appending(object.toString())
            .appending(issues.statistics(Problem.class, Warning.class, Quibble.class))
            .appending(issues.formatted())
            .titledBox("Task Failed");
    }

    /**
     * Returns the number of warnings during the build
     */
    public Count warnings()
    {
        return issues.count(Warning.class);
    }
}
