package digital.fiasco.runtime.build.tasks;

import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.processing.Task;
import digital.fiasco.runtime.dependency.processing.TaskResult;

/**
 * A {@link Task} that runs a {@link Builder}, which can be executed in parallel with other builders. The task waits for
 * the set of artifacts required by the builder to be resolved by one or more {@link ArtifactResolverTask}s before
 * running the builder.
 *
 * @param builder The builder to run
 * @param resolved The artifacts that must be resolved before running
 */
public record BuilderTask(Builder builder,
                          ResolvedArtifacts resolved)
    implements Task<Builder>
{
    @Override
    public TaskResult<Builder> call()
    {
        // Wait for all artifact dependencies to be resolved,
        resolved.waitFor(builder.dependencies(Artifact.class));

        // then run the builder.
        return builder.build();
    }

    @Override
    public String name()
    {
        return builder.name();
    }
}
