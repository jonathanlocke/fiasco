package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_PUBLIC;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;

/**
 * The name of an artifact within an {@link ArtifactGroup}
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
@TypeQuality
    (
        audience = AUDIENCE_PUBLIC,
        documentation = DOCUMENTATION_COMPLETE,
        testing = TESTED,
        stability = STABLE
    )
public record ArtifactName(String name) implements Comparable<ArtifactName>
{
    /**
     * Creates an {@link ArtifactName} for the given name
     *
     * @param name The artifact name
     * @return The new instance of {@link ArtifactName}
     */
    @MethodQuality
        (
            documentation = DOCUMENTATION_COMPLETE,
            testing = TESTED
        )
    public static ArtifactName artifact(String name)
    {
        return new ArtifactName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality
        (
            documentation = DOCUMENTATION_COMPLETE,
            testing = TESTED
        )
    public int compareTo(ArtifactName that)
    {
        return this.name.compareTo(that.name);
    }

    @Override
    @MethodQuality
        (
            documentation = DOCUMENTATION_COMPLETE,
            testing = TESTED
        )
    public String toString()
    {
        return name;
    }
}
