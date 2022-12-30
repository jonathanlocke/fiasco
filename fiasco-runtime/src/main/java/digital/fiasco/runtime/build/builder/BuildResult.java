package digital.fiasco.runtime.build.builder;

import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.value.count.Count;

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

    public Count warnings()
    {
        return issues.count(Warning.class);
    }
}
