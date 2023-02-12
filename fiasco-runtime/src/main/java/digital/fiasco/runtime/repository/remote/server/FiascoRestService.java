package digital.fiasco.runtime.repository.remote.server;

import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import digital.fiasco.runtime.repository.remote.server.api.InstallArtifactRequest;
import digital.fiasco.runtime.repository.remote.server.api.ResolveArtifactsRequest;

import static com.telenav.kivakit.core.version.Version.version;
import static com.telenav.kivakit.network.http.HttpMethod.POST;

/**
 * The {@link FiascoServer}'s {@link RestService}.
 *
 * @author Jonathan Locke
 */
public class FiascoRestService extends RestService
{
    public FiascoRestService(FiascoServer server)
    {
        super(server);
    }

    @Override
    public Version apiVersion()
    {
        return version("0.9.0");
    }

    @Override
    public void onInitialize()
    {
        mount(apiVersion(), "resolve-artifacts", POST, ResolveArtifactsRequest.class);
        mount(apiVersion(), "install-artifact", POST, InstallArtifactRequest.class);
    }
}
