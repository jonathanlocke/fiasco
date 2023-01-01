package digital.fiasco.runtime.repository.fiasco;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.fiasco.server.FiascoClient;
import digital.fiasco.runtime.repository.fiasco.server.FiascoRepositoryRequest;
import digital.fiasco.runtime.repository.fiasco.server.FiascoServer;

import java.net.URI;
import java.util.Collection;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * A remote Fiasco repository. Artifacts in this repository are resolved using a {@link FiascoClient} to make JSON
 * requests to the {@link FiascoServer}. Neither installing artifacts nor clearing a remote repository is supported.
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
 *     <li>{@link #resolveArtifacts(Collection)} - Gets the {@link Artifact} for the given descriptor</li>
 * </ul>
 *
 * <p><b>Adding and Removing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #installArtifact(Artifact)} - Adds the given artifact with the given attached resources</li>
 *     <li>{@link #clearArtifacts()} - Removes all data from this repository</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see BaseRepository
 * @see Repository
 * @see CacheFiascoRepository
 * @see FiascoClient
 * @see FiascoServer
 */
public class RemoteFiascoRepository extends BaseRepository
{
    public RemoteFiascoRepository(String name, URI uri)
    {
        super(name, uri);
    }

    @Override
    public void clearArtifacts()
    {
        unsupported("Cannot clear a remote Fiasco repository");
    }

    @Override
    public void installArtifact(Artifact artifact)
    {
        unsupported("Cannot install artifacts in a remote Fiasco repository");
    }

    @Override
    public boolean isRemote()
    {
        return true;
    }

    /**
     * Gets the artifacts for the given artifact descriptors
     *
     * @param descriptors The artifact descriptors
     * @return The artifacts
     */
    @Override
    public synchronized ObjectList<Artifact> resolveArtifacts(Collection<ArtifactDescriptor> descriptors)
    {
        // Create list of resolved artifacts,
        ObjectList<Artifact> resolved = list();

        // compose request for descriptors,
        var request = new FiascoRepositoryRequest();
        request.addAll(descriptors);

        // make request to server
        var response = new FiascoClient().request(this, request);
        resolved.addAll(response.artifacts());
        return resolved;
    }
}
