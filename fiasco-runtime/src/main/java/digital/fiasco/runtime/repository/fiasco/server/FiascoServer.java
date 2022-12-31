package digital.fiasco.runtime.repository.fiasco.server;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.network.socket.server.ConnectionListener;
import com.telenav.kivakit.resource.resources.InputResource;
import com.telenav.kivakit.resource.resources.OutputResource;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.fiasco.CacheFiascoRepository;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;
import java.util.function.Consumer;

import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static com.telenav.kivakit.core.value.count.Maximum._8;
import static com.telenav.kivakit.resource.CloseMode.LEAVE_OPEN;
import static com.telenav.kivakit.resource.WriteMode.STREAM;

/**
 * A server application that accepts JSON {@link FiascoRepositoryRequest}s and produces
 * {@link FiascoRepositoryResponse}s followed by content data. {@link FiascoServer}s run on port {@link #FIASCO_PORT}.
 *
 * @author Jonathan Locke
 */
public class FiascoServer extends Application
{
    /** The well-known Fiasco server port */
    public static int FIASCO_PORT = 25_555;

    /** The server's repository */
    private final Repository repository = new CacheFiascoRepository("server-repository");

    @Override
    public void onRun()
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
                    response.addAll(repository.resolveArtifacts(request.descriptors()));

                    // push the header to the requester.
                    @SuppressWarnings("resource")
                    var printWriter = out.writer().printWriter();
                    printWriter.println(response.toJson());
                    printWriter.flush();

                    // For each attached piece of content,
                    for (var artifact : response.artifacts())
                    {
                        for (var attachment : artifact.attachments().values())
                        {
                            // and copy it back to the requester.
                            attachment
                                .content()
                                .resource()
                                .copyTo(out, STREAM, LEAVE_OPEN, nullProgressReporter());
                        }
                    }
                }
                catch (Exception e)
                {
                    problem(e, "");
                }
            });
    }
}
