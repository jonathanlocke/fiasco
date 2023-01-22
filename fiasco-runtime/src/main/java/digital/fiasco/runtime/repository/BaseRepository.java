package digital.fiasco.runtime.repository;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.thread.locks.ReadWriteLock;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.repository.local.LocalRepository;
import digital.fiasco.runtime.repository.local.cache.CacheRepository;
import digital.fiasco.runtime.repository.maven.MavenRepository;
import digital.fiasco.runtime.repository.remote.RemoteRepository;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;
import digital.fiasco.runtime.repository.remote.server.FiascoServer;

import java.net.URI;
import java.util.Collection;
import java.util.Objects;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifacts;

/**
 * Base class for repositories of artifacts and their metadata. Subclasses include:
 *
 * <ul>
 *     <li>{@link CacheRepository} - High performance artifact store</li>
 *     <li>{@link LocalRepository} - Stores artifacts on the local filesystem</li>
 *     <li>{@link MavenRepository} - A remote or local maven repository</li>
 *     <li>{@link RemoteRepository} - A Fiasco repository at a remote URI</li>
 * </ul>
 *
 * <p><b>Local Repositories</b></p>
 *
 * <p>
 * {@link LocalRepository} is used to store artifacts and metadata on the local filesystem. Artifact
 * metadata is stored in JSON format in an append-only text file called <i>artifacts.txt</i>, which allows
 * it to be searched with grep or viewed in a text editor. The artifact content attachments are stored
 * on the filesystem in a hierarchical format similar to a Maven repository.
 * </p>
 *
 * <p><b>Maven Repositories</b></p>
 *
 * <p>
 * {@link MavenRepository} is used to access local or remote Apache Maven repositories. Support for
 * Maven POM files is limited since only basic metadata and dependencies are required by Fiasco.
 * </p>
 *
 * <p><b>Cache Repositories</b></p>
 *
 * <p>
 * {@link CacheRepository} is used to store artifacts and their metadata in a single file to allow
 * high-performance, random access. As with a {@link LocalRepository}, metadata is stored in a single append-only
 * text file, but artifact content attachments are stored end-to-end in a single file, <i>attachments.binary</i>.
 * </p>
 *
 * <p>
 * An instance of {@link CacheRepository} is used as an artifact cache to avoid unnecessary downloads when a user wipes
 * out their {@link LocalRepository}, causing it to repopulate. Instead of repopulating from Maven Central or another
 * remote repository, the artifacts in this repository can be used since artifacts and their metadata are never altered,
 * only appended to their respective <i>artifacts.txt</i> and <i>artifact-content.binary</i>files. Because remote
 * artifacts are guaranteed by Maven Central (and other remote repositories) to be immutable, it should rarely be
 * necessary to remove a download cache repository.
 * </p>
 *
 * <p>
 * Another instance of {@link CacheRepository} is used by {@link FiascoServer} to respond quickly to requests to
 * resolve one or more artifact descriptors.
 * </p>
 *
 * <p><b>Remote Repositories</b></p>
 *
 * <p>
 * {@link RemoteRepository} is used to access a remote {@link FiascoServer} (launched by the <i>fiasco-server</i> project).
 * Internally, this repository uses {@link FiascoClient} to communicate with the server.
 * </p>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #uri()}</li>
 * </ul>
 *
 * <p><b>Retrieving Artifacts and Content</b></p>
 *
 * <ul>
 *     <li>{@link Repository#resolveArtifacts(Collection)} - Resolves the given descriptors to a list of {@link Artifact}s, complete with {@link ArtifactContent} attachments</li>
 * </ul>
 *
 * <p><b>Installing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #installArtifact(Artifact)} - Installs the given artifact in this repository, along with its attached resources</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @author Jonathan Locke
 * @see CacheRepository
 * @see LocalRepository
 * @see RemoteRepository
 * @see MavenRepository
 */
public abstract class BaseRepository extends BaseRepeater implements Repository
{
    /** The name of this repository */
    @Expose
    private final String name;

    /** The location of this repository */
    @Expose
    private final URI uri;

    /** The cached artifact entries */
    private transient ObjectMap<ArtifactDescriptor, Artifact<?>> descriptorToArtifact;

    /** Cache lock (filesystem locking not yet supported) */
    private transient final ReadWriteLock lock = new ReadWriteLock();

    /**
     * Creates a maven repository
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public BaseRepository(String name, URI uri)
    {
        this.name = name;
        this.uri = uri;
    }

    @Override
    public BaseRepository clear()
    {
        lock().write(() -> descriptorToArtifact = null);
        return this;
    }

    /**
     * Returns true if this repository contains the given artifact
     *
     * @param artifact The artifact
     * @return True if it is in this repository
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean contains(Artifact<?> artifact)
    {
        return lock().read(() -> descriptorToArtifact().containsValue(artifact));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTED)
    public boolean equals(Object object)
    {
        if (object instanceof BaseRepository that)
        {
            return this.name().equals(that.name());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTED)
    public int hashCode()
    {
        return Objects.hash(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTED)
    public String name()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public URI uri()
    {
        return uri;
    }

    protected void add(ArtifactDescriptor descriptor, Artifact<?> artifact)
    {
        descriptorToArtifact().put(ensureNotNull(descriptor), ensureNotNull(artifact));
    }

    protected abstract void loadAllArtifactMetadata();

    /**
     * Returns the read/write lock for this repository
     */
    protected ReadWriteLock lock()
    {
        return lock;
    }

    protected ArtifactList resolve(Iterable<ArtifactDescriptor> descriptors)
    {
        return lock().read(() ->
        {
            var resolved = artifacts();
            for (var descriptor : descriptors)
            {
                resolved = resolved.with(matching(descriptor));
            }
            return resolved;
        });
    }

    private synchronized ObjectMap<ArtifactDescriptor, Artifact<?>> descriptorToArtifact()
    {
        if (descriptorToArtifact == null)
        {
            descriptorToArtifact = new ObjectMap<>();
            loadAllArtifactMetadata();
        }
        return descriptorToArtifact;
    }

    private ArtifactList matching(ArtifactDescriptor descriptor)
    {
        return lock().read(() ->
        {
            var matches = artifacts();
            for (var at : descriptorToArtifact().values())
            {
                if (descriptor.matches(at.descriptor()))
                {
                    matches = matches.with(at);
                }
            }
            return matches;
        });
    }
}
