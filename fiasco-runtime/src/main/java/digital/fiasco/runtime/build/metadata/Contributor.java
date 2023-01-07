package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.naming.Named;

import java.time.ZoneId;

/**
 * Model for a project contributor
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public record Contributor(@FormatProperty String name,
                          @FormatProperty String nickname,
                          @FormatProperty String email,
                          @FormatProperty Organization organization,
                          @FormatProperty ZoneId timeZone,
                          @FormatProperty ObjectList<ProjectRole> roles) implements Named
{
    public static Contributor contributor(String name)
    {
        return new Contributor(name);
    }

    private Contributor(String name)
    {
        this(name, null, null, null, null, null);
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public Contributor withEmail(String email)
    {
        return new Contributor(name, nickname, email, organization, timeZone, roles.copy());
    }

    public Contributor withName(String name)
    {
        return new Contributor(name, nickname, email, organization, timeZone, roles.copy());
    }

    public Contributor withNickname(String nickname)
    {
        return new Contributor(name, nickname, email, organization, timeZone, roles.copy());
    }

    public Contributor withOrganization(Organization organization)
    {
        return new Contributor(name, nickname, email, organization, timeZone, roles.copy());
    }

    public Contributor withRole(ProjectRole role)
    {
        var copy = new Contributor(name, nickname, email, organization, timeZone, roles.copy());
        copy.roles.add(role);
        return copy;
    }

    public Contributor withRoles(ProjectRole... roles)
    {
        var copy = new Contributor(name, nickname, email, organization, timeZone, this.roles.copy());
        copy.roles.addAll(roles);
        return copy;
    }

    public Contributor withTimeZone(ZoneId timeZone)
    {
        return new Contributor(name, nickname, email, organization, timeZone, roles.copy());
    }

    public Contributor withTimeZone(String timeZone)
    {
        return new Contributor(name, nickname, email, organization, ZoneId.of(timeZone), roles.copy());
    }
}
