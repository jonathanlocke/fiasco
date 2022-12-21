package digital.fiasco.runtime.repository.maven.resolver;

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.core.time.Rate;
import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.MetadataNotFoundException;
import org.eclipse.aether.transfer.TransferEvent;

import static com.telenav.kivakit.core.time.Rate.perSecond;
import static com.telenav.kivakit.core.time.Time.epochMilliseconds;
import static com.telenav.kivakit.core.time.Time.now;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static com.telenav.kivakit.interfaces.string.StringFormattable.Format.USER_LABEL;
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
        // If the transfer is less than 512 bytes / sec,
        var rate = rate(event);
        if (rate.isSlowerThan(perSecond(128)))
        {
            // then warn that the transfer is stalling
            var resource = event.getResource();
            warning("Slow transfer: Downloaded $ at $:\n$ => $",
                    bytes(event.getTransferredBytes()).asString(USER_LABEL),
                    rate.asBytesPerSecond(),
                    resource.getRepositoryUrl(),
                    resource.getResourceName());
        }
    }

    @Override
    public void transferSucceeded(TransferEvent event)
    {
        requireNonNull(event);

        var resource = event.getResource();
        var contentLength = event.getTransferredBytes();
        if (contentLength >= 0)
        {
            var type = event.getRequestType() == PUT
                    ? "Uploaded"
                    : "Downloaded";

            trace("$: $ => $ ($)", type,
                    resource.getRepositoryUrl(),
                    resource.getResourceName(),
                    rate(event).perSecond());
        }
    }

    private Rate rate(TransferEvent event)
    {
        var resource = event.getResource();
        var contentLength = event.getTransferredBytes();
        if (contentLength >= 0)
        {
            var duration = now().minus(epochMilliseconds(resource.getTransferStartTime()));
            if (duration.isNonZero())
            {
                return new Rate(contentLength, duration);
            }
        }
        return perSecond(0);
    }
}
