package digital.fiasco.runtime.repository.remote.server;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.function.Functions;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.microservice.protocols.rest.http.RestClient;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import com.telenav.kivakit.settings.SettingsTrait;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;
import digital.fiasco.runtime.repository.Repository.InstallationResult;
import digital.fiasco.runtime.repository.RepositoryContentReader;
import digital.fiasco.runtime.repository.remote.RemoteRepository;
import digital.fiasco.runtime.repository.remote.server.api.ResolveArtifactsRequest;
import digital.fiasco.runtime.repository.remote.server.api.ResolveArtifactResponse;
import digital.fiasco.runtime.repository.remote.server.serialization.FiascoGsonFactory;

import java.util.List;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static com.telenav.kivakit.network.core.LocalHost.localhost;
import static digital.fiasco.runtime.repository.remote.server.FiascoRestService.fiascoApiVersion;

/**
 * Client that resolves requests to a {@link RemoteRepository} using the Fiasco repository protocol over HTTPS.
 *
 * <p><b>Performance</b></p>
 *
 * <p>
 * {@link ResolveArtifactsRequest} allows the {@link FiascoClient} to resolve multiple {@link ArtifactDescriptor}s in a
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
     * {@link FiascoServerSettings}. No content is read.
     *
     * @param descriptors The artifact descriptors
     * @return The list of resolved artifacts
     */
    public ArtifactList resolveArtifacts(List<ArtifactDescriptor> descriptors)
    {
        return resolveArtifacts(descriptors, nullProgressReporter(), Functions::doNothing);
    }

    /**
     * Resolves the given artifact descriptors by posting a request to the {@link FiascoServer} specified in
     * {@link FiascoServerSettings}. Content is read by the Repository content reader, and progress is reported by the
     * given progress reporter.
     *
     * @param descriptors The artifact descriptors
     * @param reporter The progress reporter
     * @param reader The content reader
     * @return The list of resolved artifacts
     */
    public ArtifactList resolveArtifacts(List<ArtifactDescriptor> descriptors,
                                         ProgressReporter reporter,
                                         RepositoryContentReader reader)
    {
        // Get the port of the Fiasco server,
        var port = localhost().http(requireSettings(FiascoServerSettings.class).port());

        // create a client to talk to it via REST,
        var restClient = listenTo(new RestClient(new GsonObjectSerializer(), port, fiascoApiVersion()));

        // then issue a ResolveArtifactsRequest and read the response. The reader callback will be called with the
        // input that trails the JSON header, in this case the artifact content).
        var response = restClient.postAndReadContent("resolve-artifacts/pretty/true",
            new ResolveArtifactsRequest(descriptors), ResolveArtifactResponse.class, reader::read);
        trace("response => $", response);

        // If we got a response, then return the artifacts.
        return response != null ? response.artifacts() : null;
    }
}
