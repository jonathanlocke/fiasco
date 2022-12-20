package digital.fiasco.runtime.repository.maven.resolver;

import com.google.inject.Guice;
import com.telenav.kivakit.core.language.trait.TryCatchTrait;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;

import java.util.List;

public class MavenResolver implements TryCatchTrait
{
    public static void main(String[] args)
    {
        new MavenResolver().resolve();
    }

    private final RepositorySystem system;

    private final RepositorySystemSession session;

    public MavenResolver()
    {
        system = Guice.createInjector(new MavenResolverGuiceModule()).getInstance(RepositorySystem.class);
        session = newSession();
    }

    public void resolve()
    {
        tryCatch(() ->
        {
            var artifact = new DefaultArtifact("org.apache.maven.resolver:maven-resolver-impl:1.3.3");

            var classpathFlter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE);

            var collectRequest = new CollectRequest();
            collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
            collectRequest.setRepositories(newRepositories());

            DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, classpathFlter);

            List<ArtifactResult> artifactResults =
                    system.resolveDependencies(session, dependencyRequest).getArtifactResults();

            for (ArtifactResult artifactResult : artifactResults)
            {
                System.out.println(artifactResult.getArtifact() + " resolved to "
                        + artifactResult.getArtifact().getFile());
            }
        });
    }

    private static RemoteRepository newCentralRepository()
    {
        return new RemoteRepository.Builder("central", "default", "https://repo.maven.apache.org/maven2/").build();
    }

    private List<RemoteRepository> newRepositories()
    {
        return List.of(newCentralRepository());
    }

    private RepositorySystemSession newSession()
    {
        var session = MavenRepositorySystemUtils.newSession();

        var localRepo = new LocalRepository("target/local-repo");
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

        session.setTransferListener(new ConsoleTransferListener());
        session.setRepositoryListener(new ConsoleRepositoryListener());
        return session;
    }
}
