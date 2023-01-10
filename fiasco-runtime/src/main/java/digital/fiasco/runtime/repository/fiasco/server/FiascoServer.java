package digital.fiasco.runtime.repository.fiasco.server;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.network.socket.server.ConnectionListener;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.InputResource;
import com.telenav.kivakit.resource.resources.OutputResource;
import com.telenav.kivakit.resource.writing.WritableResource;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.fiasco.CacheRepository;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;
import java.util.function.Consumer;

import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static com.telenav.kivakit.core.value.count.Maximum._8;
import static com.telenav.kivakit.resource.CloseMode.LEAVE_OPEN;
import static com.telenav.kivakit.resource.WriteMode.STREAM;
import static digital.fiasco.runtime.repository.fiasco.server.FiascoRepositoryRequest.requestFromJson;

/**
 * A server application that accepts JSON-encoded {@link FiascoRepositoryRequest}s and produces
 * {@link FiascoRepositoryResponse}s. The JSON-encoded response header includes all content metadata, including content
 * signatures and sizes. Following the response JSON is the binary content for each resolved artifact. The content size
 * for each requested artifact is used to ensure that the correct data for each content attachment is read from the
 * binary portion of the response. {@link FiascoServer}s run on port {@link #FIASCO_PORT}.
 *
 * <p><b>Performance</b></p>
 *
 * <p>
 * {@link FiascoServer} allows {@link FiascoClient} to request the resolution of multiple {@link ArtifactDescriptor}s at
 * once. The response includes all metadata and binary content associated with the requested artifacts. By comparison,
 * Apache Maven repositories require multiple requests to resolve a single artifact.
 * </p>
 *
 * @author Jonathan Locke
 */
public class FiascoServer extends Application
{
    /** The well-known Fiasco server port */
    public static final int FIASCO_PORT = 25_555;

    /** The server's repository */
    private final Repository repository = new CacheRepository("fiasco-server-repository");

    @Override
    public void onRun()
    {
        // Accept connections on the Fiasco server port (retrying 8 times before giving up).
        // When a socket connection occurs, handle the request.
        new ConnectionListener(FIASCO_PORT, _8).listen(handleRequest());
    }

    /**
     * Returns a socket consumer that handles requests
     *
     * @return A new socket consumer
     */
    @NotNull
    private Consumer<Socket> handleRequest()
    {
        // Given a socket,
        return socket ->

            // handle one request on a background thread.
            KivaKitThread.run(this, "FiascoRequestHandler", () ->
            {
                // Open the socket input and output,
                try (var in = new InputResource(socket.getInputStream());
                     var out = new OutputResource(socket.getOutputStream()))
                {
                    // read the request and send back a response
                    var response = response(readRequest(in));
                    writeHeader(response, out);
                    writeArtifacts(response, out, nullProgressReporter());
                }
                catch (Exception e)
                {
                    problem(e, "Response failed");
                }
            });
    }

    /**
     * Reads a JSON request from the given input resource, returning a request object containing the artifact
     * descriptors requested
     *
     * @param in The input resource
     * @return A request object, containing the requested artifact descriptors
     */
    private FiascoRepositoryRequest readRequest(Resource in)
    {
        return requestFromJson(in.reader().readText());
    }

    /**
     * Creates a response object for the given request
     *
     * @param request The request containing a list of desired artifact descriptors
     * @return The response, containing any resolved artifacts for the given descriptors
     */
    @NotNull
    private FiascoRepositoryResponse response(FiascoRepositoryRequest request)
    {
        return new FiascoRepositoryResponse()
            .with(repository.resolveArtifacts(request.descriptors()));
    }

    /**
     * Writes the artifacts in the response to the given output resource
     *
     * @param response The response
     * @param target The output resource
     * @param reporter The reporter to report progress
     */
    private void writeArtifacts(FiascoRepositoryResponse response, WritableResource target, ProgressReporter reporter)
    {
        // For each artifact,
        for (var artifact : response.artifacts())
        {
            // and each attached piece of content,
            for (var attachment : artifact.attachments())
            {
                // and copy it back to the requester.
                attachment
                    .content()
                    .resource()
                    .copyTo(target, STREAM, LEAVE_OPEN, reporter);
            }
        }
    }

    /**
     * Writes a JSON header from the given response to the given resource
     *
     * @param response The response containing a list of artifacts
     * @param target The target resource to write to
     */
    private void writeHeader(FiascoRepositoryResponse response, WritableResource target)
    {
        var out = target.writer().printWriter();
        out.println(response.toJson());
        out.flush();
    }
}
