package digital.fiasco.runtime.dependency.artifact;

/**
 * Identifies an artifact within a {@link ArtifactGroup}
 */
@SuppressWarnings("unused")
public record ArtifactIdentifier(String identifier)
{
    public int compareTo(ArtifactIdentifier that)
    {
        return this.identifier.compareTo(that.identifier);
    }
}
