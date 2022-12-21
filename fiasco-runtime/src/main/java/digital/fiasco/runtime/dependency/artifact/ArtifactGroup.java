package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.interfaces.naming.Named;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public record ArtifactGroup(String name) implements Named
{
    public ArtifactDescriptor artifact(ArtifactIdentifier identifier)
    {
        return new ArtifactDescriptor(this, identifier, null);
    }

    public String toString()
    {
        return name;
    }

    @Override
    public String name()
    {
        return name;
    }
}
