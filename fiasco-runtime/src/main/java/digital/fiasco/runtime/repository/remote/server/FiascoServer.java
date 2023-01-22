package digital.fiasco.runtime.repository.remote.server;

import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.MicroserviceMetadata;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.local.LocalRepository;
import digital.fiasco.runtime.repository.remote.server.api.resolve.ResolveArtifactRequest;
import digital.fiasco.runtime.repository.remote.server.api.resolve.ResolveArtifactResponse;
import digital.fiasco.runtime.repository.remote.server.serialization.FiascoGsonFactory;

import static digital.fiasco.runtime.repository.remote.server.FiascoRestService.fiascoApiVersion;

/**
 * A server application that accepts JSON-encoded {@link ResolveArtifactRequest}s and produces
 * {@link ResolveArtifactResponse}s. The JSON-encoded response includes all content metadata, including content
 * signatures and sizes. Following the response is the binary content for each resolved artifact. The content size for
 * each requested artifact is used to ensure that the correct data for each content attachment is read from the binary
 * portion of the response.
 *
 * <p><b>Performance</b></p>
 *
 * <p>
 * {@link FiascoServer} allows {@link FiascoClient} to request the resolution of multiple {@link ArtifactDescriptor}s at
 * once. The response includes all metadata and binary content associated with the requested artifacts. By comparison,
 * Apache Maven repositories require multiple requests to resolve a single artifact.
 * </p>
 *
 * @author Jonathan Locke
 */
public class FiascoServer extends Microservice<Void>
{
    public static void main(String[] arguments)
    {
        run(FiascoServer.class, arguments);
    }

    /**
     * Returns metadata describing this microservice
     */
    @Override
    public MicroserviceMetadata metadata()
    {
        return new MicroserviceMetadata()
            .withName("Fiasco Server")
            .withDescription("Fiasco Artifact Server")
            .withVersion(fiascoApiVersion());
    }

    @Override
    public void onInitialize()
    {
        register(new LocalRepository("server-repository"));
    }

    /**
     * Returns the REST service for this microservice
     */
    @Override
    public FiascoRestService onNewRestService()
    {
        return new FiascoRestService(this);
    }

    @Override
    protected void onSerializationInitialize()
    {
        register(new FiascoGsonFactory());
    }
}
