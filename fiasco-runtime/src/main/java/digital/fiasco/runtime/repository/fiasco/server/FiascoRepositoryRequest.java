package digital.fiasco.runtime.repository.fiasco.server;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;

import java.io.InputStream;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;

/**
 * The request portion of the Fiasco repository protocol. Contains a JSON-serializable list of artifacts to retrieve. A
 * {@link FiascoServer} responds to a JSON {@link FiascoRepositoryRequest} with a {@link FiascoRepositoryResponse},
 * containing the metadata for the subsequent content, which follows this header in the {@link InputStream}.
 *
 * <p><b>Descriptors</b></p>
 *
 * <ul>
 *     <li>{@link #descriptors()}</li>
 *     <li>{@link #with(ArtifactDescriptor...)}</li>
 *     <li>{@link #with(Iterable)}</li>
 * </ul>
 *
 * <p><b>Serialization</b></p>
 *
 * <ul>
 *     <li>{@link #requestFromJson(String)}</li>
 *     <li>{@link #toJson()}</li>
 * </ul>
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
        return new GsonObjectSerializer()
            .readObject(new StringResource(json), FiascoRepositoryRequest.class)
            .object();
    }

    /** The artifacts to retrieve */
    private final StringList descriptors = stringList();

    /**
     * Returns the artifact descriptors for this request
     */
    public ObjectList<ArtifactDescriptor> descriptors()
    {
        return descriptors.map(ArtifactDescriptor::descriptor);
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

    @Override
    public String toString()
    {
        return descriptors.join(", ");
    }

    /**
     * Returns a copy of this request with the given artifact descriptors added
     *
     * @param descriptors The descriptors to add
     * @return A copy of this request with the given desriptors added
     */
    public FiascoRepositoryRequest with(Iterable<ArtifactDescriptor> descriptors)
    {
        var request = new FiascoRepositoryRequest();
        descriptors.forEach(descriptor -> request.descriptors.add(descriptor.name()));
        return request;
    }

    /**
     * Returns a copy of this request with the given artifact descriptors added
     *
     * @param descriptors The artifact descriptors
     * @return A copy of this request with the given desriptors added
     */
    public FiascoRepositoryRequest with(ArtifactDescriptor... descriptors)
    {
        return with(list(descriptors));
    }
}
