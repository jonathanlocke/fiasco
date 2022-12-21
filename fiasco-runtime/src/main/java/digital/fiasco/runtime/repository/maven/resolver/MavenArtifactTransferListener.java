package digital.fiasco.runtime.repository.maven.resolver;

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.core.time.Rate;
import com.telenav.kivakit.core.time.Time;
import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.MetadataNotFoundException;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transfer.TransferResource;

import static java.util.Objects.requireNonNull;
import static org.eclipse.aether.transfer.TransferEvent.RequestType.PUT;

/**
 * A simplistic transfer listener that logs uploads/downloads to the console.
 */
public class MavenArtifactTransferListener extends AbstractTransferListener implements ComponentMixin
{
    @Override
    public void transferCorrupted(TransferEvent event)
    {
        requireNonNull(event);
        problem(event.getException(), "Transfer corrupted");
    }

    @Override
    public void transferFailed(TransferEvent event)
    {
        requireNonNull(event);
        if (!(event.getException() instanceof MetadataNotFoundException))
        {
            problem(event.getException(), "Transfer failed");
        }
    }

    @Override
    public void transferInitiated(TransferEvent event)
    {
        requireNonNull(event);
        var message = event.getRequestType() == PUT ? "Uploading" : "Downloading";

        trace("$ $", message, event.getResource().getRepositoryUrl() + event.getResource().getResourceName());
    }

    @Override
    public void transferProgressed(TransferEvent event)
    {
    }

    @Override
    public void transferSucceeded(TransferEvent event)
    {
        requireNonNull(event);

        TransferResource resource = event.getResource();
        var contentLength = event.getTransferredBytes();
        if (contentLength >= 0)
        {
            var duration = Time.now().minus(Time.epochMilliseconds(resource.getTransferStartTime()));
            if (duration.isNonZero())
            {
                String type = event.getRequestType() == PUT ? "Uploaded" : "Downloaded";
                var rate = new Rate(contentLength, duration);
                trace(type + ": " + resource.getRepositoryUrl() + resource.getResourceName()
                        + " (" + rate.perSecond() + ")");
            }
        }
    }
}
