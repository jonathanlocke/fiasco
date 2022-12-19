package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;

import java.net.MalformedURLException;
import java.net.URL;

import static com.telenav.kivakit.core.ensure.Ensure.illegalArgument;

public record License(@FormatProperty String name,
                      @FormatProperty URL url,
                      @FormatProperty String description)
{
    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public License withDescription(String description)
    {
        return new License(name, url, description);
    }

    public License withName(String name)
    {
        return new License(name, url, description);
    }

    public License withUrl(String url)
    {
        try
        {
            return new License(name, new URL(url), description);
        }
        catch (MalformedURLException e)
        {
            return illegalArgument("Invalid URL");
        }
    }

    public License withUrl(URL url)
    {
        return new License(name, url, description);
    }
}
