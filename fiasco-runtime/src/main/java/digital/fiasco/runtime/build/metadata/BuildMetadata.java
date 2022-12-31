package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.string.FormatProperty;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactType;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.parseArtifactDescriptor;

/**
 * The metadata for a build.
 *
 * @param artifactDescriptor The main artifact produced by the build
 * @param artifactType The type of artifact that is built
 * @param organization The organization responsible for the artifact
 * @param copyright Any copyright
 * @param license Any license
 * @param resources Locations of resources for developers
 * @param contributors The project contributors
 */
@SuppressWarnings("unused")
public record BuildMetadata(@FormatProperty ArtifactDescriptor artifactDescriptor,
                            @FormatProperty String description,
                            @FormatProperty ArtifactType artifactType,
                            @FormatProperty Organization organization,
                            @FormatProperty Copyright copyright,
                            @FormatProperty License license,
                            @FormatProperty Resources resources,
                            @FormatProperty ObjectList<Contributor> contributors)
{
    public BuildMetadata()
    {
        this(null, null, null, null, null, null, null, null);
    }

    public BuildMetadata withArtifactDescriptor(String descriptor)
    {
        return withArtifactDescriptor(parseArtifactDescriptor(throwingListener(), descriptor));
    }

    public BuildMetadata withArtifactDescriptor(ArtifactDescriptor artifact)
    {
        return new BuildMetadata(artifact, description, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withContributors(ObjectList<Contributor> contributors)
    {
        return new BuildMetadata(artifactDescriptor, description, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withCopyright(Copyright copyright)
    {
        return new BuildMetadata(artifactDescriptor, description, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withDescription(String description)
    {
        return new BuildMetadata(artifactDescriptor, description, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withLicense(License license)
    {
        return new BuildMetadata(artifactDescriptor, description, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withOrganization(Organization organization)
    {
        return new BuildMetadata(artifactDescriptor, description, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withResources(Resources resources)
    {
        return new BuildMetadata(artifactDescriptor, description, artifactType, organization, copyright, license, resources, contributors);
    }
}
