package fiasco.metadata;

import com.telenav.kivakit.interfaces.naming.Named;

import java.net.MalformedURLException;
import java.net.URL;

import static com.telenav.kivakit.core.ensure.Ensure.fail;

@SuppressWarnings("unused")
public class Organization implements Named
{
    public static Organization organization(String name)
    {
        return new Organization(name);
    }

    private final String name;

    private URL url;

    private Organization(String name)
    {
        this.name = name;
    }

    private Organization(Organization that)
    {
        this.name = that.name;
        this.url = that.url;
    }

    public Organization copy()
    {
        return new Organization(this);
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
