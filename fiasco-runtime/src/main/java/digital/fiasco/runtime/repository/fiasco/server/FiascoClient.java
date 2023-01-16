package digital.fiasco.runtime.repository.fiasco.server;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.network.http.secure.SecureHttpNetworkLocation;
import com.telenav.kivakit.resource.resources.DataResource;
import com.telenav.kivakit.resource.resources.InputResource;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.repository.fiasco.RemoteRepository;

import java.net.URI;

import static com.telenav.kivakit.core.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.resource.Uris.uri;
import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifacts;
import static digital.fiasco.runtime.repository.fiasco.server.FiascoRepositoryResponse.responseFromJson;
import static digital.fiasco.runtime.repository.fiasco.server.FiascoServer.FIASCO_PORT;

/**
 * Client that resolves requests to a {@link RemoteRepository} using the Fiasco repository protocol over HTTPS.
 *
 * @author Jonathan Locke
 */
public class FiascoClient extends BaseComponent
{
    /**
     * Gets the requested artifacts from the given {@link RemoteRepository}
     */
    public FiascoRepositoryResponse request(RemoteRepository repository, FiascoRepositoryRequest request)
    {
        // Get the fiasco server's HTTP location,
        var location = new SecureHttpNetworkLocation(fiascoServer(repository));

        // then post the request to the server, and return the response.
        var httpPostResource = location.post("application/json", request.toJson());

        // Open the response input,
        try (var in = (httpPostResource.openForReading()))
        {
            // and read the response JSON.
            var response = responseFromJson(new InputResource(in));

            // Then, for each artifact,
            var resolved = ArtifactList.artifacts();
            for (var artifact : response.artifacts())
            {
                // and attachment,
                var resolvedAttachments = new ObjectMap<ArtifactAttachmentType, ArtifactAttachment>();
                for (var at : artifact.attachments())
                {
                    // read the content data,
                    var attachment = (ArtifactAttachment) at;
                    var content = attachment.content();
                    var size = content.size().asInt();
                    var data = new byte[size];
                    ensureEqual(in.read(data), size);

                    // and resolve the attachment.
                    resolvedAttachments.put(attachment.attachmentType(), attachment
                        .withContent(content
                            .withResource(new DataResource(data))));
                }

                // Assign the resolved attachments to the artifact then resolve the artifact.
                // noinspection unchecked
                resolved = resolved.with(artifact.withAttachments(resolvedAttachments));
            }

            // Assign the resolved artifacts to the response.
            response.artifacts(resolved);
            return response;
        }
        catch (Exception e)
        {
            throw problem(e, "Resolve request failed: $", request).asException();
        }
    }

    /**
     * Get a URI for the given repository. If the URI does not specify the port, the default Fiasco server port is
     * added.
     *
     * @param repository The repository
     * @return The URI to the repository
     */
    private URI fiascoServer(RemoteRepository repository)
    {
        var uri = repository.uri();
        if (uri.getPort() <= 0)
        {
            uri = uri(uri + ":" + FIASCO_PORT);
        }
        return uri;
    }
}
