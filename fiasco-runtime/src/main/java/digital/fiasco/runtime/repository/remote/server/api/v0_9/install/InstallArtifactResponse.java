package digital.fiasco.runtime.repository.remote.server.api.v0_9.install;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletResponse;
import digital.fiasco.runtime.repository.Repository.InstallResult;
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
    private InstallResult result;

    public InstallArtifactResponse(InstallResult result)
    {
        this.result = result;
    }

    public InstallResult result()
    {
        return result;
    }
}
