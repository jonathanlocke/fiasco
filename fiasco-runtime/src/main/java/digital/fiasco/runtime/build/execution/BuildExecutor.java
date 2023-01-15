package digital.fiasco.runtime.build.execution;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.language.trait.TryTrait;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.DependencyResolutionQueue;
import digital.fiasco.runtime.dependency.DependencyTree;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;

import java.util.concurrent.Future;

import static com.telenav.kivakit.core.thread.Threads.threadPool;
import static digital.fiasco.runtime.dependency.DependencyTree.dependencyTree;

/**
 * Runs a build.
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
 * {@link ResolvedArtifactSet} object serves as a way to communicate between the two thread pools. As artifacts are
 * resolved by the {@link ArtifactResolver}, they are marked as resolved with
 * {@link ResolvedArtifactSet#resolve(ArtifactList)}. Builders can wait for their artifacts to be resolved by calling
 * {@link ResolvedArtifactSet#waitForResolutionOf(ArtifactList)}. In this way, artifact resolution proceeds
 * independently of building, with builders blocking only when it is necessary to wait for artifacts to be resolved.
 * </p>
 *
 * @author Jonathan Locke
 * @see ResolvedArtifactSet
 * @see Result
 * @see Builder
 * @see Dependency
 * @see DependencyList
 * @see DependencyTree
 * @see DependencyResolutionQueue
 * @see Artifact
 * @see ArtifactList
 * @see ArtifactResolver
 */
public class BuildExecutor extends BaseComponent implements TryTrait
{
    /** The root of the builder tree */
    private final Builder root;

    public BuildExecutor(Builder root)
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
        var resolved = new ResolvedArtifactSet();

        // Start resolving artifacts in the background,
        listenTo(new ArtifactResolver()).resolveArtifacts(root, result ->
        {
            // and for each group of artifacts that are successfully resolved,
            if (result.succeeded())
            {
                // mark them in the resolved set.
                resolved.resolve(result.get());
            }
            result.messages().broadcastTo(this);
        });

        // Start running builders in parallel (builders will block until their dependencies are resolved)
        return build(resolved);
    }

    /**
     * Executes all builders from the root in parallel groups.
     */
    @SuppressWarnings("unchecked")
    private ObjectList<Result<Builder>> build(ResolvedArtifactSet resolved)
    {
        // Create a queue of builders from the given root,
        var queue = dependencyTree(root, Builder.class).asQueue();

        // create a thread pool,
        var executor = threadPool("FiascoBuildPool", root.settings().builderThreads());

        // and while there are builders yet to run,
        var futures = new ObjectList<Future<Result<Builder>>>();
        for (var builder = queue.nextReady(); builder != null; builder = queue.nextReady())
        {
            // submit the builder to the executor,
            var finalBuilder = builder;
            var future = (Future<Result<Builder>>) executor.submit(() ->
            {
                // Wait for artifact dependencies to be resolved,
                resolved.waitForResolutionOf(finalBuilder.artifactDependencies());

                // run the builder,
                finalBuilder.build();

                // and then mark it as resolved.
                queue.resolve(finalBuilder);
            });

            // and add the future to the list of results to wait for.
            futures.add(future);
        }

        // Wait for all futures to be resolved, and return the results.
        var results = new ObjectList<Result<Builder>>();
        for (var at : futures)
        {
            results.add(tryCatch(() -> at.get()));
        }

        return results;
    }
}
