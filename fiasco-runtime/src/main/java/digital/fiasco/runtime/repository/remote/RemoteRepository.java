package digital.fiasco.runtime.repository.remote;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.local.CacheRepository;
import digital.fiasco.runtime.repository.Repository;

import java.net.URI;

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
 *     <li>{@link Repository#resolveArtifacts(ObjectList)} - Resolves the given descriptors to a list of {@link Artifact}s, complete with {@link ArtifactContent} attachments</li>
 * </ul>
 *
 * <p><b>Installing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link Repository#resolveArtifacts(ObjectList)} - Resolves the given descriptors to a list of {@link Artifact}s, complete with {@link ArtifactContent} attachments</li>
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installArtifact(Artifact<?> artifact)
    {
        unsupported("Cannot install artifacts in a remote Fiasco repository");
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
    public ArtifactList resolveArtifacts(ObjectList<ArtifactDescriptor> descriptors)
    {
        // Return resolved artifacts for the given descriptors
        return new FiascoClient()
            .request(this, new FiascoRepositoryRequest().with(descriptors))
            .artifacts();
    }
}
