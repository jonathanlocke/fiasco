package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.naming.Named;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_PUBLIC;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
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
 *     <li>{@link #artifact(ArtifactName)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public record ArtifactGroup(String name) implements Named
{
    /**
     * Creates a new group with the given name
     *
     * @param name The name of the group
     * @return The new group instance
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static ArtifactGroup group(String name)
    {
        return new ArtifactGroup(name);
    }

    /**
     * Returns an artifact descriptor for the given artifact in this group
     *
     * @param artifact The artifact name
     * @return The artifact descriptor
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor artifact(ArtifactName artifact)
    {
        return new ArtifactDescriptor(this, artifact, null);
    }

    /**
     * Returns an artifact descriptor for the given artifact in this group
     *
     * @param artifact The artifact name
     * @return The artifact descriptor
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor artifact(String artifact)
    {
        return artifact(ArtifactName.artifact(artifact));
    }

    /**
     * Returns the given asset in this group
     *
     * @param asset The asset name
     * @return The asset
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public Asset asset(String asset)
    {
        return Asset.asset(descriptor(name + ":" + asset + ":"));
    }

    /**
     * Returns the given library in this group
     *
     * @param library The library name
     * @return The library
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public Library library(String library)
    {
        return Library.library(descriptor(name + ":" + library + ":"));
    }

    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    @Override
    public String name()
    {
        return name;
    }

    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    @Override
    public String toString()
    {
        return name;
    }
}
