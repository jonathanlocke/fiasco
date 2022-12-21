package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.string.FormatProperty;
import digital.fiasco.runtime.build.metadata.Contributor;
import digital.fiasco.runtime.build.metadata.Copyright;
import digital.fiasco.runtime.build.metadata.License;
import digital.fiasco.runtime.build.metadata.Organization;
import digital.fiasco.runtime.build.metadata.Resources;
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
public record BuildMetadata(@FormatProperty ArtifactDescriptor artifactDescriptor,
                            @FormatProperty ArtifactType artifactType,
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

    public BuildMetadata withArtifactDescriptor(String descriptor)
    {
        return withArtifactDescriptor(parseArtifactDescriptor(throwingListener(), descriptor));
    }

    public BuildMetadata withArtifactDescriptor(ArtifactDescriptor artifact)
    {
        return new BuildMetadata(artifact, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withContributors(ObjectList<Contributor> contributors)
    {
        return new BuildMetadata(artifactDescriptor, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withCopyright(Copyright copyright)
    {
        return new BuildMetadata(artifactDescriptor, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withLicense(License license)
    {
        return new BuildMetadata(artifactDescriptor, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withOrganization(Organization organization)
    {
        return new BuildMetadata(artifactDescriptor, artifactType, organization, copyright, license, resources, contributors);
    }

    public BuildMetadata withResources(Resources resources)
    {
        return new BuildMetadata(artifactDescriptor, artifactType, organization, copyright, license, resources, contributors);
    }
}
