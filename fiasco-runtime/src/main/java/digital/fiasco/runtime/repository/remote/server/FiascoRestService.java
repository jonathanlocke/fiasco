package digital.fiasco.runtime.repository.remote.server;

import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import digital.fiasco.runtime.repository.remote.server.api.install.InstallArtifactRequest;
import digital.fiasco.runtime.repository.remote.server.api.resolve.ResolveArtifactsRequest;

import static com.telenav.kivakit.core.version.Version.version;
import static com.telenav.kivakit.network.http.HttpMethod.POST;

/**
 * The {@link FiascoServer}'s {@link RestService}.
 *
 * @author Jonathan Locke
 */
public class FiascoRestService extends RestService
{
    public static Version fiascoApiVersion()
    {
        return version("0.9.0");
    }

    public FiascoRestService(FiascoServer server)
    {
        super(server);
    }

    @Override
    public void onInitialize()
    {
        mount(fiascoApiVersion(), "resolve-artifacts", POST, ResolveArtifactsRequest.class);
        mount(fiascoApiVersion(), "install-artifact", POST, InstallArtifactRequest.class);
    }
}
