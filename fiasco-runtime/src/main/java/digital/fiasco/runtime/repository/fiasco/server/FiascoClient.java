package digital.fiasco.runtime.repository.fiasco.server;

import com.telenav.kivakit.network.http.secure.SecureHttpNetworkLocation;
import digital.fiasco.runtime.repository.fiasco.RemoteFiascoRepository;

import java.net.URI;

import static com.telenav.kivakit.resource.Uris.uri;
import static digital.fiasco.runtime.repository.fiasco.server.FiascoRepositoryResponse.responseFromJson;
import static digital.fiasco.runtime.repository.fiasco.server.FiascoServer.FIASCO_PORT;

/**
 * Client that resolves requests to a {@link RemoteFiascoRepository} using the Fiasco repository protocol over HTTPS.
 *
 * @author Jonathan Locke
 */
public class FiascoClient
{
    /**
     * Gets the requested artifacts from the given {@link RemoteFiascoRepository}
     */
    public FiascoRepositoryResponse request(RemoteFiascoRepository repository, FiascoRepositoryRequest request)
    {
        // Get the fiasco server's HTTP location,
        var location = new SecureHttpNetworkLocation(fiascoServer(repository));

        // then post the request to the server, and return the response.
        return responseFromJson(location.post("application/json", request.toJson()).asString());
    }

    /**
     * Get a URI for the given repository. If the URI does not specify the port, the default Fiasco server port is
     * added.
     *
     * @param repository The repository
     * @return The URI to the repository
     */
    private URI fiascoServer(RemoteFiascoRepository repository)
    {
        var uri = repository.uri();
        if (uri.getPort() <= 0)
        {
            uri = uri(uri + ":" + FIASCO_PORT);
        }
        return uri;
    }
}
