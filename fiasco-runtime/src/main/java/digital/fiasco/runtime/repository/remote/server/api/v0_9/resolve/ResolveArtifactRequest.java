package digital.fiasco.runtime.repository.remote.server.api.v0_9.resolve;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.local.LocalRepository;
import digital.fiasco.runtime.repository.remote.server.FiascoServer;

import java.io.InputStream;
import java.util.Collection;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * A request to resolve artifact descriptors.
 *
 * <p>
 * A running {@link FiascoServer} will respond to a JSON {@link ResolveArtifactRequest} with a
 * {@link ResolveArtifactResponse}, containing the metadata for the subsequent content, which follows in the response
 * {@link InputStream}.
 * </p>
 *
 * <p><b>Descriptors</b></p>
 *
 * <ul>
 *     <li>{@link #descriptors()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
public class ResolveArtifactRequest extends BaseMicroservletRequest
{
    /** The artifacts to retrieve */
    @Expose
    private final Collection<ArtifactDescriptor> descriptors;

    public ResolveArtifactRequest(Collection<ArtifactDescriptor> descriptors)
    {
        this.descriptors = descriptors;
    }

    /**
     * Returns the artifact descriptors for this request
     */
    public ObjectList<ArtifactDescriptor> descriptors()
    {
        return list(descriptors);
    }

    @Override
    public MicroservletResponse onRespond()
    {
        return new ResolveArtifactResponse(require(LocalRepository.class)
            .resolveArtifacts(descriptors));
    }

    @Override
    public Class<? extends MicroservletResponse> responseType()
    {
        return ResolveArtifactResponse.class;
    }

    @Override
    public String toString()
    {
        return descriptors().join(", ");
    }
}
