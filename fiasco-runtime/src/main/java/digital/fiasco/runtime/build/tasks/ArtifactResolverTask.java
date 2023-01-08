package digital.fiasco.runtime.build.tasks;

import com.telenav.kivakit.core.messaging.listeners.MessageList;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.processing.Task;
import digital.fiasco.runtime.dependency.processing.TaskResult;

import static digital.fiasco.runtime.dependency.processing.TaskResult.taskResult;

/**
 * A task that can be executed in parallel that resolves the given artifacts using the given {@link Librarian}. As each
 * group of artifacts is resolved, they are marked as resolved by calling
 * {@link ResolvedArtifacts#markResolved(DependencyList)}.
 *
 * @param librarian The librarian to resolve artifacts
 * @param artifacts The artifacts to resolve
 * @param resolved The set of resolved artifacts to update
 */
public record ArtifactResolverTask(Librarian librarian,
                                   DependencyList<Artifact> artifacts,
                                   ResolvedArtifacts resolved)
    implements Task<DependencyList<Artifact>>
{
    @Override
    public TaskResult<DependencyList<Artifact>> call()
    {
        // Get the list of descriptors to resolve,
        var descriptors = artifacts.artifactDescriptors();

        // ask the librarian to resolve them,
        var issues = new MessageList();
        issues.capture(() -> librarian.resolve(descriptors),
            "Unable to resolve artifacts: $", artifacts);

        // then mark the artifacts as resolved (potentially allowing a builder that is waiting
        // for the artifacts to start running),
        resolved.markResolved(artifacts);

        // and return any issues encountered while resolving the artifacts.
        return taskResult(artifacts, issues);
    }

    @Override
    public String name()
    {
        return artifacts.asString();
    }
}
