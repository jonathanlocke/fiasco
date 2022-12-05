package fiasco.metadata;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.interfaces.naming.Named;

@SuppressWarnings("unused")
public class Contributor implements Named
{
    public static Contributor contributor(String name)
    {
        return new Contributor(name);
    }

    private String name;

    private String email;

    private StringList roles;

    private Contributor(String name)
    {
        this.name = name;
    }

    private Contributor(Contributor that)
    {
        this.email = that.email;
        this.name = that.name;
        this.roles = that.roles.copy();
    }

    public Contributor copy()
    {
        return new Contributor(this);
    }

    public String email()
    {
        return email;
    }

    @Override
    public String name()
    {
        return name;
    }

    public StringList role()
    {
        return roles;
    }

    public Contributor withEmail(String email)
    {
        var copy = copy();
        copy.email = email;
        return copy;
    }

    public Contributor withName(String name)
    {
        var copy = copy();
        copy.name = name;
        return copy;
    }

    public Contributor withRole(String role)
    {
        var copy = copy();
        copy.roles.add(role);
        return copy;
    }
}
