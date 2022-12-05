package fiasco.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.naming.Named;

import java.net.MalformedURLException;
import java.net.URL;

import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * Model for an organization managing a project
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Organization implements Named
{
    @FormatProperty
    private final String name;

    @FormatProperty
    private URL url;

    /**
     * Creates an {@link Organization} with the given name
     *
     * @param name The name of the organization
     */
    public Organization(String name)
    {
        this.name = name;
    }

    protected Organization(Organization that)
    {
        this.name = that.name;
        this.url = that.url;
    }

    public Organization copy()
    {
        return new Organization(this);
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public URL url()
    {
        return url;
    }

    public Organization withUrl(URL url)
    {
        var copy = copy();
        copy.url = url;
        return copy;
    }

    public Organization withUrl(String url)
    {
        try
        {
            return withUrl(new URL(url));
        }
        catch (MalformedURLException e)
        {
            return fail(e, "Invalid URL: $", url);
        }
    }
}
