package fiasco;

import com.telenav.kivakit.interfaces.naming.Named;
import fiasco.metadata.Contributor;
import fiasco.metadata.Copyright;
import fiasco.metadata.Organization;

import java.util.ArrayList;
import java.util.List;

/**
 * Data about a project, including its name, the organization that develops it and a copyright.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class BuildMetadata implements Named
{
    public static BuildMetadata metadata(String artifact)
    {
        return new BuildMetadata(artifact);
    }

    private List<Contributor> contributors;

    private Copyright copyright;

    private Organization organization;

    protected BuildMetadata(BuildMetadata that)
    {
        copyright = that.copyright;
        contributors = new ArrayList<>(that.contributors);
        organization = that.organization;
    }

    private BuildMetadata(String artifact)
    {
    }

    public List<Contributor> contributors()
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

    public BuildMetadata withContributor(Contributor contributor)
    {
        var copy = new BuildMetadata(this);
        copy.contributors.add(contributor);
        return copy;
    }

    public BuildMetadata withCopyright(String copyright)
    {
        var copy = new BuildMetadata(this);
        copy.copyright = new Copyright(copyright);
        return copy;
    }

    public BuildMetadata withOrganization(Organization organization)
    {
        var copy = new BuildMetadata(this);
        copy.organization = organization;
        return copy;
    }
}
