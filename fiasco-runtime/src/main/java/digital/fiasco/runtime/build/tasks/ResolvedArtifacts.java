package digital.fiasco.runtime.build.tasks;

import com.telenav.kivakit.core.thread.Monitor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;

import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifactList;

/**
 * Holds a set of artifacts resolved from repositories by background threads. When a new set of artifacts is resolved,
 * they are marked as resolved by calling {@link #markResolved(ArtifactList)}. A task that can't proceed until its
 * artifact dependencies are all resolved can wait for that condition by calling {@link #waitFor(ArtifactList)}.
 *
 * @author Jonathan Locke
 */
public class ResolvedArtifacts
{
    /** The set of artifacts that have been resolved */
    private ArtifactList resolved = artifactList();

    /** A monitor that is signaled when artifacts are resolved */
    private final Monitor updated = new Monitor();

    /**
     * Marks the given artifacts as resolved
     *
     * @param artifacts The artifacts that were resolved
     */
    public void markResolved(ArtifactList artifacts)
    {
        synchronized (updated)
        {
            this.resolved = resolved.with(artifacts);
            updated.signal();
        }
    }

    /**
     * Waits until the given set of artifacts is resolved
     *
     * @param required The artifacts that are required
     */
    public void waitFor(ArtifactList required)
    {
        synchronized (updated)
        {
            while (!resolved.containsAll(required))
            {
                updated.await();
            }
        }
    }
}
