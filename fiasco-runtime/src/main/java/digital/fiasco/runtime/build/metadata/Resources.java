package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;

import java.net.URL;

/**
 * Locations of project resources
 *
 * @param home The project home page
 * @param issues The issue database for the project
 * @param sources The location of project source code
 */
@SuppressWarnings("unused")
public record Resources(@FormatProperty URL home,
                        @FormatProperty URL issues,
                        @FormatProperty URL sources)
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

    public Resources withHome(URL home)
    {
        return new Resources(home, issues, sources);
    }

    public Resources withIssues(URL issues)
    {
        return new Resources(home, issues, sources);
    }

    public Resources withSources(URL sources)
    {
        return new Resources(home, issues, sources);
    }
}
