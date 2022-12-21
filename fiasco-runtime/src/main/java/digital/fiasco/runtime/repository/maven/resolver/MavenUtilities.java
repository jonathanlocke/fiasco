package digital.fiasco.runtime.repository.maven.resolver;

import org.eclipse.aether.RepositoryEvent;

public class MavenUtilities
{
    public static String artifact(RepositoryEvent event)
    {
        var artifact = event.getArtifact();
        return artifact.getGroupId()
                + ":" + artifact.getArtifactId()
                + ":" + artifact.getVersion();
    }

    public static String metadata(RepositoryEvent event)
    {
        var metadata = event.getMetadata();
        return metadata.getGroupId()
                + ":" + metadata.getArtifactId()
                + ":" + metadata.getVersion();
    }

    public static String repository(RepositoryEvent event)
    {
        return event.getRepository().getId();
    }

    public static String repositoryAndArtifact(RepositoryEvent event)
    {
        return repository(event) + "/" + artifact(event);
    }

    public static String repositoryAndMetadata(RepositoryEvent event)
    {
        return metadata(event) + "/" + artifact(event);
    }
}
