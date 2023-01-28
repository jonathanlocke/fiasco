package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.string.FormatProperty;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * The metadata for a build.
 *
 * @param descriptor The type of artifact that is built
 * @param description A description of the project being built
 * @param copyrights Any copyright
 * @param licenses Any license
 * @param resources Locations of resources for developers
 * @param organizations The organization responsible for the artifact
 * @param contributors The project contributors
 */
@SuppressWarnings("unused")
public record BuildMetadata(@FormatProperty ArtifactDescriptor descriptor,
                            @FormatProperty String description,
                            @FormatProperty ObjectList<Copyright> copyrights,
                            @FormatProperty ObjectList<License> licenses,
                            @FormatProperty ObjectList<ProjectResource> resources,
                            @FormatProperty ObjectList<MailingList> mailingLists,
                            @FormatProperty ObjectList<Organization> organizations,
                            @FormatProperty ObjectList<Contributor> contributors)
{
    public static BuildMetadata buildMetadata()
    {
        return new BuildMetadata(null, null, list(), list(), list(), list(), list(), list());
    }

    public BuildMetadata withArtifactDescriptor(String descriptor)
    {
        return withArtifactDescriptor(ArtifactDescriptor.descriptor(descriptor));
    }

    public BuildMetadata withArtifactDescriptor(ArtifactDescriptor descriptor)
    {
        return new BuildMetadata(descriptor, description, copyrights, licenses, resources, mailingLists, organizations, contributors);
    }

    public BuildMetadata withContributor(Contributor contributor)
    {
        return withContributors(contributor);
    }

    public BuildMetadata withContributors(Contributor... contributors)
    {
        return new BuildMetadata(descriptor, description, copyrights, licenses, resources, mailingLists, organizations, this.contributors.with(contributors));
    }

    public BuildMetadata withCopyright(Copyright copyright)
    {
        return withCopyrights(copyright);
    }

    public BuildMetadata withCopyrights(Copyright... copyrights)
    {
        return new BuildMetadata(descriptor, description, this.copyrights.with(copyrights), licenses, resources, mailingLists, organizations, contributors);
    }

    public BuildMetadata withDescription(String description)
    {
        return new BuildMetadata(descriptor, description, copyrights, licenses, resources, mailingLists, organizations, contributors);
    }

    public BuildMetadata withLicense(License license)
    {
        return withLicenses(license);
    }

    public BuildMetadata withLicenses(License... licenses)
    {
        return new BuildMetadata(descriptor, description, copyrights, this.licenses.with(licenses), resources, mailingLists, organizations, contributors);
    }

    public BuildMetadata withMailingLists(MailingList... mailingLists)
    {
        return new BuildMetadata(descriptor, description, copyrights, licenses, resources, this.mailingLists.with(mailingLists), organizations, contributors);
    }

    public BuildMetadata withOrganization(Organization organization)
    {
        return withOrganizations(organization);
    }

    public BuildMetadata withOrganizations(Organization... organizations)
    {
        return new BuildMetadata(descriptor, description, copyrights, licenses, resources, mailingLists, this.organizations.with(organizations), contributors);
    }

    public BuildMetadata withProjectResource(ProjectResource resource)
    {
        return new BuildMetadata(descriptor, description, copyrights, licenses, resources, mailingLists, organizations, contributors);
    }

    public BuildMetadata withProjectResources(ProjectResource... resources)
    {
        return new BuildMetadata(descriptor, description, copyrights, licenses, this.resources.with(resources), mailingLists, organizations, contributors);
    }
}
