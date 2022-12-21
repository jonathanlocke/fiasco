package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.interfaces.naming.Named;

/**
 * The group for an artifact, where an artifact desriptor is group:identifier:version.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public record ArtifactGroup(String name) implements Named
{
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
