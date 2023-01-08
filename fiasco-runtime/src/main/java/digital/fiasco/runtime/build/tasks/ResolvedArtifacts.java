package digital.fiasco.runtime.build.tasks;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.thread.Monitor;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;

/**
 * Holds a set of artifacts resolved from repositories by background threads. When a new set of artifacts is resolved,
 * they are marked as resolved by calling {@link #markResolved(DependencyList)}. A task that can't proceed until its
 * artifact dependencies are all resolved can wait for that condition by calling {@link #waitFor(ObjectSet)}.
 *
 * @author Jonathan Locke
 */
public class ResolvedArtifacts
{
    /** The set of artifacts that have been resolved */
    private final ObjectSet<Artifact> resolved = new ObjectSet<>();

    /** A monitor that is signaled when artifacts are resolved */
    private final Monitor added = new Monitor();

    /**
     * Marks the given artifacts as resolved
     *
     * @param artifacts The artifacts that were resolved
     */
    public void markResolved(DependencyList<Artifact> artifacts)
    {
        synchronized (added)
        {
            this.resolved.addAll(artifacts);
            added.signal();
        }
    }

    /**
     * Waits until the given set of artifacts is resolved
     *
     * @param required The artifacts that are required
     */
    public synchronized void waitFor(ObjectList<Artifact> required)
    {
        synchronized (added)
        {
            while (!resolved.containsAll(required.asSet()))
            {
                added.await();
            }
        }
    }
}
