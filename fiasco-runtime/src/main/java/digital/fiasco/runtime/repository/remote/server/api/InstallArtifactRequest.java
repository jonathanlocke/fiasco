package digital.fiasco.runtime.repository.remote.server.api;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.repository.local.FiascoUserRepository;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;
import digital.fiasco.runtime.repository.remote.server.FiascoServer;

/**
 * <b>Not public API</b>
 *
 * <p>
 * A request to a {@link FiascoServer} to install an artifact.
 * </p>
 *
 * @author Jonathan Locke
 * @see FiascoClient
 * @see InstallArtifactResponse
 */
public class InstallArtifactRequest extends BaseMicroservletRequest
{
    /** The artifact to install */
    @Expose
    private final Artifact<?> artifact;

    public InstallArtifactRequest(Artifact<?> artifact)
    {
        this.artifact = artifact;
    }

    public InstallArtifactRequest()
    {
        this.artifact = null;
    }

    @Override
    public MicroservletResponse onRespond()
    {
        return new InstallArtifactResponse(require(FiascoUserRepository.class)
            .installArtifact(artifact));
    }

    @Override
    public Class<? extends MicroservletResponse> responseType()
    {
        return InstallArtifactResponse.class;
    }

    @Override
    public String toString()
    {
        return artifact.name();
    }
}
