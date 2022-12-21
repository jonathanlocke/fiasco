package digital.fiasco.runtime.repository.fiasco;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.network.socket.server.ConnectionListener;
import com.telenav.kivakit.resource.resources.InputResource;
import com.telenav.kivakit.resource.resources.OutputResource;
import digital.fiasco.runtime.repository.fiasco.protocol.FiascoRepositoryRequest;
import digital.fiasco.runtime.repository.fiasco.protocol.FiascoRepositoryResponse;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;
import java.util.function.Consumer;

import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static com.telenav.kivakit.core.value.count.Maximum._8;
import static com.telenav.kivakit.resource.CloseMode.LEAVE_OPEN;
import static com.telenav.kivakit.resource.CopyMode.STREAM;

/**
 * A server application that accepts JSON {@link FiascoRepositoryRequest}s and produces
 * {@link FiascoRepositoryResponse}s followed by content data.
 *
 * @author Jonathan Locke
 */
public class FiascoServer extends Application
{
    /** The well-known Fiasco server port */
    public static int FIASCO_PORT = 25_555;

    /** The server's repository */
    private final FiascoRepository repository = new FiascoRepository("server-repository");

    @Override
    protected void onRun()
    {
        // Accept connections on the Fiasco server port (retrying 8 times before giving up).
        // When a socket connection occurs, handle the request.
        new ConnectionListener(FIASCO_PORT, _8).listen(handleRequest());
    }

    @NotNull
    private Consumer<Socket> handleRequest()
    {
        return socket ->

                // handle it on a background thread.
                KivaKitThread.run(this, "FiascoRequestHandler", () ->
                {
                    // Open the socket input and output,
                    try (var in = new InputResource(socket.getInputStream()); var out = new OutputResource(socket.getOutputStream()))
                    {
                        // read the request,
                        var json = in.reader().readText();
                        var request = FiascoRepositoryRequest.requestFromJson(json);

                        // compose a response,
                        var response = new FiascoRepositoryResponse();
                        response.addAll(repository.resolve(request.descriptors()));

                        // push the header to the requester.
                        var printWriter = out.writer().printWriter();
                        printWriter.println(response.toJson());
                        printWriter.flush();

                        // For each attached piece of content,
                        for (var contentMetadata : response.contentMetadata())
                        {
                            // get the content from the repository,
                            var content = repository.content(null, contentMetadata, null);

                            // and copy it back to the requester.
                            content.copyTo(out, STREAM, LEAVE_OPEN, nullProgressReporter());
                        }
                    }
                    catch (Exception e)
                    {
                        problem(e, "");
                    }
                });
    }
}
