package digital.fiasco.runtime.repository.fiasco;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.fiasco.server.FiascoClient;
import digital.fiasco.runtime.repository.fiasco.server.FiascoRepositoryRequest;

import java.net.URI;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

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
     * Gets the artifacts for the given artifact descriptors
     *
     * @param descriptors The artifact descriptors
     * @return The artifacts
     */
    @Override
    public synchronized ObjectList<Artifact<?>> resolveArtifacts(ObjectList<ArtifactDescriptor> descriptors)
    {
        // Create list of resolved artifacts,
        ObjectList<Artifact<?>> resolved = list();

        // compose request for descriptors,
        var request = new FiascoRepositoryRequest();
        request.addAll(descriptors);

        // make request to server
        var response = new FiascoClient().request(this, request);
        resolved.addAll(response.artifacts());
        return resolved;
    }
}
