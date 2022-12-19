package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.naming.Named;

import java.net.URL;

/**
 * Model for an organization managing a project
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public record Organization(@FormatProperty String name,
                           @FormatProperty URL url) implements Named
{
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
}
