package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.interfaces.naming.Named;

import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptor;

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
 *     <li>{@link #artifact(ArtifactIdentifier)}</li>
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

    /**
     * Returns an artifact descriptor for the given artifact in this group
     *
     * @param artifact The identifier
     * @return The artifact descriptor
     */
    public ArtifactDescriptor artifact(ArtifactIdentifier artifact)
    {
        return new ArtifactDescriptor(this, artifact, null);
    }

    /**
     * Returns an artifact descriptor for the given artifact in this group
     *
     * @param artifact The identifier
     * @return The artifact descriptor
     */
    public ArtifactDescriptor artifact(String artifact)
    {
        return artifact(ArtifactIdentifier.artifact(artifact));
    }

    /**
     * Returns the given asset in this group
     *
     * @param asset The asset identifier
     * @return The asset
     */
    public Asset asset(String asset)
    {
        return Asset.asset(descriptor(name + ":" + asset + ":"));
    }

    /**
     * Returns the given library in this group
     *
     * @param library The library identifier
     * @return The library
     */
    public Library library(String library)
    {
        return Library.library(descriptor(name + ":" + library + ":"));
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
