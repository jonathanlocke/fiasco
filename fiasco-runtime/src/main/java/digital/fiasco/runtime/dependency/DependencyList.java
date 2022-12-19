package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ConcurrentHashSet;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.thread.Monitor;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.code.Callback;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.thread.Threads.shutdownAndAwaitTermination;

/**
 * An immutable, ordered list of {@link Dependency} objects. The objects in the list can be processed with
 * {@link #process(Listener, Count, Callback)}, which calls the given callback with the number of threads requested and
 * reports issues to the given listener.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #dependencyList(Dependency[])} - Variable arguments factory method</li>
 *     <li>{@link #dependencyList(List)} - List factory method</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #with(Dependency[])} - Returns a copy of this list with the given variable-argument dependencies</li>
 *     <li>{@link #without(Matcher)} - Returns a copy of this list without the given dependencies</li>
 *     <li>{@link #without(Collection) - Returns a copy of this list without the given dependencies}</li>
 * </ul>
 *
 * <p><b>Processing</b></p>
 *
 * <ul>
 *     <li>{@link #process(Listener, Count, Callback)} - Processes the dependencies in this list using the given number of threads</li>
 *     <li>{@link #process(Listener, Callback)} - Processes this dependency list using a single thread</li>
 *     <li>{@link #queue()} - Returns a blocking queue containing the dependencies in this list</li>
 * </ul>
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class DependencyList<D extends Dependency<D>> implements Iterable<D>
{
    /**
     * Creates a list of dependencies
     *
     * @param dependencies The dependencies to add
     * @param <T> The type of dependency
     * @return The dependency list
     */
    @SafeVarargs
    public static <T extends Dependency<T>> DependencyList<T> dependencyList(T... dependencies)
    {
        return new DependencyList<>(list(dependencies));
    }

    /**
     * Creates a list of dependencies
     *
     * @param dependencies The dependencies to add
     * @param <T> The type of dependency
     * @return The dependency list
     */
    public static <T extends Dependency<T>> DependencyList<T> dependencyList(List<T> dependencies)
    {
        return new DependencyList<>(dependencies);
    }

    /** The underlying dependencies */
    private final ObjectList<D> dependencies = list();

    protected DependencyList()
    {
    }

    protected DependencyList(List<D> dependencies)
    {
        this.dependencies.addAll(dependencies);
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    public ObjectList<D> asList()
    {
        return dependencies.copy();
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    public ObjectSet<D> asSet()
    {
        return set(dependencies.copy());
    }

    /**
     * Returns an immutable copy of this dependency list.
     *
     * @return The copy
     */
    public DependencyList<D> copy()
    {
        return new DependencyList<>(dependencies.copy());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<D> iterator()
    {
        return dependencies.iterator();
    }

    /**
     * Processes the dependencies in this list using a single thread
     *
     * @param listener The listener to call with any messages from processing
     * @param callback The callback to process each dependency
     */
    public void process(Listener listener, Callback<D> callback)
    {
        process(listener, Count._1, callback);
    }

    /**
     * Processes the dependencies in this list, possibly in parallel, calling the callback with each dependency to
     * process only after its dependencies have been processed.
     *
     * @param listener The listener to call with any messages from processing
     * @param threads The number of threads to use
     * @param callback The callback to process each dependency
     */
    @SuppressWarnings("unchecked")
    public void process(Listener listener, Count threads, Callback<D> callback)
    {
        // If there is only one thread requested,
        if (threads.equals(Count._1))
        {
            // call the callback for each dependency in order
            forEach(callback::call);
        }
        else
        {
            // otherwise, create an executor with the requested number of threads
            var executor = Executors.newFixedThreadPool(threads.asInt());

            // and a queue with the dependencies in it
            var queue = queue();

            // and submit jobs for each thread
            var completed = new ConcurrentHashSet<Dependency<D>>();
            var monitor = new Monitor();
            threads.loop(() -> executor.submit(() ->
            {
                // While the queue has dependencies to process,
                while (!queue.isEmpty())
                {
                    Dependency<D> dependency = null;
                    try
                    {
                        // take the next dependency from the queue
                        dependency = queue.take();

                        // and wait until all of its dependencies have been processed
                        while (!completed.containsAll(dependency.dependencies().dependencies))
                        {
                            monitor.await();
                        }

                        // before processing it,
                        callback.call((D) dependency);
                        completed.add(dependency);

                        // and waking any threads waiting on this dependency.
                        monitor.done();
                    }
                    catch (InterruptedException ignored)
                    {
                    }
                    catch (Exception e)
                    {
                        listener.problem(e, "Error processing '$'", dependency);
                    }
                }
            }));

            // then wait for the executor threads to finish processing.
            shutdownAndAwaitTermination(executor);
        }
    }

    /**
     * Returns a blocking queue of the dependencies in this list, in depth-first order
     *
     * @return A blocking queue of dependencies
     */
    public LinkedBlockingDeque<Dependency<D>> queue()
    {
        return new LinkedBlockingDeque<>(dependencies);
    }

    /**
     * Returns this list with the given dependencies appended
     *
     * @param dependencies A variable-argument list of dependencies to include
     * @return A copy of this list with the given dependencies added
     */
    @SafeVarargs
    public final DependencyList<D> with(D... dependencies)
    {
        var copy = copy();
        copy.dependencies.addAll(dependencies);
        return copy;
    }

    /**
     * Returns this list with the given dependencies appended
     *
     * @return A copy of this list with the given dependencies added
     */
    public DependencyList<D> with(DependencyList<D> dependencies)
    {
        var copy = copy();
        copy.dependencies.addAll(dependencies.dependencies);
        return copy;
    }

    /**
     * Returns a copy of this list without the given dependencies
     *
     * @param exclusions The dependencies to exclude
     * @return The dependency list
     */
    public DependencyList<D> without(Collection<D> exclusions)
    {
        var copy = copy();
        copy.dependencies.removeAll(exclusions);
        return copy;
    }

    /**
     * Returns a copy of this dependency list without all dependencies matching the given pattern
     *
     * @param pattern The pattern to match
     * @return A copy of this list without the specified dependencies
     */
    public DependencyList<D> without(Matcher<D> pattern)
    {
        var copy = copy();
        copy.dependencies.removeIf(pattern::matches);
        return copy;
    }
}
