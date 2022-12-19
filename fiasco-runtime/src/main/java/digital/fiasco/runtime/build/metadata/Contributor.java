package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.naming.Named;

import java.time.ZoneId;

/**
 * Model for a project contributor
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public record Contributor(@FormatProperty String name,
                          @FormatProperty String email,
                          @FormatProperty Organization organization,
                          @FormatProperty ZoneId timeZone,
                          @FormatProperty StringList roles) implements Named
{
    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public Contributor withEmail(String email)
    {
        return new Contributor(name, email, organization, timeZone, roles.copy());
    }

    public Contributor withName(String name)
    {
        return new Contributor(name, email, organization, timeZone, roles.copy());
    }

    public Contributor withOrganization(Organization organization)
    {
        return new Contributor(name, email, organization, timeZone, roles.copy());
    }

    public Contributor withRole(String role)
    {
        var copy = new Contributor(name, email, organization, timeZone, roles.copy());
        copy.roles.add(role);
        return copy;
    }

    public Contributor withTimeZone(ZoneId timeZone)
    {
        return new Contributor(name, email, organization, timeZone, roles.copy());
    }

    public Contributor withTimeZone(String timeZone)
    {
        return new Contributor(name, email, organization, ZoneId.of(timeZone), roles.copy());
    }
}
