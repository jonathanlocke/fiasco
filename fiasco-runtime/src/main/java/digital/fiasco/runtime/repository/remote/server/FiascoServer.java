package digital.fiasco.runtime.repository.remote.server;

import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.MicroserviceMetadata;
import digital.fiasco.runtime.repository.local.LocalRepository;
import digital.fiasco.runtime.repository.remote.server.api.InstallArtifactRequest;
import digital.fiasco.runtime.repository.remote.server.api.ResolveArtifactsRequest;
import digital.fiasco.runtime.repository.remote.server.serialization.FiascoGsonFactory;

/**
 * A microservice that responds to JSON-encoded requests:
 *
 * <ul>
 *     <li>{@link ResolveArtifactsRequest}</li>
 *     <li>{@link InstallArtifactRequest}</li>
 * </ul>
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
            .withVersion(restService().apiVersion());
    }

    @Override
    public void onInitialize()
    {
        if (lookup(LocalRepository.class) == null)
        {
            register(new LocalRepository("server-repository"));
        }
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
        super.onSerializationInitialize();

        register(new FiascoGsonFactory());
    }
}
