package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.naming.Named;
import digital.fiasco.runtime.build.metadata.Contributor;
import digital.fiasco.runtime.build.metadata.Copyright;
import digital.fiasco.runtime.build.metadata.Organization;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * Data about a project, including its name, the organization that develops it and a copyright.
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class BuildMetadata implements Named
{
    @FormatProperty
    private ObjectList<Contributor> contributors = list();

    @FormatProperty
    private Copyright copyright;

    @FormatProperty
    private Organization organization;

    public BuildMetadata()
    {
    }

    protected BuildMetadata(BuildMetadata that)
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

    public BuildMetadata withContributor(Contributor contributor)
    {
        var copy = copy();
        copy.contributors.add(contributor);
        return copy;
    }

    public BuildMetadata withCopyright(Copyright copyright)
    {
        var copy = copy();
        copy.copyright = copyright;
        return copy;
    }

    public BuildMetadata withOrganization(Organization organization)
    {
        var copy = copy();
        copy.organization = organization;
        return copy;
    }

    @NotNull
    public BuildMetadata copy()
    {
        return new BuildMetadata(this);
    }
}
