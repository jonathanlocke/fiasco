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
import org.eclipse.aether.resolution.DependencyRequest;

import java.net.URI;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.os.Console.console;
import static com.telenav.kivakit.resource.Urls.url;
import static digital.fiasco.runtime.FiascoRuntime.fiascoCacheFolder;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.artifactDescriptor;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.parseArtifactDescriptor;
import static org.eclipse.aether.util.artifact.JavaScopes.COMPILE;
import static org.eclipse.aether.util.filter.DependencyFilterUtils.classpathFilter;

/**
 * Resolves {@link MavenDependency}s for {@link ArtifactDescriptor}s.
 *
 * <p><b>Dependency Resolution</b></p>
 *
 * <ul>
 *     <li>{@link #resolveDependencies(String)} - Resolves the Maven dependencies for the given artifact descriptor</li>
 *     <li>{@link #resolveDependencies(ArtifactDescriptor)} - Resolves the Maven dependencies for the given artifact descriptor</li>
 * </ul>
 *
 * <p><b>Repositories</b></p>
 *
 * <ul>
 *     <li>{@link #withRepository(MavenRepository)} - Returns a copy of this resolver with the given Maven repository added</li>
 *     <li>{@link #withLocalRepository(LocalRepository)} - Returns a copy of this resolver with the given local Maven repostitory added</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
public class MavenResolver extends BaseComponent implements TryTrait
{
    /** The per-thread {@link RepositorySystemSession} object, to maintain thread safety */
    private static final ThreadLocal<RepositorySystemSession> threadLocalSession = new ThreadLocal<>();

    /**
     * Sanity test entrypoint
     *
     * @param arguments Ignored
     */
    public static void main(String[] arguments)
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

    /**
     * Creates a resolver, using the Guice injector {@link MavenResolverGuiceInjector} to configure the
     * {@link RepositorySystem}.
     */
    public MavenResolver()
    {
        system = Guice
            .createInjector(new MavenResolverGuiceInjector())
            .getInstance(RepositorySystem.class);

        repositories = list();
    }

    /**
     * Creates a copy of the given resolver
     *
     * @param that The resolver to copy
     */
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
            var artifactResults = system
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
     * @param repository The repository to append
     * @return The new resolver
     */
    public MavenResolver withRepository(MavenRepository repository)
    {
        var copy = copy();
        var newRepository = newRepository(repository.name(), repository.uri());
        ensure(!repositories.contains(newRepository), "A repository with the name '$' was already added", repository.name());
        copy.repositories.add(newRepository);
        return copy;
    }

    /**
     * Returns a new {@link RemoteRepository} with the given id and location
     *
     * @param id The repository identifier
     * @param uri The repository location
     * @return The new repository
     */
    private RemoteRepository newRepository(String id, URI uri)
    {
        return new RemoteRepository.Builder(id, "default", uri.toString()).build();
    }

    /**
     * Returns the {@link MavenRepository} with the given identifier
     *
     * @param id The identifier
     * @return The repository, or null if a repository with the given identifier cannot be found
     */
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

    /**
     * Creates a thread-local {@link RepositorySystemSession} using the {@link RepositorySystem} created in the
     * constructor. It is necessary to maintain one session per thread because the session object is not thread-safe.
     *
     * @return An existing or new session for this thread
     */
    private RepositorySystemSession session()
    {
        // Get any thread-local session,
        var session = MavenResolver.threadLocalSession.get();

        // and if there is none,
        if (session == null)
        {
            // then create and attach a new session.
            var newSession = MavenRepositorySystemUtils.newSession();
            newSession.setLocalRepositoryManager(system.newLocalRepositoryManager(newSession, localRepository));
            newSession.setTransferListener(new MavenArtifactTransferListener());
            newSession.setRepositoryListener(new MavenRepositoryListener());
            MavenResolver.threadLocalSession.set(session = newSession);
        }

        // Return the session for this thread.
        return session;
    }
}
