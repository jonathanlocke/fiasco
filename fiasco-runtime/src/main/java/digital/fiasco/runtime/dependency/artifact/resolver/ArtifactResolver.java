package digital.fiasco.runtime.dependency.artifact.resolver;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.interfaces.code.Callback;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.settings.BuildSettingsObject;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.collections.DependencyQueue;
import digital.fiasco.runtime.dependency.collections.DependencyTree;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;
import digital.fiasco.runtime.librarian.MultiRepositoryLibrarian;
import digital.fiasco.runtime.repository.remote.RemoteRepository;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;
import digital.fiasco.runtime.repository.remote.server.FiascoServer;

import static com.telenav.kivakit.core.function.Result.result;
import static com.telenav.kivakit.core.thread.KivaKitThread.run;
import static com.telenav.kivakit.core.thread.Threads.shutdownAndAwaitTermination;
import static com.telenav.kivakit.core.thread.Threads.threadPool;

/**
 * Resolves artifacts in groups by turning the given root dependency into a {@link DependencyTree}, and then turning
 * that tree into a {@link DependencyQueue}. Groups of dependencies that are ready for resolution are retrieved with
 * {@link DependencyQueue#takeAllReadyDependencies()}, and then resolved using the {@link MultiRepositoryLibrarian} found in the
 * {@link BuildSettingsObject}. When a group of dependencies is resolved, the given {@link Callback} is called with the
 * resolution {@link Result}.
 *
 * <p><b>Performance</b></p>
 *
 * <p>
 * Artifact resolution occurs in groups because the Fiasco repository system ({@link RemoteRepository},
 * {@link FiascoClient}, {@link FiascoServer}) is capable of resolving multiple dependencies in a single request, which
 * reduces the number of requests that are required for artifact resolution, which speeds up the system.
 * </p>
 *
 * @author Jonathan Locke
 * @see BuildSettingsObject
 * @see DependencyQueue
 * @see Dependency
 * @see ArtifactList
 */
public class ArtifactResolver extends BaseComponent implements TryTrait
{
    /** The build that this resolver is resolving artifacts for */
    private final Build build;

    /** The set of resolved artifacts */
    private final ArtifactResolutionTracker resolved;

    /**
     * Creates an artifact resolver for the given build
     *
     * @param build The build
     * @param resolved The resolved artifact set to update as artifacts are resolved
     */
    public ArtifactResolver(Build build, ArtifactResolutionTracker resolved)
    {
        this.build = build;
        this.resolved = resolved;

        build.listenTo(this);
    }

    /**
     * Resolves the artifact dependencies of a build tree in groups, updating the given {@link ArtifactResolutionTracker} set
     * for each group that is resolved.
     */
    public void resolveArtifacts()
    {
        // Resolve artifacts on a background thread
        run(this, "Resolver", () ->
        {
            // Create a dependency queue from the build's dependency tree,
            var queue = build.dependencyTree().asQueue(Artifact.class);

            // create an executor and completion service,
            trace("Starting artifact resolver threads");
            var threads = build.settings().builderThreads();
            var executor = threadPool("ResolverPool", threads);

            // and submit a resolve artifacts task for each thread.
            threads.loop(() -> executor.submit(() -> resolveArtifacts(queue)));

            // Wait until all artifacts in the queue are resolved.
            trace("Waiting for artifact resolution to complete");
            shutdownAndAwaitTermination(executor);
        });
    }

    /**
     * Resolve artifacts from the given queue until it is empty
     *
     * @param queue The queue of artifacts to resolve
     */
    private Void resolveArtifacts(DependencyQueue queue)
    {
        // While there are dependencies left to resolve,
        while (queue.canTakeDependencies())
        {
            // resolve the next group of artifacts that are ready to be resolved.
            trace("Waiting for ready dependencies");
            var ready = queue.takeAllReadyDependencies();
            if (ready.isNonEmpty())
            {
                trace("Resolving artifacts: $", ready);
                resolveArtifacts(ready.asArtifactList());
                queue.processed(ready);
            }
        }

        // Required by ExecutorCompletionService.submit(Callable<Void>).
        return null;
    }

    /**
     * Resolves the given list of artifacts using the build's librarian
     *
     * @param artifacts The artifacts to resolve
     */
    private void resolveArtifacts(ArtifactList artifacts)
    {
        if (artifacts.isNonEmpty())
        {
            // Use the librarian to resolve the requested artifacts,
            var librarian = build.librarian();
            trace("Librarian resolving: $", artifacts);
            var result = result(librarian, () -> librarian.resolve(artifacts.asArtifactDescriptors()));

            // and for each group of artifacts that are successfully resolved,
            if (result.succeeded())
            {
                // add them to the resolved set.
                trace("Resolved: $", artifacts);
                resolved.resolved(result.get());
            }
            else
            {
                result.messages().broadcastTo(this);
            }
        }
    }
}
