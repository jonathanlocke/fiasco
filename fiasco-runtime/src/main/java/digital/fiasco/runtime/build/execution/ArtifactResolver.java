package digital.fiasco.runtime.build.execution;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.interfaces.code.Callback;
import digital.fiasco.runtime.build.settings.BuildSettingsObject;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.queue.DependencyQueue;
import digital.fiasco.runtime.dependency.queue.DependencyTree;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.lists.ArtifactList;
import digital.fiasco.runtime.repository.remote.RemoteRepository;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;
import digital.fiasco.runtime.repository.remote.server.FiascoServer;

import java.util.concurrent.ExecutorCompletionService;

import static com.telenav.kivakit.core.function.Result.result;
import static com.telenav.kivakit.core.thread.KivaKitThread.run;
import static com.telenav.kivakit.core.thread.Threads.shutdownAndAwaitTermination;
import static com.telenav.kivakit.core.thread.Threads.threadPool;

/**
 * Resolves artifacts in groups by turning the given root dependency into a {@link DependencyTree}, and then turning
 * that tree into a {@link DependencyQueue}. Groups of dependencies that are ready for resolution are retrieved with
 * {@link DependencyQueue#takeAll(Class)}, and then resolved using the {@link Librarian} found in the
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
    /**
     * Resolves the artifact dependencies of a build tree in groups, calling back with the results for each group
     *
     * @param root The root of the build tree
     * @param completed The callback to call as each group of artifacts is resolved
     */
    public void resolveArtifacts(Builder root,
                                 Callback<Result<ArtifactList>> completed)
    {
        var settings = root.settings();

        run(this, "FiascoResolver", () ->
        {
            // Build a dependency queue from the root dependency,
            var queue = new DependencyTree(root).asQueue();

            // create an executor and completion service,
            var executor = threadPool("FiascoResolverPool", settings.builderThreads());
            var completion = new ExecutorCompletionService<Void>(executor);

            // and then go through groups of artifacts from the queue that are ready to be resolved,
            for (var group = queue.takeAll(Artifact.class); group != null; group = queue.takeAll(Artifact.class))
            {
                // and use the completion service to resolve the group.
                var artifacts = group.asArtifactList();
                completion.submit(() ->
                {
                    // Use the librarian to resolve the requested artifacts, and call back with the result.
                    var librarian = settings.librarian();
                    completed.call(result(librarian, () -> librarian.resolve(artifacts.asArtifactDescriptors())));
                    return null;
                });
            }

            // Wait until all artifacts in the queue are resolved.
            shutdownAndAwaitTermination(executor);
        });
    }
}
