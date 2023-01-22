package digital.fiasco.runtime.repository.remote.server;

import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import digital.fiasco.runtime.repository.remote.server.api.v0_9.install.InstallArtifactRequest;
import digital.fiasco.runtime.repository.remote.server.api.v0_9.resolve.ResolveArtifactRequest;

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
        mount(version("0.9.0"), "/resolve-artifacts", POST, ResolveArtifactRequest.class);
        mount(version("0.9.0"), "/install-artifact", POST, InstallArtifactRequest.class);
    }
}
