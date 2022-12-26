package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.interfaces.naming.Named;

import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.artifactDescriptor;

/**
 * The group for an artifact, where an artifact desriptor is group:identifier:version.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public record ArtifactGroup(String name) implements Named
{
    public static ArtifactGroup group(String name)
    {
        return new ArtifactGroup(name);
    }

    /**
     * Returns an artifact descriptor for this group with the given identifier
     *
     * @param identifier The identifier
     * @return The artifact descriptor
     */
    public ArtifactDescriptor descriptor(ArtifactIdentifier identifier)
    {
        return new ArtifactDescriptor(this, identifier, null);
    }

    public Library library(String identifier)
    {
        return Library.library(artifactDescriptor(name + ":" + identifier));
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
