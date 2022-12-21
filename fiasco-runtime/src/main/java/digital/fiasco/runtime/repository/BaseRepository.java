package digital.fiasco.runtime.repository;

import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachments;
import digital.fiasco.runtime.dependency.artifact.ArtifactContentMetadata;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.maven.MavenRepository;

import java.net.URI;
import java.util.Objects;

/**
 * Base implementation of a {@link Repository}
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #name()} - The human readable name of this repository</li>
 *     <li>{@link #uri()} - The location of this repository</li>
 * </ul>
 *
 * <p><b>Retrieving Artifacts and Content</b></p>
 *
 * <ul>
 *     <li>{@link #resolve(ArtifactDescriptor)} - Gets the {@link Artifact} for the given descriptor</li>
 *     <li>{@link #content(Artifact, ArtifactContentMetadata, String)} - Gets the cached resource for the given artifact and content metadata</li>
 * </ul>
 *
 * <p><b>Adding and Removing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #install(Artifact, ArtifactAttachments)} - Adds the given artifact with the given attached resources</li>
 *     <li>{@link #clear()} - Removes all data from this repository</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
public abstract class BaseRepository extends BaseRepeater implements Repository
{
    /** The name of this repository */
    private final String name;

    /** The location of this repository */
    private final URI uri;

    /**
     * Creates a maven repository
     */
    public BaseRepository(String name, URI uri)
    {
        this.name = name;
        this.uri = uri;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof MavenRepository that)
        {
            return this.name().equals(that.name());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public URI uri()
    {
        return uri;
    }
}
