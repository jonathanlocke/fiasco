package digital.fiasco.runtime.repository;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.thread.locks.ReadWriteLock;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.fiasco.CacheRepository;
import digital.fiasco.runtime.repository.fiasco.LocalRepository;
import digital.fiasco.runtime.repository.fiasco.RemoteRepository;
import digital.fiasco.runtime.repository.fiasco.server.FiascoClient;
import digital.fiasco.runtime.repository.fiasco.server.FiascoServer;
import digital.fiasco.runtime.repository.maven.MavenRepository;

import java.net.URI;
import java.util.Objects;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * Base class for repositories of artifacts and their metadata. Subclasses include:
 *
 * <ul>
 *     <li>{@link CacheRepository} - High performance artifact store</li>
 *     <li>{@link LocalRepository} - Stores artifacts on the local filesystem</li>
 *     <li>{@link MavenRepository} - A remote or local maven repository</li>
 *     <li>{@link RemoteRepository} - Fiasco repository at a remote URI</li>
 * </ul>
 *
 * <p><b>Local Repositories</b></p>
 *
 * <p>
 * {@link LocalRepository} is used to store artifacts and metadata on the local filesystem. Artifact
 * metadata is stored in a human-readable, append-only text file called <i>artifacts.txt</i>, which allows it to be searched
 * with grep or viewed in a text editor. The artifact content attachments are stored on the filesystem in a hierarchical
 * format similar to a Maven repository. For access to Maven repositories, see {@link MavenRepository}.
 * </p>
 *
 * <p><b>Maven Repositories</b></p>
 *
 * <p>
 * {@link MavenRepository} is used to access local or remote repositories in Apache Maven format.
 * </p>
 *
 * <p><b>Cache Repositories</b></p>
 *
 * <p>
 * {@link CacheRepository} is used to store artifacts and their metadata in a single file to allow
 * high-performance, random access. As with a {@link LocalRepository}, metadata is stored in a single append-only
 * text file, but artifact content attachments are stored end-to-end in a single file, <i>attachments.binary</i>. This
 * repository is used as a download cache to avoid unnecessary downloads when a user wipes out their local repository, causing
 * it to repopulate. Instead of repopulating from Maven Central or another remote repository, the artifacts in this
 * cache can be used. Because downloaded artifacts are not mutable in Maven Central (and should not be mutable in any
 * other repository), it should rarely be necessary to remove the download repository. A cache repository is also used
 * by {@link digital.fiasco.runtime.repository.fiasco.server.FiascoServer} to enable quick response times to requests for artifacts.
 * </p>
 *
 * <p><b>Remote Repositories</b></p>
 *
 * <p>
 * {@link RemoteRepository} is used to access remote Fiasco repositories served by a {@link FiascoServer}.
 * Internally, this repository used {@link FiascoClient} to communicate with the server.
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
 *     <li>{@link Repository#resolveArtifacts(ObjectList)} - Resolves the given descriptors to a list of {@link Artifact}s, complete with {@link ArtifactContent} attachments</li>
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
    private final String name;

    /** The location of this repository */
    private final URI uri;

    /** The cached artifact entries */
    private final ObjectMap<ArtifactDescriptor, Artifact> artifacts = new ObjectMap<>();

    /** Cache lock (filesystem locking not yet supported) */
    private final ReadWriteLock lock = new ReadWriteLock();

    /**
     * Creates a maven repository
     */
    public BaseRepository(String name, URI uri)
    {
        this.name = name;
        this.uri = uri;
    }

    /**
     * Returns true if this repository contains the given artifact
     *
     * @param artifact The artifact
     * @return True if it is in this repository
     */
    public boolean contains(Artifact artifact)
    {
        return artifacts.containsValue(artifact);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof MavenRepository that)
        {
            return this.name().equals(that.name());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI uri()
    {
        return uri;
    }

    /**
     * Returns the map from descriptor to artifact
     */
    protected ObjectMap<ArtifactDescriptor, Artifact> artifacts()
    {
        return artifacts;
    }

    /**
     * Returns the read/write lock for this repository
     */
    protected ReadWriteLock lock()
    {
        return lock;
    }

    protected ObjectList<Artifact> resolve(Iterable<ArtifactDescriptor> descriptors)
    {
        return list(descriptors).map(artifacts::get);
    }
}
