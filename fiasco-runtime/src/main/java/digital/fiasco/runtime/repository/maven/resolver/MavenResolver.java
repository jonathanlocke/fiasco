package digital.fiasco.runtime.repository.maven.resolver;

import com.google.inject.Guice;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.trait.TryTrait;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.maven.MavenRepository;
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

import java.net.URI;
import java.util.List;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.os.Console.console;
import static com.telenav.kivakit.resource.Urls.url;
import static digital.fiasco.runtime.FiascoRuntime.fiascoCacheFolder;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.artifactDescriptor;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.parseArtifactDescriptor;
import static org.eclipse.aether.util.artifact.JavaScopes.COMPILE;
import static org.eclipse.aether.util.filter.DependencyFilterUtils.classpathFilter;

/**
 * Resolves Maven dependencies
 */
public class MavenResolver extends BaseComponent implements TryTrait
{
    /** The per-thread {@link RepositorySystemSession} object, to maintain thread safety */
    private static final ThreadLocal<RepositorySystemSession> session = new ThreadLocal<>();

    public static void main(String[] args)
    {
        console().println(new MavenResolver()
                .resolveDependencies("com.telenav.kivakit:kivakit-application:1.9.0")
                .asStringList()
                .join("\n"));
    }

    /** The Maven repository system for resolving artifacts */
    private final RepositorySystem system;

    /** The list of maven repositories to consult */
    private final ObjectList<RemoteRepository> repositories;

    /** The local Maven repository, defaults to ~/.fiasco/maven-repository */
    private LocalRepository localRepository = new LocalRepository(fiascoCacheFolder()
            .folder("maven-repository")
            .toString());

    public MavenResolver()
    {
        system = Guice
                .createInjector(new MavenResolverGuiceModule())
                .getInstance(RepositorySystem.class);

        repositories = list();
    }

    protected MavenResolver(MavenResolver that)
    {
        this.system = that.system;
        this.repositories = that.repositories.copy();
        this.localRepository = that.localRepository;
    }

    /**
     * Returns a copy of this resolver
     *
     * @return The copy
     */
    public MavenResolver copy()
    {
        return new MavenResolver(this);
    }

    /**
     * Resolves the dependencies for the given artifact descripttor
     *
     * @param descriptor The descriptor
     * @return The list of resolved dependencies
     */
    public ObjectList<MavenDependency> resolveDependencies(ArtifactDescriptor descriptor)
    {
        return tryCatch(() ->
        {
            var artifact = new DefaultArtifact(descriptor.name());

            var collectRequest = new CollectRequest();
            collectRequest.setRoot(new Dependency(artifact, COMPILE));
            collectRequest.setRepositories(repositories);

            DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, classpathFilter(COMPILE));
            List<ArtifactResult> artifactResults = system
                    .resolveDependencies(session(), dependencyRequest)
                    .getArtifactResults();

            ObjectList<MavenDependency> dependencies = list();
            for (var artifactResult : artifactResults)
            {
                var mavenArtifact = artifactResult.getArtifact();
                var mavenArtifactDescriptor = mavenArtifact.getGroupId()
                        + ":" + mavenArtifact.getArtifactId()
                        + ":" + mavenArtifact.getVersion();
                var mavenRepository = repository(artifactResult.getRepository().getId());
                var fiascoDescriptor = parseArtifactDescriptor(this, mavenArtifactDescriptor);
                if (mavenRepository != null && fiascoDescriptor != null)
                {
                    dependencies.add(new MavenDependency(mavenRepository, fiascoDescriptor));
                }
            }
            return dependencies;
        }, "Could not resolve dependencies for: $", descriptor);
    }

    /**
     * Resolves the dependencies for the given artifact descripttor
     *
     * @param descriptor The descriptor
     * @return The list of resolved dependencies
     */
    public ObjectList<MavenDependency> resolveDependencies(String descriptor)
    {
        return resolveDependencies(artifactDescriptor(descriptor));
    }

    /**
     * Returns a copy of this resolver with the given Maven local respository
     *
     * @param localRepository The local repository
     * @return The new resolver
     */
    public MavenResolver withLocalRepository(LocalRepository localRepository)
    {
        var copy = copy();
        copy.localRepository = localRepository;
        return copy;
    }

    /**
     * Returns a copy of this resolver with the given Maven local respository
     *
     * @param name The repository name
     * @param uri The repository URI
     * @return The new resolver
     */
    public MavenResolver withRepository(MavenRepository repository)
    {
        var copy = copy();
        copy.repositories.add(newRepository(repository.name(), repository.uri()));
        return copy;
    }

    private RemoteRepository newRepository(String id, URI uri)
    {
        return new RemoteRepository.Builder(id, "default", uri.toString()).build();
    }

    private MavenRepository repository(String id)
    {
        return tryCatch(() ->
        {
            for (var at : repositories)
            {
                if (at.getId().equals(id))
                {
                    return new MavenRepository(at.getId(), url(at.getUrl()).toURI());
                }
            }
            return null;
        });
    }

    private RepositorySystemSession session()
    {
        RepositorySystemSession session = MavenResolver.session.get();
        if (session == null)
        {
            var newSession = MavenRepositorySystemUtils.newSession();
            newSession.setLocalRepositoryManager(system.newLocalRepositoryManager(newSession, localRepository));
            newSession.setTransferListener(new MavenArtifactTransferListener());
            newSession.setRepositoryListener(new MavenRepositoryListener());
            MavenResolver.session.set(session = newSession);
        }
        return session;
    }
}
