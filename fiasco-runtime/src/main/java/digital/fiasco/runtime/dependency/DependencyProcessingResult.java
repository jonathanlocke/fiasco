package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.value.count.Count;

import static com.telenav.kivakit.core.os.Console.console;

/**
 * Holds the result of a build. Any warning or problem messages are contained in the {@link MessageList} returned by
 * {@link #issues()}. The number of problems and warnings can be retrieved with {@link #problems()} and
 * {@link #warnings()}, respectively. Statistics for the result can be retrieved by calling {@link #toString()}, or
 * shown on the console with {@link #showResult()}.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unchecked", "unused", "ClassCanBeRecord" })
public class DependencyProcessingResult
{
    public static DependencyProcessingResult dependencyProcessingResult(Dependency dependency, MessageList issues)
    {
        return new DependencyProcessingResult(dependency, issues);
    }

    /** The dependency that was processed */
    private final Dependency dependency;

    /** The list of issues encountered during the build */
    private final MessageList issues;

    private DependencyProcessingResult(Dependency dependency, MessageList issues)
    {
        this.issues = issues;
        this.dependency = dependency;
    }

    /**
     * Returns the messages captured during the build
     */
    public MessageList issues()
    {
        return issues;
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
        var statistics = issues.statistics(Problem.class, Warning.class, Quibble.class);
        return statistics.titledBox("Build Results");
    }

    /**
     * Returns the number of warnings during the build
     */
    public Count warnings()
    {
        return issues.count(Warning.class);
    }
}
