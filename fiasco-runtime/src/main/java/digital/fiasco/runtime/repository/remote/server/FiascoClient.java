package digital.fiasco.runtime.repository.remote.server;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.protocols.rest.http.RestClient;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import com.telenav.kivakit.settings.SettingsTrait;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.repository.Repository.InstallationResult;
import digital.fiasco.runtime.repository.remote.RemoteRepository;
import digital.fiasco.runtime.repository.remote.server.api.resolve.ResolveArtifactRequest;
import digital.fiasco.runtime.repository.remote.server.api.resolve.ResolveArtifactResponse;
import digital.fiasco.runtime.repository.remote.server.serialization.FiascoGsonFactory;

import java.util.List;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.network.core.LocalHost.localhost;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptors;
import static digital.fiasco.runtime.repository.remote.server.FiascoRestService.fiascoApiVersion;

/**
 * Client that resolves requests to a {@link RemoteRepository} using the Fiasco repository protocol over HTTPS.
 *
 * <p><b>Performance</b></p>
 *
 * <p>
 * {@link ResolveArtifactRequest} allows the {@link FiascoClient} to resolve multiple {@link ArtifactDescriptor}s in a
 * single request.
 * </p>
 *
 * @author Jonathan Locke
 */
public class FiascoClient extends BaseComponent implements SettingsTrait
{
    /**
     * Returns an instance of {@link FiascoClient}
     */
    public static FiascoClient fiascoClient()
    {
        return new FiascoClient();
    }

    public FiascoClient()
    {
        register(new FiascoGsonFactory());
    }

    /**
     * Installs the given artifact on the {@link FiascoServer} specified in {@link FiascoServerSettings}
     *
     * @param artifact The artifact to install
     * @return The result of attempting to install the artifact
     */
    public InstallationResult installArtifact(Artifact<?> artifact)
    {
        return unsupported();
    }

    /**
     * Resolves the given artifact descriptors by posting a request to the {@link FiascoServer} specified in
     * {@link FiascoServerSettings}
     *
     * @param descriptors The artifact descriptors
     * @return The list of resolved artifacts
     */
    public ArtifactList resolveArtifacts(String... descriptors)
    {
        return resolveArtifacts(descriptors(descriptors));
    }

    /**
     * Resolves the given artifact descriptors by posting a request to the {@link FiascoServer} specified in
     * {@link FiascoServerSettings}
     *
     * @param descriptors The artifact descriptors
     * @return The list of resolved artifacts
     */
    public ArtifactList resolveArtifacts(List<ArtifactDescriptor> descriptors)
    {
        // Get the port and version of the Fiasco server,
        var port = localhost()
            .http(requireSettings(FiascoServerSettings.class)
                .port());

        // create a client to talk to the microservice REST API,
        var restClient = listenTo(new RestClient(new GsonObjectSerializer(), port, fiascoApiVersion()));

        // then issue a divide request and read the response,
        var response = restClient.post("resolve-artifacts/pretty/true",
            ResolveArtifactResponse.class, new ResolveArtifactRequest(descriptors));

        // then show the response.
        trace("response => $", response);

        return response.artifacts();
    }
}
