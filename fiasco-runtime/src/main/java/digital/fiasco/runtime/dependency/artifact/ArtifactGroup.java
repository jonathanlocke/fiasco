package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.interfaces.naming.Named;

import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.artifactDescriptor;

/**
 * The group for an artifact, where an artifact desriptor is [group:artifact:version].
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #name()}</li>
 * </ul>
 *
 * <p><b>Group Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #asset(String)}</li>
 *     <li>{@link #library(String)}</li>
 *     <li>{@link #descriptor(ArtifactIdentifier)}</li>
 * </ul>
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

    public Asset asset(String artifact)
    {
        return Asset.asset(artifactDescriptor(name + ":" + artifact));
    }

    /**
     * Returns an artifact descriptor for the given artifact in this group
     *
     * @param artifact The identifier
     * @return The artifact descriptor
     */
    public ArtifactDescriptor descriptor(ArtifactIdentifier artifact)
    {
        return new ArtifactDescriptor(this, artifact, null);
    }

    public Library library(String artifact)
    {
        return Library.library(artifactDescriptor(name + ":" + artifact));
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
