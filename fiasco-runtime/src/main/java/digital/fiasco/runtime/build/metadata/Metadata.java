package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.naming.Named;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * Data about a project, including its name, the organization that develops it and a copyright.
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Metadata implements Named
{
    @FormatProperty
    private ObjectList<Contributor> contributors = list();

    @FormatProperty
    private Copyright copyright;

    @FormatProperty
    private Organization organization;

    /**
     * Creates a {@link Metadata} object for the given artifact specifier
     *
     * @param artifact The artifact specifier in group:artifact:version format
     */
    public Metadata(String artifact)
    {
    }

    protected Metadata(Metadata that)
    {
        copyright = that.copyright;
        contributors = list(that.contributors);
        organization = that.organization;
    }

    public ObjectList<Contributor> contributors()
    {
        return contributors;
    }

    public Copyright copyright()
    {
        return copyright;
    }

    public Organization organization()
    {
        return organization;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public Metadata withContributor(Contributor contributor)
    {
        var copy = new Metadata(this);
        copy.contributors.add(contributor);
        return copy;
    }

    public Metadata withCopyright(Copyright copyright)
    {
        var copy = new Metadata(this);
        copy.copyright = copyright;
        return copy;
    }

    public Metadata withOrganization(Organization organization)
    {
        var copy = new Metadata(this);
        copy.organization = organization;
        return copy;
    }
}
