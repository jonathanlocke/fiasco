package digital.fiasco.runtime.repository.fiasco.server;

import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;

import java.io.InputStream;

import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;

/**
 * The response header for a {@link FiascoRepositoryRequest} containing the {@link ArtifactContent} metadata for each
 * artifact requested. The artifact content blocks themselves follow this header in the {@link InputStream}.
 *
 * <p><b>Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #artifacts()}</li>
 *     <li>{@link #with(Artifact...)}</li>
 *     <li>{@link #with(DependencyList)}</li>
 * </ul>
 *
 * <p><b>Serialization</b></p>
 *
 * <ul>
 *     <li>{@link #responseFromJson(Resource)}</li>
 *     <li>{@link #toJson()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class FiascoRepositoryResponse
{
    /**
     * Returns the response for the given JSON
     *
     * @param in The JSON to read
     * @return The response
     */
    public static FiascoRepositoryResponse responseFromJson(Resource in)
    {
        var serializer = new GsonObjectSerializer();
        return serializer.readObject(in, FiascoRepositoryResponse.class).object();
    }

    /** The list of artifact content metadata */
    private transient DependencyList<Artifact<?>> artifacts = dependencyList();

    /**
     * Returns the artifacts that have been added to this response
     */
    public DependencyList<Artifact<?>> artifacts()
    {
        return artifacts.copy();
    }

    /**
     * Sets the artifacts for this response
     *
     * @param artifacts The artifacts
     */
    public void artifacts(DependencyList<Artifact<?>> artifacts)
    {
        this.artifacts = artifacts;
    }

    /**
     * Returns a shallow copy of this response
     *
     * @return The copy
     */
    public FiascoRepositoryResponse copy()
    {
        var copy = new FiascoRepositoryResponse();
        copy.artifacts = artifacts.copy();
        return copy;
    }

    /**
     * Returns this response as JSON
     */
    public String toJson()
    {
        var serialized = new StringOutputResource();
        new GsonObjectSerializer().writeObject(serialized, new SerializableObject<>(this));
        return serialized.string();
    }

    /**
     * Adds the given artifacts to this response
     *
     * @param artifacts The artifacts to add
     */
    public FiascoRepositoryResponse with(DependencyList<Artifact<?>> artifacts)
    {
        var copy = new FiascoRepositoryResponse();
        copy.artifacts = this.artifacts.with(artifacts);
        return copy;
    }

    /**
     * Adds the given artifact to this response
     *
     * @param artifact The artifact to add
     */
    public final FiascoRepositoryResponse with(Artifact<?>... artifact)
    {
        var copy = new FiascoRepositoryResponse();
        copy.artifacts = artifacts.with(artifact);
        return copy;
    }
}
