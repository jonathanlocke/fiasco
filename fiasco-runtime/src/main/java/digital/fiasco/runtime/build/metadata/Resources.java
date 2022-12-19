package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;

import java.net.URL;

@SuppressWarnings("unused")
public record Resources(@FormatProperty URL home,
                        @FormatProperty URL issues,
                        @FormatProperty URL code)
{
    public Resources()
    {
        this(null, null, null);
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public Resources withCode(URL code)
    {
        return new Resources(home, issues, code);
    }

    public Resources withHome(URL home)
    {
        return new Resources(home, issues, code);
    }

    public Resources withIssues(URL issues)
    {
        return new Resources(home, issues, code);
    }
}
