package digital.fiasco.runtime.dependency.processing;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.value.count.Count;
import digital.fiasco.runtime.dependency.DependencyGrouper;
import digital.fiasco.runtime.dependency.DependencyTree;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.thread.Threads.shutdownAndAwaitTermination;
import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * Executes dependency processing {@link Task}s for a {@link DependencyTree} on one or more threads. A
 * {@link DependencyGrouper} is used to break dependencies into groups that can be processed in parallel. A task factory
 * is then used to create tasks to process each group.
 *
 * @author Jonathan Locke
 */
public class TaskExecutor extends BaseComponent implements TryTrait
{
    /**
     * Processes the given tasks with the given number of threads
     *
     * @param threads The number of threads to use
     * @param tasks The tasks to execute
     * @return A list of task results
     */
    @NotNull
    public <T> ObjectList<TaskResult<T>> process(Count threads, TaskList<T> tasks)
    {
        // Create an executor with the requested number of threads
        var executor = newFixedThreadPool(threads.asInt());

        // process the builder tasks, obtaining a list of results,
        var results = new ObjectList<TaskResult<T>>();
        tryCatch(() ->
        {
            for (var futureTask : executor.invokeAll(tasks))
            {
                var result = tryCatchThrow(() -> futureTask.get(), "Task failed: $", futureTask);
                results.add(result);
            }
        }, "Task processing failed");

        // and finally, wait for the executor threads to finish processing.
        shutdownAndAwaitTermination(executor);

        return results;
    }
}
