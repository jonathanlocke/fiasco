package digital.fiasco.runtime.repository.remote.server.api.resolve;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletResponse;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;

import java.util.ArrayList;
import java.util.List;

/**
 * The response to a {@link ResolveArtifactRequest}, containing a list of artifacts complete with content attachments
 * encoded in Base64.
 *
 * <p><b>Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #artifacts()}</li>
 * </ul>
 *
 * <p><b>Performance</b></p>
 *
 * <p>
 * {@link ResolveArtifactRequest} allows the {@link FiascoClient} to resolve multiple {@link ArtifactDescriptor}s
 * in a single request.
 * </p>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "UnusedReturnValue", "unused", "rawtypes" })
public class ResolveArtifactResponse extends BaseMicroservletResponse
{
    /** The list of artifact content metadata */
    @Expose
    private List<Artifact> artifacts;

    public ResolveArtifactResponse(ArtifactList artifacts)
    {
        this.artifacts = new ArrayList<>();
        this.artifacts.addAll(artifacts.asList());
    }

    /**
     * Returns the artifacts that were resolved by the request associated with this response
     */
    public ArtifactList artifacts()
    {
        return ArtifactList.artifacts(artifacts);
    }
}
