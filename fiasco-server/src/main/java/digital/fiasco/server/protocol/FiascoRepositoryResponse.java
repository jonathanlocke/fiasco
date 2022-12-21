package digital.fiasco.server.protocol;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactContentMetadata;

import java.io.InputStream;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * The response header for a {@link FiascoRepositoryRequest} containing the {@link ArtifactContentMetadata} metadata for
 * each artifact requested. The artifact content blocks themselves follow this header in the {@link InputStream}.
 *
 * @author Jonathan Locke
 */
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
    private final ObjectList<ArtifactContentMetadata> metadata = list();

    /** The list of artifact content metadata */
    private transient final ObjectList<Artifact<?>> artifacts = list();

    /**
     * Adds the given content metadata to this response
     *
     * @param content The content metadata to add
     */
    public FiascoRepositoryResponse add(Artifact<?> artifact)
    {
        metadata.addAll(artifact.attachments());
        artifacts.add(artifact);
        return this;
    }

    /**
     * Returns the artifacts that have been added to this response
     */
    public ObjectList<Artifact<?>> artifacts()
    {
        return artifacts;
    }

    /**
     * Returns the list of metadata in this response header
     */
    public ObjectList<ArtifactContentMetadata> contentMetadata()
    {
        return metadata;
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
}
