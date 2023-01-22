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

import java.util.Collection;

import static com.telenav.kivakit.network.core.LocalHost.localhost;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptors;
import static digital.fiasco.runtime.repository.remote.server.FiascoRestService.fiascoApiVersion;

/**
 * Client that resolves requests to a {@link RemoteRepository} using the Fiasco repository protocol over HTTPS.
 *
 * @author Jonathan Locke
 */
public class FiascoClient extends BaseComponent implements SettingsTrait
{
    /**
     * Installs the given artifact on the {@link FiascoServer} specified in {@link FiascoServerSettings}
     *
     * @param artifact The artifact to install
     * @return The result of attempting to install the artifact
     */
    public InstallationResult installArtifact(Artifact<?> artifact)
    {
        return null;
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
    public ArtifactList resolveArtifacts(Collection<ArtifactDescriptor> descriptors)
    {
        // Get the port and version of the Fiasco server,
        var port = localhost().http(requireSettings(FiascoServerSettings.class).port());

        // create a client to talk to the microservice REST API,
        var client = listenTo(new RestClient(new GsonObjectSerializer(), port, fiascoApiVersion()));

        // then issue a divide request and read the response,
        var response = client.post("0.9.0/resolve-artifacts",
            ResolveArtifactResponse.class, new ResolveArtifactRequest(descriptors));

        // then show the response.
        trace("response => $", response);

        return response.artifacts();
    }
}
