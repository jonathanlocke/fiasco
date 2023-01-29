package digital.fiasco.runtime.repository.remote.server.api;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.repository.local.LocalRepository;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;
import digital.fiasco.runtime.repository.remote.server.FiascoServer;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * A request to resolve artifact descriptors.
 *
 * <p>
 * A running {@link FiascoServer} will respond to a JSON {@link ResolveArtifactsRequest} with a
 * {@link ResolveArtifactResponse}.
 * </p>
 *
 * <p><b>Descriptors</b></p>
 *
 * <ul>
 *     <li>{@link #descriptors()}</li>
 * </ul>
 *
 * <p><b>Performance</b></p>
 *
 * <p>
 * {@link ResolveArtifactsRequest} allows the {@link FiascoClient} to resolve multiple {@link ArtifactDescriptor}s
 * in a single request.
 * </p>
 *
 * @author Jonathan Locke
 * @see ResolveArtifactResponse
 */
public class ResolveArtifactsRequest extends BaseMicroservletRequest
{
    /** The artifacts to retrieve */
    @Expose
    private final List<ArtifactDescriptor> descriptors;

    public ResolveArtifactsRequest(List<ArtifactDescriptor> descriptors)
    {
        this.descriptors = new ArrayList<>(descriptors);
    }

    public ResolveArtifactsRequest()
    {
        this.descriptors = list();
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
