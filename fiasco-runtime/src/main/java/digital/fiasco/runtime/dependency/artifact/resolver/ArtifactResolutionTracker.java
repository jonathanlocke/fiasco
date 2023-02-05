package digital.fiasco.runtime.dependency.artifact.resolver;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.thread.locks.Lock;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;

import java.util.concurrent.locks.Condition;

import static com.telenav.kivakit.core.time.Duration.seconds;

/**
 * Tracks the resolution of artifacts.
 *
 * <p>
 * Tracks the artifacts being resolved by an {@link ArtifactResolver}. When a new group of one or more artifacts is
 * resolved, they are marked resolved by calling {@link #resolved(ArtifactList)}. A task that can't proceed until its
 * artifact dependencies have all been resolved can wait for that condition by calling
 * {@link #waitForResolutionOf(ArtifactList)}.
 * </p>
 *
 * @author Jonathan Locke
 */
public class ArtifactResolutionTracker extends BaseComponent
{
    /** The set of artifacts that have been resolved */
    private ArtifactList resolved = ArtifactList.artifacts();

    /** Read/write lock for accessing the resolved list */
    private final Lock lock = new Lock();

    /** Condition to signal/await artifact resolution */
    private final Condition resolvedMore = lock.newCondition();

    public ArtifactResolutionTracker(Listener listener)
    {
        listener.listenTo(this);
    }

    /**
     * Returns true if all the artifacts in the given list have been resolved
     *
     * @param artifacts The artifacts
     * @return True if the artifacts have been resolved
     */
    public boolean isResolved(ArtifactList artifacts)
    {
        return lock.whileLocked(() -> resolved.containsAll(artifacts));
    }

    /**
     * Marks the given artifacts as resolved
     *
     * @param artifacts The artifacts that were resolved
     */
    public void resolved(ArtifactList artifacts)
    {
        lock.whileLocked(() ->
        {
            if (artifacts.isNonEmpty())
            {
                this.resolved = resolved.with(artifacts);
                resolvedMore.signal();
                trace("Signaled resolution: $", artifacts);
            }
        });
    }

    /**
     * Returns the number of resolved artifacts
     */
    public int size()
    {
        return lock.whileLocked(() -> resolved.size());
    }

    /**
     * Waits until the given set of artifacts is resolved
     *
     * @param required The artifacts that are required
     */
    public void waitForResolutionOf(ArtifactList required)
    {
        lock.whileLocked(() ->
        {
            while (!isResolved(required))
            {
                trace("Awaiting resolution: $", required.without(resolved));
                seconds(0.5).await(resolvedMore);
            }
        });
    }
}
