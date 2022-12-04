package fiasco.metadata;

import com.telenav.kivakit.core.value.name.Name;

import java.net.MalformedURLException;
import java.net.URL;

import static com.telenav.kivakit.core.ensure.Ensure.fail;

@SuppressWarnings("unused")
public class Organization extends Name
{
    private URL url;

    public Organization(String name)
    {
        super(name);
    }

    public URL url()
    {
        return url;
    }

    public Organization withUrl(URL url)
    {
        this.url = url;
        return this;
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
