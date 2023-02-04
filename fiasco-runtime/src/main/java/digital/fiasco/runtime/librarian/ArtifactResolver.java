package digital.fiasco.runtime.librarian;

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
import digital.fiasco.runtime.repository.remote.RemoteRepository;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;
import digital.fiasco.runtime.repository.remote.server.FiascoServer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;

import static com.telenav.kivakit.core.function.Result.result;
import static com.telenav.kivakit.core.thread.KivaKitThread.run;
import static com.telenav.kivakit.core.thread.Threads.shutdownAndAwaitTermination;
import static com.telenav.kivakit.core.thread.Threads.threadPool;

/**
 * Resolves artifacts in groups by turning the given root dependency into a {@link DependencyTree}, and then turning
 * that tree into a {@link DependencyQueue}. Groups of dependencies that are ready for resolution are retrieved with
 * {@link DependencyQueue#takeReadyDependencies()}, and then resolved using the {@link RepositoryLibrarian} found in
 * the {@link BuildSettingsObject}. When a group of dependencies is resolved, the given {@link Callback} is called with
 * the resolution {@link Result}.
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
    private final ResolvedArtifactSet resolved;

    /**
     * Creates an artifact resolver for the given build
     *
     * @param build The build
     * @param resolved The resolved artifact set to update as artifacts are resolved
     */
    public ArtifactResolver(Build build, ResolvedArtifactSet resolved)
    {
        this.build = build;
        this.resolved = resolved;

        build.listenTo(this);
    }

    /**
     * Resolves the artifact dependencies of a build tree in groups, updating the given {@link ResolvedArtifactSet} set
     * for each group that is resolved.
     */
    public void resolveArtifacts()
    {
        var settings = build.settings();

        run(this, "Resolver", () ->
        {
            // Create a dependency queue from the build's dependency tree,
            var queue = build.dependencyTree().asQueue(Artifact.class);

            // create an executor and completion service,
            trace("Starting artifact resolver threads");
            var executor = threadPool("ResolverPool", settings.builderThreads());
            var completion = new ExecutorCompletionService<Void>(executor);

            // and while there are dependencies to resolve,
            while (queue.isWorkAvailable())
            {
                // take the next group of artifacts that are ready to be resolved,
                var artifacts = queue.takeReadyDependencies().asArtifactList();

                // and use the completion service to resolve the group of artifacts.
                trace("Submitting resolver task: $", artifacts);
                completion.submit(resolverTask(artifacts));
            }

            // Wait until all artifacts in the queue are resolved.
            trace("Waiting for artifact resolution to complete");
            shutdownAndAwaitTermination(executor);
        });
    }

    /**
     * Resolves the given list of artifacts using the build's librarian
     *
     * @param artifacts The artifacts to resolve
     */
    private Callable<Void> resolverTask(ArtifactList artifacts)
    {
        return () ->
        {
            // Use the librarian to resolve the requested artifacts,
            var librarian = build.librarian();
            trace("Resolver librarian resolving: $", artifacts);
            var result = result(librarian, () -> librarian.resolve(artifacts.asArtifactDescriptors()));

            // and for each group of artifacts that are successfully resolved,
            if (result.succeeded())
            {
                // add them to the resolved set.
                trace("Resolved: $", artifacts);
                resolved.resolve(result.get());
            }
            else
            {
                result.messages().broadcastTo(this);
            }
            return null;
        };
    }
}
