package digital.fiasco.runtime.dependency.processing;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.value.count.Count;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.DependencyTree;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.thread.Threads.shutdownAndAwaitTermination;
import static com.telenav.kivakit.core.value.count.Count._1;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.stream.Collectors.toList;

/**
 * Executes dependency processing {@link Task}s for a {@link DependencyTree} on one or more threads. A
 * {@link DependencyGrouper} is used to break dependencies into groups that can be processed in parallel. A task factory
 * is then used to create tasks to process each group.
 *
 * @author Jonathan Locke
 */
public class DependencyProcessor extends BaseComponent implements TryTrait
{
    /** The dependency tree to process */
    private final DependencyTree dependencyTree;

    /**
     * Creates a dependency processor
     *
     * @param dependencyTree The tree to process
     */
    public DependencyProcessor(DependencyTree dependencyTree)
    {
        this.dependencyTree = dependencyTree;
    }

    /**
     * Processes the dependencies in this list, possibly in parallel. The task to process each dependency is created by
     * the given task factory.
     *
     * @param threads The number of threads to use
     * @param taskFactory The factory to create a task for a dependency
     * @return Returns a list of build results
     */
    public ObjectList<TaskResult> process(Count threads,
                                          Function<Dependency, Task> taskFactory)
    {
        var results = new ObjectList<TaskResult>();

        // Break down the dependency tree into groups that can be executed in parallel,
        var groups = new DependencyGrouper().group(dependencyTree);

        // then for each group,
        for (var group : groups)
        {
            // split it into builders and artifacts,
            var builders = group.matching(at -> at instanceof Builder);
            var artifacts = group.without(builders);

            // download all group artifacts in parallel,
            parallelProcess(threads, artifacts.map(taskFactory));

            // then map the builder dependencies to tasks.
            var builderTasks = list(builders.stream()
                .map(taskFactory)
                .collect(toList()));

            // If there is only one thread requested,
            if (threads.equals(_1))
            {
                // run the task for each dependency,
                results.addAll(builderTasks.map(task -> tryCatch(task::call, "Build task failed: $", task)));
            }
            else
            {
                // otherwise, run the tasks in parallel.
                results.addAll(parallelProcess(threads, builderTasks));
            }
        }

        return results;
    }

    @NotNull
    private ObjectList<TaskResult> parallelProcess(Count threads,
                                                   ObjectList<Task> tasks)
    {
        // Create an executor with the requested number of threads
        var executor = newFixedThreadPool(threads.asInt());

        // process the builder tasks, obtaining a list of results,
        var results = new ObjectList<TaskResult>();
        tryCatch(() ->
        {
            for (var futureTask : executor.invokeAll(tasks))
            {
                var result = tryCatchThrow(() -> futureTask.get(), "Task failed: $", futureTask);
                results.add(result);
            }
        }, "Parallel task processing failed");

        // and finally, wait for the executor threads to finish processing.
        shutdownAndAwaitTermination(executor);

        return results;
    }
}
