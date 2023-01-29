package digital.fiasco.runtime.build.execution;

import com.telenav.kivakit.core.thread.Monitor;
import digital.fiasco.runtime.dependency.artifact.lists.ArtifactList;

/**
 * Holds a set of artifacts resolved from repositories by background threads. When a new set of artifacts is resolved,
 * they are marked as resolved by calling {@link #resolve(ArtifactList)}. A task that can't proceed until its artifact
 * dependencies are all resolved can wait for that condition by calling {@link #waitForResolutionOf(ArtifactList)}.
 *
 * @author Jonathan Locke
 */
public class ResolvedArtifacts
{
    /** The set of artifacts that have been resolved */
    private ArtifactList resolved = ArtifactList.artifacts();

    /** A monitor that is signaled when artifacts are resolved */
    private final Monitor updated = new Monitor();

    /**
     * Returns true if all the artifacts in the given list have been resolved
     *
     * @param artifacts The artifacts
     * @return True if the artifacts ahve been resolved
     */
    public boolean isResolved(ArtifactList artifacts)
    {
        return resolved.containsAll(artifacts);
    }

    /**
     * Marks the given artifacts as resolved
     *
     * @param artifacts The artifacts that were resolved
     */
    public void resolve(ArtifactList artifacts)
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
    public void waitForResolutionOf(ArtifactList required)
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
