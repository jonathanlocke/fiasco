package digital.fiasco.runtime.repository.maven.resolver;

import com.telenav.kivakit.component.ComponentMixin;
import org.eclipse.aether.AbstractRepositoryListener;
import org.eclipse.aether.RepositoryEvent;

import static digital.fiasco.runtime.repository.maven.resolver.MavenUtilities.artifact;
import static digital.fiasco.runtime.repository.maven.resolver.MavenUtilities.metadata;
import static digital.fiasco.runtime.repository.maven.resolver.MavenUtilities.repositoryAndArtifact;
import static digital.fiasco.runtime.repository.maven.resolver.MavenUtilities.repositoryAndMetadata;

/**
 * A simplistic repository listener that logs events to the console.
 */
public class MavenRepositoryListener extends AbstractRepositoryListener implements ComponentMixin
{
    @Override
    public void artifactDeployed(RepositoryEvent event)
    {
        trace("Deployed " + repositoryAndArtifact(event));
    }

    @Override
    public void artifactDeploying(RepositoryEvent event)
    {
        trace("Deploying " + repositoryAndArtifact(event));
    }

    @Override
    public void artifactDescriptorInvalid(RepositoryEvent event)
    {
        problem("Invalid descriptor for " + artifact(event) + ": " + event.getException().getMessage());
    }

    @Override
    public void artifactDescriptorMissing(RepositoryEvent event)
    {
        problem("Missing descriptor for " + artifact(event));
    }

    @Override
    public void artifactDownloaded(RepositoryEvent event)
    {
        trace("Downloaded " + repositoryAndArtifact(event));
    }

    @Override
    public void artifactDownloading(RepositoryEvent event)
    {
        information("Downloading " + repositoryAndArtifact(event));
    }

    @Override
    public void artifactInstalled(RepositoryEvent event)
    {
        trace("Installed " + artifact(event) + " => " + event.getFile());
    }

    @Override
    public void artifactInstalling(RepositoryEvent event)
    {
        trace("Installing " + artifact(event) + " => " + event.getFile());
    }

    @Override
    public void artifactResolved(RepositoryEvent event)
    {
        trace("Resolved " + repositoryAndArtifact(event));
    }

    @Override
    public void artifactResolving(RepositoryEvent event)
    {
        trace("Resolving " + artifact(event));
    }

    @Override
    public void metadataDeployed(RepositoryEvent event)
    {
        trace("Deployed " + repositoryAndMetadata(event));
    }

    @Override
    public void metadataDeploying(RepositoryEvent event)
    {
        trace("Deploying " + repositoryAndMetadata(event));
    }

    @Override
    public void metadataInstalled(RepositoryEvent event)
    {
        trace("Installed " + metadata(event) + " => " + event.getFile());
    }

    @Override
    public void metadataInstalling(RepositoryEvent event)
    {
        trace("Installing " + metadata(event) + " => " + event.getFile());
    }

    @Override
    public void metadataInvalid(RepositoryEvent event)
    {
        problem("Invalid metadata " + metadata(event));
    }

    @Override
    public void metadataResolved(RepositoryEvent event)
    {
        trace("Resolved metadata " + repositoryAndMetadata(event));
    }

    @Override
    public void metadataResolving(RepositoryEvent event)
    {
        trace("Resolving metadata " + repositoryAndMetadata(event));
    }
}
