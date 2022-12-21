package digital.fiasco.runtime.repository.maven.resolver;

import com.telenav.kivakit.component.ComponentMixin;
import org.eclipse.aether.AbstractRepositoryListener;
import org.eclipse.aether.RepositoryEvent;

import static digital.fiasco.runtime.repository.maven.resolver.MavenUtilities.artifact;
import static digital.fiasco.runtime.repository.maven.resolver.MavenUtilities.metadata;
import static digital.fiasco.runtime.repository.maven.resolver.MavenUtilities.repository;
import static java.util.Objects.requireNonNull;

/**
 * A simplistic repository listener that logs events to the console.
 */
public class MavenRepositoryListener extends AbstractRepositoryListener implements ComponentMixin
{
    @Override
    public void artifactDeployed(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Deployed " + artifact(event) + " to " + repository(event));
    }

    @Override
    public void artifactDeploying(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Deploying " + artifact(event) + " to " + repository(event));
    }

    @Override
    public void artifactDescriptorInvalid(RepositoryEvent event)
    {
        requireNonNull(event);
        problem("Invalid artifact descriptor for " + artifact(event) + ": " + event.getException().getMessage());
    }

    @Override
    public void artifactDescriptorMissing(RepositoryEvent event)
    {
        requireNonNull(event);
        problem("Missing artifact descriptor for " + artifact(event));
    }

    @Override
    public void artifactDownloaded(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Downloaded artifact " + artifact(event) + " from " + repository(event));
    }

    @Override
    public void artifactDownloading(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Downloading artifact " + artifact(event) + " from " + repository(event));
    }

    @Override
    public void artifactInstalled(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Installed " + artifact(event) + " to " + event.getFile());
    }

    @Override
    public void artifactInstalling(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Installing " + artifact(event) + " to " + event.getFile());
    }

    @Override
    public void artifactResolved(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Resolved artifact " + artifact(event) + " from " + repository(event));
    }

    @Override
    public void artifactResolving(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Resolving artifact " + artifact(event));
    }

    @Override
    public void metadataDeployed(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Deployed " + metadata(event) + " to " + repository(event));
    }

    @Override
    public void metadataDeploying(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Deploying " + metadata(event) + " to " + repository(event));
    }

    @Override
    public void metadataInstalled(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Installed " + metadata(event) + " to " + event.getFile());
    }

    @Override
    public void metadataInstalling(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Installing " + metadata(event) + " to " + event.getFile());
    }

    @Override
    public void metadataInvalid(RepositoryEvent event)
    {
        requireNonNull(event);
        problem("Invalid metadata " + metadata(event));
    }

    @Override
    public void metadataResolved(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Resolved metadata " + metadata(event) + " from " + repository(event));
    }

    @Override
    public void metadataResolving(RepositoryEvent event)
    {
        requireNonNull(event);
        trace("Resolving metadata " + metadata(event) + " from " + repository(event));
    }
}
