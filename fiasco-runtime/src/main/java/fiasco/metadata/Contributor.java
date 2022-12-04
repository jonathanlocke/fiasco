package fiasco.metadata;

import com.telenav.kivakit.core.value.name.Name;

@SuppressWarnings("unused")
public class Contributor extends Name
{
    private String email;

    private String role;

    public Contributor(String name)
    {
        super(name);
    }

    public String email()
    {
        return email;
    }

    public String role()
    {
        return role;
    }

    public Contributor withEmail(String email)
    {
        this.email = email;
        return this;
    }

    public Contributor withRole(String role)
    {
        this.role = role;
        return this;
    }
}
