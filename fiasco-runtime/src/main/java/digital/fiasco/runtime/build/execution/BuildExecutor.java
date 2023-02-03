package digital.fiasco.runtime.build.execution;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.messaging.Listener;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.settings.BuildSettings;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.collections.DependencyQueue;
import digital.fiasco.runtime.dependency.collections.DependencyTree;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;
import digital.fiasco.runtime.dependency.collections.lists.BaseDependencyList;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static com.telenav.kivakit.core.thread.Threads.threadPool;

/**
 * Runs a parallel build for the build tree with the given root builder. The root builder's settings provide the number
 * of threads to use with {@link BuildSettings#builderThreads()}. Artifacts are resolved in parallel by an
 * {@link ArtifactResolver}, which uses {@link BuildSettings#artifactResolverThreads()} threads. Both the artifact
 * resolver and this build executor are greedy and begin work as soon as it is possible.
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
 * {@link ResolvedArtifacts} object serves as a way to communicate between the two thread pools. As artifacts are
 * resolved by the {@link ArtifactResolver}, they are marked as resolved with
 * {@link ResolvedArtifacts#resolve(ArtifactList)}. Builders can wait for their artifacts to be resolved by calling
 * {@link ResolvedArtifacts#waitForResolutionOf(ArtifactList)}. In this way, artifact resolution proceeds independently
 * of building, with builders blocking only when it is necessary to wait for artifacts to be resolved.
 * </p>
 *
 * @author Jonathan Locke
 * @see ResolvedArtifacts
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
    /** The root of the builder tree */
    private final Builder root;

    public BuildExecutor(Listener listener, @NotNull Builder root)
    {
        this.root = root;
    }

    /**
     * Runs builders and resolves artifacts, returning a list of results, one for each builder.
     *
     * @return The build results
     */
    public ObjectList<Result<Builder>> build()
    {
        // Start resolving artifacts in the background starting from the root
        // builder's dependencies. Each resolved artifact is added to the
        // resolved set.
        var resolved = new ResolvedArtifacts(this);
        trace("Starting artifact resolver");
        new ArtifactResolver(this).resolveArtifacts(root, resolved);

        // Start running builders in parallel. Each builder will wait until its
        // dependencies are in the resolved set before executing.
        trace("Starting build");
        return build(resolved);
    }

    /**
     * Executes all builders from the root in parallel groups.
     */
    private ObjectList<Result<Builder>> build(ResolvedArtifacts resolved)
    {
        // Create a queue of builders from the given root,
        var queue = new DependencyTree(root).asQueue();

        // create a thread pool,
        var executor = threadPool("FiascoBuild", root.settings().builderThreads());

        // and while there are builders yet to run,
        var futures = new ObjectList<Future<Result<Builder>>>();
        for (var builder = queue.takeOne(Builder.class); builder != null; builder = queue.takeOne(Builder.class))
        {
            // submit the builder to the executor,
            var future = (Future<Result<Builder>>) executor.submit(builderTask(resolved, queue, builder));
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

        return results;
    }

    @NotNull
    private Callable<Result<Builder>> builderTask(ResolvedArtifacts resolved,
                                                  DependencyQueue queue,
                                                  Builder builder)
    {
        return () ->
        {
            // Wait for artifact dependencies to be resolved,
            trace("$: waiting for artifacts to be resolved", builder);
            resolved.waitForResolutionOf(builder.artifactDependencies());

            // run the builder,
            trace("$: building", builder);
            var result = builder.build();
            trace("$: build completed ", builder);

            // and then mark it as processed.
            queue.processed(builder);

            return result;
        };
    }
}
