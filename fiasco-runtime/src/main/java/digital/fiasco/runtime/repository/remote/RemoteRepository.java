package digital.fiasco.runtime.repository.remote;

import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.local.cache.CacheRepository;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;
import digital.fiasco.runtime.repository.remote.server.FiascoServer;

import java.net.URI;
import java.util.Collection;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * A remote Fiasco repository accessed with a {@link FiascoClient}. Artifacts in this repository are resolved using a
 * {@link FiascoClient} to make JSON requests to the remote {@link FiascoServer} at the given {@link URI}. Installing
 * artifacts on a remote fiasco repository is not supported.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #name()}</li>
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
 *     <li>{@link Repository#installArtifact(Artifact)} - Installs the given artifact</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see BaseRepository
 * @see Repository
 * @see CacheRepository
 * @see FiascoClient
 * @see FiascoServer
 */
public class RemoteRepository extends BaseRepository
{
    /**
     * Creates a remote fiasco repository accessed with a {@link FiascoClient}
     *
     * @param name The name of the repository
     * @param uri The location of the remote repository
     */
    public RemoteRepository(String name, URI uri)
    {
        super(name, uri);
        ensure(uri.getScheme().equals("https"), "HTTPS is required");
    }

    @Override
    public RemoteRepository clear()
    {
        unsupported();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstallResult installArtifact(Artifact<?> artifact)
    {
        return new FiascoClient().installArtifact(artifact);
    }

    @Override
    public boolean isRemote()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArtifactList resolveArtifacts(Collection<ArtifactDescriptor> descriptors)
    {
        // Return resolved artifacts for the given descriptors
        return new FiascoClient().resolveArtifacts(descriptors);
    }

    @Override
    protected void loadAllArtifactMetadata()
    {
        unsupported();
    }
}
