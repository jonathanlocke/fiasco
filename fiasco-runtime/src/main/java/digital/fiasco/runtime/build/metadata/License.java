package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;

import java.net.MalformedURLException;
import java.net.URL;

import static com.telenav.kivakit.core.ensure.Ensure.illegalArgument;

/**
 * A software license for a project
 *
 * @param name The name of the license
 * @param url A URL to the full license text
 * @param description A description of the license
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public record License(@FormatProperty String name,
                      @FormatProperty URL url,
                      @FormatProperty String description)
{
    public static final License APACHE_LICENSE = new License("Apache License 2.0")
            .withDescription("Licensed under Apache License, Version 2.0")
            .withUrl("https://www.apache.org/licenses/LICENSE-2.0");

    public License(String name)
    {
        this(name, null, null);
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public License withDescription(String description)
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
