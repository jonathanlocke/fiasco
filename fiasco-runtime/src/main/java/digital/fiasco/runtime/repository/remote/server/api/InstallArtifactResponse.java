package digital.fiasco.runtime.repository.remote.server.api;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletResponse;
import digital.fiasco.runtime.repository.Repository.InstallationResult;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Response to an {@link InstallArtifactRequest}.
 * </p>
 *
 * @author Jonathan Locke
 * @see FiascoClient
 * @see InstallArtifactRequest
 */
public class InstallArtifactResponse extends BaseMicroservletResponse
{
    /** The installation result */
    @Expose
    private final InstallationResult result;

    public InstallArtifactResponse(InstallationResult result)
    {
        this.result = result;
    }

    public InstallationResult result()
    {
        return result;
    }
}
