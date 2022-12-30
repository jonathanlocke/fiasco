package digital.fiasco.runtime.build.builder;

import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.value.count.Count;

import static com.telenav.kivakit.core.os.Console.console;

@SuppressWarnings("unchecked")
public class BuildResult
{
    private final MessageList issues;

    public BuildResult(MessageList issues)
    {
        this.issues = issues;
    }

    public MessageList messages()
    {
        return issues;
    }

    public Count problems()
    {
        return issues.countWorseThanOrEqualTo(Problem.class);
    }

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

    public Count warnings()
    {
        return issues.count(Warning.class);
    }
}
