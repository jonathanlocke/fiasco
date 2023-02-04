package digital.fiasco.runtime.repository.maven.resolver;

import com.telenav.kivakit.component.ComponentMixin;
import org.eclipse.aether.AbstractRepositoryListener;
import org.eclipse.aether.RepositoryEvent;

/**
 * A repository listener that broadcasts messages as repository events occur.
 *
 * @author Jonathan Locke
 */
public class MavenRepositoryListener extends AbstractRepositoryListener implements ComponentMixin
{
    @Override
    public void artifactDeployed(RepositoryEvent event)
    {
        traceCall("Deployed " + repositoryAndArtifact(event));
    }

    @Override
    public void artifactDeploying(RepositoryEvent event)
    {
        traceCall("Deploying " + repositoryAndArtifact(event));
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
        traceCall("Downloaded " + repositoryAndArtifact(event));
    }

    @Override
    public void artifactDownloading(RepositoryEvent event)
    {
        information("Downloading " + repositoryAndArtifact(event));
    }

    @Override
    public void artifactInstalled(RepositoryEvent event)
    {
        traceCall("Installed " + artifact(event) + " => " + event.getFile());
    }

    @Override
    public void artifactInstalling(RepositoryEvent event)
    {
        traceCall("Installing " + artifact(event) + " => " + event.getFile());
    }

    @Override
    public void artifactResolved(RepositoryEvent event)
    {
        traceCall("Resolved " + repositoryAndArtifact(event));
    }

    @Override
    public void artifactResolving(RepositoryEvent event)
    {
        traceCall("Resolving " + artifact(event));
    }

    @Override
    public void metadataDeployed(RepositoryEvent event)
    {
        traceCall("Deployed " + repositoryAndMetadata(event));
    }

    @Override
    public void metadataDeploying(RepositoryEvent event)
    {
        traceCall("Deploying " + repositoryAndMetadata(event));
    }

    @Override
    public void metadataInstalled(RepositoryEvent event)
    {
        traceCall("Installed " + metadata(event) + " => " + event.getFile());
    }

    @Override
    public void metadataInstalling(RepositoryEvent event)
    {
        traceCall("Installing " + metadata(event) + " => " + event.getFile());
    }

    @Override
    public void metadataInvalid(RepositoryEvent event)
    {
        problem("Invalid metadata " + metadata(event));
    }

    @Override
    public void metadataResolved(RepositoryEvent event)
    {
        traceCall("Resolved metadata " + repositoryAndMetadata(event));
    }

    @Override
    public void metadataResolving(RepositoryEvent event)
    {
        traceCall("Resolving metadata " + repositoryAndMetadata(event));
    }

    private String artifact(RepositoryEvent event)
    {
        var artifact = event.getArtifact();
        return "library"
            + ":" + artifact.getGroupId()
            + ":" + artifact.getArtifactId()
            + ":" + artifact.getVersion();
    }

    private String metadata(RepositoryEvent event)
    {
        var metadata = event.getMetadata();
        return "library"
            + ":" + metadata.getGroupId()
            + ":" + metadata.getArtifactId()
            + ":" + metadata.getVersion();
    }

    private String repository(RepositoryEvent event)
    {
        return event.getRepository().getId();
    }

    private String repositoryAndArtifact(RepositoryEvent event)
    {
        return repository(event) + "/" + artifact(event);
    }

    private String repositoryAndMetadata(RepositoryEvent event)
    {
        return metadata(event) + "/" + artifact(event);
    }

    private void traceCall(String message, Object... arguments)
    {
        trace("MavenRepositoryListener: " + message, arguments);
    }
}
