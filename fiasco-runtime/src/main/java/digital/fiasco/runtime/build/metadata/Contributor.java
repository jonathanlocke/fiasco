package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.naming.Named;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;

/**
 * Model for a project contributor
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Contributor implements Named
{
    @FormatProperty
    private String name;

    @FormatProperty
    private String email;

    @FormatProperty
    private StringList roles = stringList();

    /**
     * Creates a {@link Contributor} with the given name
     *
     * @param name The name of the contributor
     */
    public Contributor(String name)
    {
        this.name = name;
    }

    protected Contributor(Contributor that)
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

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
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
