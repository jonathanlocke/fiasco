package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;

import java.net.URL;

public record Issues(@FormatProperty URL issueTracker)
{
    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
