package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.value.count.Count;
import digital.fiasco.runtime.build.builder.Builder;

import java.util.stream.Collectors;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.thread.Threads.shutdownAndAwaitTermination;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DependencyProcessor extends BaseComponent implements TryTrait
{
    /** The dependency tree to process */
    private final DependencyTree dependencyTree;

    public DependencyProcessor(DependencyTree dependencyTree)
    {
        this.dependencyTree = dependencyTree;
    }

    /**
     * Processes the dependencies in this list, possibly in parallel, calling the callback with each dependency to
     * process only after its dependencies have been processed.
     *
     * @param threads The number of threads to use
     * @param task The callback to process each dependency
     * @return Returns a list of build results
     */
    public ObjectList<DependencyProcessingResult> process(Count threads,
                                                          DependencyTask task)
    {
        var dependencies = dependencyTree.depthFirst();

        var batches = new DependencyBatcher().batches(dependencyTree);

        for (var batch : batches)
        {
            var builders = batch.matching(at -> at instanceof Builder);
            var artifacts = batch.without(builders);

            // If there is only one thread requested,
            if (threads.equals(Count._1))
            {
                // call the callback for each dependency in order
                return list(builders.stream()
                    .map(task)
                    .collect(Collectors.toList()));
            }
            else
            {
                // otherwise, create an executor with the requested number of threads
                var executor = newFixedThreadPool(threads.asInt());
                tryCatch(() -> executor.invokeAll(builders, 60, SECONDS), "Unable to ");

                // then wait for the executor threads to finish processing.
                shutdownAndAwaitTermination(executor);
            }
        }

        return results;
    }
}
