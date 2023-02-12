package digital.fiasco.runtime.build.execution;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.language.trait.TryTrait;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.settings.BuildSettings;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.resolver.ArtifactResolutionTracker;
import digital.fiasco.runtime.dependency.artifact.resolver.ArtifactResolver;
import digital.fiasco.runtime.dependency.collections.ArtifactList;
import digital.fiasco.runtime.dependency.collections.BaseDependencyList;
import digital.fiasco.runtime.dependency.collections.DependencyQueue;
import digital.fiasco.runtime.dependency.collections.DependencyTree;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.telenav.kivakit.core.thread.Threads.shutdownAndAwaitTermination;
import static com.telenav.kivakit.core.thread.Threads.threadPool;

/**
 * Runs a parallel build for the build tree with the given root builder. The root builder's settings provide the number
 * of threads to use with {@link BuildSettings#builderThreads()}. Artifacts are resolved in parallel by an
 * {@link ArtifactResolver}, which uses {@link BuildSettings#resolverThreads()} threads. Both the artifact resolver and
 * this build executor are greedy and begin work as soon as it is possible.
 *
 * <p>
 * A build is defined by a dependency tree of builders and artifacts starting at the root {@link Builder}. Each builder
 * in the tree can have both {@link Builder} and {@link Artifact} dependencies. However, artifacts can only have other
 * artifacts as dependencies.
 * </p>
 *
 * <p><b>Build Execution</b></p>
 *
 * <p>
 * Builds are executed by two thread pools. One thread pool resolves artifacts, and the other runs builders. A
 * {@link ArtifactResolutionTracker} object serves as a way to communicate between the two thread pools. As artifacts
 * are resolved by the {@link ArtifactResolver}, they are marked as resolved with
 * {@link ArtifactResolutionTracker#resolved(ArtifactList)}. Builders can wait for their artifacts to be resolved by
 * calling {@link ArtifactResolutionTracker#waitForResolutionOf(ArtifactList)}. In this way, artifact resolution
 * proceeds independently of building, with builders blocking only when it is necessary to wait for artifacts to be
 * resolved.
 * </p>
 *
 * @author Jonathan Locke
 * @see ArtifactResolutionTracker
 * @see Result
 * @see Builder
 * @see Dependency
 * @see BaseDependencyList
 * @see DependencyTree
 * @see DependencyQueue
 * @see Artifact
 * @see ArtifactList
 * @see ArtifactResolver
 */
public class BuildExecutor extends BaseComponent implements TryTrait
{
    /** The build to execute */
    private final Build build;

    /**
     * Creates a build executor for the given build
     *
     * @param build The build
     */
    public BuildExecutor(Build build)
    {
        this.build = build;

        build.listenTo(this);
    }

    /**
     * Runs builders and resolves artifacts, returning a list of results, one for each builder.
     *
     * @return The build results
     */
    public ObjectList<Result<Builder>> run()
    {
        // Start resolving artifacts in the background starting from the root
        // builder's dependencies. Each resolved artifact is added to the
        // resolved set.
        var resolved = new ArtifactResolutionTracker(this);
        trace("Starting artifact resolver");
        new ArtifactResolver(build, resolved).resolveArtifacts();

        // Start running builders in parallel. Each builder will wait until its
        // dependencies are in the resolved set before executing.
        trace("Starting build");
        return build(resolved);
    }

    /**
     * Executes all builders from the root in parallel groups.
     *
     * @param resolved The resolved artifacts set
     * @return The results for each builder
     */
    private ObjectList<Result<Builder>> build(ArtifactResolutionTracker resolved)
    {
        // Create a queue of builders from the dependency tree for the build, where builders are
        // ready for processing once their artifact and builder dependencies have been resolved.
        var builderQueue = build.dependencyTree().asQueue(Builder.class)
            .withIsReady((queue, it) -> resolved.isResolved(it.dependencies())
                && queue.hasCompleted(it.builderDependencies()));

        // Create a thread pool,
        var executor = threadPool("FiascoBuild", build.settings().builderThreads());

        // and while there are builders yet to run,
        var futures = new ObjectList<Future<Result<Builder>>>();
        for (Builder builder = builderQueue.takeNextReady(); builder != null; builder = builderQueue.takeNextReady())
        {
            // submit the builder to the executor,
            var future = (Future<Result<Builder>>) executor.submit(builderTask(resolved, builderQueue, builder));
            trace("$: submitted", builder);

            // and add the future to the list of results to wait for.
            futures.add(future);
        }

        // Wait for all futures to be resolved, and return the results.
        trace("Waiting for builders to finish");
        var results = new ObjectList<Result<Builder>>();
        for (var at : futures)
        {
            results.add(tryCatch(() -> at.get()));
        }
        trace("All builders finished");

        shutdownAndAwaitTermination(executor);

        return results;
    }

    /**
     * Returns a {@link Callable} task for execution by the executor's thread pool
     *
     * @param resolved The set of resolved artifacts to wait on, as needed
     * @param queue The dependency queue for marking when the builder completes
     * @param builder The builder to execute
     * @return The {@link Callable} to execute with {@link ExecutorService#submit(Callable)}
     */
    @NotNull
    private Callable<Result<Builder>> builderTask(ArtifactResolutionTracker resolved,
                                                  DependencyQueue queue,
                                                  Builder builder)
    {
        return () ->
        {
            // Wait for artifact dependencies to be resolved,
            trace("Waiting for artifacts to be resolved: $", builder);
            resolved.waitForResolutionOf(builder.dependencies());

            // run the builder,
            trace("Building: $", builder);
            var result = builder.run();
            trace("Build completed: $", builder);

            // and then mark it as processed.
            queue.completed(builder);

            return result;
        };
    }
}
