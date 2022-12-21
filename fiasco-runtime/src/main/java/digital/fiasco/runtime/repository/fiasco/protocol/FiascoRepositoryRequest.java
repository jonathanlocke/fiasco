package digital.fiasco.runtime.repository.fiasco.protocol;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.fiasco.FiascoRepository;

import java.io.InputStream;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;

/**
 * The request portion of the Fiasco repository protocol. Contains a JSON-serializable list of artifacts to retrieve. A
 * {@link FiascoRepository} responds to a JSON query with a {@link FiascoRepositoryResponse}, containing the metdata for
 * the subsequent content, which follows this header in the {@link InputStream}.
 *
 * @author Jonathan Locke
 */
public class FiascoRepositoryRequest
{
    /**
     * Converts the given JSON text to a request
     *
     * @param json The JSON
     * @return The request
     */
    public static FiascoRepositoryRequest requestFromJson(String json)
    {
        var serialized = new StringResource(json);
        var serializer = new GsonObjectSerializer();
        return serializer.readObject(serialized, FiascoRepositoryRequest.class).object();
    }

    /** The artifacts to retrieve */
    private final StringList descriptors = stringList();

    /**
     * Adds an artifact to retrieve
     *
     * @param descriptor The artifact descriptor
     * @return This request for chaining
     */
    public FiascoRepositoryRequest add(ArtifactDescriptor descriptor)
    {
        descriptors.add(descriptor.name());
        return this;
    }

    /**
     * Adds the give list of descriptors to this request
     *
     * @param descriptors The descriptors to add
     */
    public void addAll(ObjectList<ArtifactDescriptor> descriptors)
    {
        descriptors.forEach(this::add);
    }

    /**
     * Returns the artifact descriptors for this request
     */
    public ObjectList<ArtifactDescriptor> descriptors()
    {
        return descriptors.map(ArtifactDescriptor::artifactDescriptor);
    }

    /**
     * Returns this request as JSON
     */
    public String toJson()
    {
        var serializer = new GsonObjectSerializer();
        var serialized = new StringOutputResource();
        serializer.writeObject(serialized, new SerializableObject<>(this));
        return serialized.string();
    }
}
