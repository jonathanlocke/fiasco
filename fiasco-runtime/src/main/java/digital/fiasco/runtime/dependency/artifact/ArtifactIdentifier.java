package digital.fiasco.runtime.dependency.artifact;

/**
 * Identifies an artifact within a {@link ArtifactGroup}
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public record ArtifactIdentifier(String identifier) implements Comparable<ArtifactIdentifier>
{
    public static ArtifactIdentifier artifact(String identifier)
    {
        return new ArtifactIdentifier(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(ArtifactIdentifier that)
    {
        return this.identifier.compareTo(that.identifier);
    }

    @Override
    public String toString()
    {
        return identifier;
    }
}
