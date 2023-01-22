package digital.fiasco.runtime.repository.remote.server.api.resolve;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletResponse;
import com.telenav.kivakit.resource.writing.WritableResource;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;

import java.io.InputStream;

import static com.telenav.kivakit.resource.CloseMode.LEAVE_OPEN;
import static com.telenav.kivakit.resource.WriteMode.STREAM;

/**
 * The response to a {@link ResolveArtifactRequest}, containing the {@link ArtifactContent} metadata for each artifact
 * requested. The artifact content data follows the response in the {@link InputStream}.
 *
 * <p><b>Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #artifacts()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
public class ResolveArtifactResponse extends BaseMicroservletResponse
{
    /** The list of artifact content metadata */
    @Expose
    private ArtifactList artifacts;

    public ResolveArtifactResponse(ArtifactList artifacts)
    {
        this.artifacts = artifacts;
    }

    /**
     * Returns the artifacts that were resolved by the request associated with this response
     */
    public ArtifactList artifacts()
    {
        return artifacts;
    }

    /**
     * Writes the artifacts in the response to the given output resource
     *
     * @param response The response
     * @param target The output resource
     * @param reporter The reporter to report progress
     */
    private void writeArtifacts(ResolveArtifactResponse response, WritableResource target, ProgressReporter reporter)
    {
        // For each artifact,
        for (var artifact : response.artifacts())
        {
            // and each attached piece of content,
            for (var attachment : artifact.attachments())
            {
                // and copy it back to the requester.
                ((ArtifactAttachment) attachment)
                    .content()
                    .resource()
                    .copyTo(target, STREAM, LEAVE_OPEN, reporter);
            }
        }
    }
}
