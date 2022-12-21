package digital.fiasco.server;

import com.telenav.kivakit.network.http.secure.SecureHttpNetworkLocation;
import com.telenav.kivakit.resource.Uris;
import digital.fiasco.runtime.repository.fiasco.FiascoRepository;
import digital.fiasco.server.protocol.FiascoRepositoryRequest;
import digital.fiasco.server.protocol.FiascoRepositoryResponse;

import java.net.URI;

import static digital.fiasco.server.FiascoServer.FIASCO_PORT;
import static digital.fiasco.server.protocol.FiascoRepositoryResponse.responseFromJson;

/**
 * Client that resolves requests to a {@link FiascoRepository} using the Fiasco repository protocol over HTTPS.
 *
 * @author Jonathan Locke
 */
public class FiascoClient
{
    /**
     * Gets the requested artifacts from the given {@link FiascoRepository}
     */
    public FiascoRepositoryResponse request(FiascoRepository repository, FiascoRepositoryRequest request)
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
    private URI fiascoServer(FiascoRepository repository)
    {
        var uri = repository.uri();
        if (uri.getPort() <= 0)
        {
            uri = Uris.uri(uri + ":" + FIASCO_PORT);
        }
        return uri;
    }
}
