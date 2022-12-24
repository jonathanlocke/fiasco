package digital.fiasco.runtime.repository.fiasco.server;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;

import java.io.InputStream;
import java.util.Collection;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * The response header for a {@link FiascoRepositoryRequest} containing the {@link ArtifactContent} metadata for each
 * artifact requested. The artifact content blocks themselves follow this header in the {@link InputStream}.
 *
 * <p><b>Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #artifacts()}</li>
 * </ul>
 *
 * <p><b>Serialization</b></p>
 *
 * <ul>
 *     <li>{@link #responseFromJson(String)}</li>
 *     <li>{@link #toJson()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("UnusedReturnValue")
public class FiascoRepositoryResponse
{
    /**
     * Returns the response for the given JSON
     *
     * @param json The JSON
     * @return The response
     */
    public static FiascoRepositoryResponse responseFromJson(String json)
    {
        var serialized = new StringResource(json);
        var serializer = new GsonObjectSerializer();
        return serializer.readObject(serialized, FiascoRepositoryResponse.class).object();
    }

    /** The list of artifact content metadata */
    private transient final ObjectList<Artifact<?>> artifacts = list();

    /**
     * Returns the artifacts that have been added to this response
     */
    public ObjectList<Artifact<?>> artifacts()
    {
        return artifacts;
    }

    /**
     * Returns this response as JSON
     */
    public String toJson()
    {
        var serializer = new GsonObjectSerializer();
        var serialized = new StringOutputResource();
        serializer.writeObject(serialized, new SerializableObject<>(this));
        return serialized.string();
    }

    /**
     * Adds the given artifact to this response
     *
     * @param artifact The artifact to add
     */
    FiascoRepositoryResponse add(Artifact<?> artifact)
    {
        artifacts.add(artifact);
        return this;
    }

    /**
     * Adds the given artifacts to this response
     *
     * @param artifacts The artifacts to add
     */
    void addAll(Collection<Artifact<?>> artifacts)
    {
        artifacts.forEach(this::add);
    }
}
