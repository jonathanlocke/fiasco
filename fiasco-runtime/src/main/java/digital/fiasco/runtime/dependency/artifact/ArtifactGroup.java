package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.naming.Named;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static digital.fiasco.runtime.dependency.artifact.ArtifactName.artifactName;

/**
 * The group for an artifact, where an artifact desriptor is [type:group:artifact:version].
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #group(String)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #name()}</li>
 * </ul>
 *
 * <p><b>Child Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #artifact(Class, ArtifactName)}</li>
 *     <li>{@link #asset(String)}</li>
 *     <li>{@link #asset(ArtifactName)}</li>
 *     <li>{@link #library(String)}</li>
 *     <li>{@link #library(ArtifactName)}</li>
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
     * @param name The artifact name
     * @return The artifact descriptor
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor artifact(Class<? extends Artifact<?>> type, ArtifactName name)
    {
        return new ArtifactDescriptor(type, this, name, null);
    }

    /**
     * Returns an artifact descriptor for the given artifact in this group
     *
     * @param name The artifact name
     * @return The artifact descriptor
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor asset(ArtifactName name)
    {
        return artifact(Library.class, name);
    }

    /**
     * Returns an artifact descriptor for the given artifact in this group
     *
     * @param name The artifact name
     * @return The artifact descriptor
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor asset(String name)
    {
        return asset(artifactName(name));
    }

    /**
     * Returns an artifact descriptor for the given artifact in this group
     *
     * @param artifact The artifact name
     * @return The artifact descriptor
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor library(String artifact)
    {
        return library(artifactName(artifact));
    }

    /**
     * Returns an artifact descriptor for the given artifact in this group
     *
     * @param name The artifact name
     * @return The artifact descriptor
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor library(ArtifactName name)
    {
        return artifact(Library.class, name);
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
