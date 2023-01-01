package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.string.FormatProperty;
import digital.fiasco.runtime.dependency.artifact.Artifact;

/**
 * The metadata for a build.
 *
 * @param description A description of the project being built
 * @param artifactType The type of artifact that is built
 * @param organization The organization responsible for the artifact
 * @param copyright Any copyright
 * @param license Any license
 * @param resources Locations of resources for developers
 * @param contributors The project contributors
 */
@SuppressWarnings("unused")
public record BuildMetadata(@FormatProperty String description,
                            @FormatProperty Class<? extends Artifact> artifactType,
                            @FormatProperty Organization organization,
                            @FormatProperty Copyright copyright,
                            @FormatProperty License license,
                            @FormatProperty Resources resources,
                            @FormatProperty ObjectList<Contributor> contributors)
{
    public BuildMetadata()
    {
        this(null, null, null, null, null, null, null);
    }

    public BuildMetadata withContributors(ObjectList<Contributor> contributors)
    {
        return new BuildMetadata(description, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withCopyright(Copyright copyright)
    {
        return new BuildMetadata(description, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withDescription(String description)
    {
        return new BuildMetadata(description, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withLicense(License license)
    {
        return new BuildMetadata(description, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withOrganization(Organization organization)
    {
        return new BuildMetadata(description, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withResources(Resources resources)
    {
        return new BuildMetadata(description, artifactType, organization, copyright, license, resources, contributors);
    }
}
