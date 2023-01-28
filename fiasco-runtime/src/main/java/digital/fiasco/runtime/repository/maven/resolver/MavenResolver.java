package digital.fiasco.runtime.repository.maven.resolver;

import com.google.inject.Guice;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
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
import static com.telenav.kivakit.core.os.Console.console;
import static com.telenav.kivakit.resource.Urls.url;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor.descriptor;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor.parseDescriptor;
import static digital.fiasco.runtime.repository.maven.MavenRepository.LOCAL_MAVEN_REPOSITORY_FOLDER;
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
 *     <li>{@link #withMavenRepository(MavenRepository)} - Returns a copy of this resolver with the given Maven repository added</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
public class MavenResolver extends BaseComponent implements TryTrait
{
    /**
     * Sanity test entrypoint
     *
     * @param arguments Ignored
     */
    public static void main(String[] arguments)
    {
        console().println(new MavenResolver(LOCAL_MAVEN_REPOSITORY_FOLDER)
            .resolveDependencies("library:com.telenav.kivakit:kivakit-application:1.9.0")
            .asStringList()
            .join("\n"));
    }

    /** The Maven repository system for resolving artifacts */
    private final RepositorySystem system;

    /** The list of maven repositories to consult */
    private final ObjectList<RemoteRepository> repositories;

    /** The local Maven repository, defaults to ~/.fiasco/maven-repository */
    private LocalRepository localRepository;

    /** The local folder containing this maven repository */
    private final Folder localRepositoryFolder;

    /**
     * Creates a resolver, using the Guice injector {@link MavenResolverGuiceInjector} to configure the
     * {@link RepositorySystem}.
     */
    public MavenResolver(Folder localRepositoryFolder)
    {
        this.repositories = list();
        this.localRepositoryFolder = localRepositoryFolder.mkdirs();

        system = Guice
            .createInjector(new MavenResolverGuiceInjector())
            .getInstance(RepositorySystem.class);
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
        this.localRepositoryFolder = that.localRepositoryFolder;
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
            var artifact = new DefaultArtifact(descriptor.mavenName());

            var collectRequest = new CollectRequest();
            collectRequest.setRoot(new Dependency(artifact, COMPILE));
            collectRequest.setRepositories(repositories);

            var dependencyRequest = new DependencyRequest(collectRequest, classpathFilter(COMPILE));
            var artifactResults = system
                .resolveDependencies(session(), dependencyRequest)
                .getArtifactResults();

            var dependencies = new ObjectList<MavenDependency>();
            for (var artifactResult : artifactResults)
            {
                var mavenArtifact = artifactResult.getArtifact();
                var mavenArtifactDescriptor = mavenArtifact.getGroupId()
                    + ":" + mavenArtifact.getArtifactId()
                    + ":" + mavenArtifact.getVersion();
                var mavenRepository = repository(artifactResult.getRepository().getId());
                var fiascoDescriptor = parseDescriptor(this, "library:" + mavenArtifactDescriptor);
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
        return resolveDependencies(descriptor(descriptor));
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
    public MavenResolver withMavenRepository(MavenRepository repository)
    {
        var copy = copy();
        var newRepository = newRepository(repository.name(), repository.uri());
        if (!repositories.contains(newRepository))
        {
            copy.repositories.add(newRepository);
        }
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
                    return new MavenRepository(at.getId(), url(at.getUrl()).toURI(), localRepositoryFolder);
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
        // then create and attach a new session.
        var newSession = MavenRepositorySystemUtils.newSession();
        newSession.setLocalRepositoryManager(system.newLocalRepositoryManager(newSession, localRepository));
        newSession.setTransferListener(new MavenArtifactTransferListener());
        newSession.setRepositoryListener(new MavenRepositoryListener());
        return newSession;
    }
}
