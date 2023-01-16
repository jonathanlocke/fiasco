package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.core.thread.Monitor;
import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.Library;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Duration.milliseconds;
import static digital.fiasco.runtime.dependency.DependencyList.dependencies;
import static digital.fiasco.runtime.dependency.DependencyTree.dependencyTree;
import static digital.fiasco.runtime.dependency.artifact.Library.library;

public class DependencyResolutionQueueTest extends FiascoTest
{
    Library a = library("a:a:1");

    Library b = library("b:b:1");

    Library c = library("c:c:1");

    Library d = library("d:d:1");

    Library e = library("e:e:1");

    Library f = library("f:f:1");

    @Test
    public void testNextReady()
    {
        var queue = queue();

        ensureEqual(queue.nextReady(), c);
        queue.resolve(c);

        ensureEqual(queue.nextReady(), b);
        queue.resolve(b);

        ensureEqual(queue.nextReady(), e);
        queue.resolve(e);

        ensureEqual(queue.nextReady(), f);
        queue.resolve(f);

        ensureEqual(queue.nextReady(), d);
        queue.resolve(d);

        ensureEqual(queue.nextReady(), a);
        queue.resolve(a);
    }

    @Test
    public void testNextReadyAwait()
    {
        var queue = queue();
        var ready = new Monitor();
        KivaKitThread.run(this, "await", () ->
        {
            queue.resolve(c);
            milliseconds(100).sleep();
            ready.signal();
            milliseconds(100).sleep();
            queue.resolve(b);
            milliseconds(100).sleep();
            ready.signal();
            milliseconds(100).sleep();
            queue.resolve(e);
            milliseconds(100).sleep();
            ready.signal();
            milliseconds(100).sleep();
            queue.resolve(f);
            milliseconds(100).sleep();
            ready.signal();
            milliseconds(100).sleep();
            queue.resolve(d);
            milliseconds(100).sleep();
            ready.signal();
            queue.resolve(a);
        });
        ensureEqual(queue.nextReady(), c);
        ensureEqual(queue.nextReady(), b);
        ensureEqual(queue.nextReady(), e);
        ensureEqual(queue.nextReady(), f);
        ensureEqual(queue.nextReady(), d);
        ensureEqual(queue.nextReady(), a);
    }

    @Test
    public void testNextReadyGroup()
    {
        var queue = queue();

        var group1 = queue.nextReadyGroup();
        ensure(group1.equals(dependencies(c, e, f)));
        queue.resolveGroup(group1);

        var group2 = queue.nextReadyGroup();
        ensure(group2.equals(dependencies(b, d)));
        queue.resolveGroup(group2);

        var group3 = queue.nextReadyGroup();
        ensure(group3.equals(dependencies(a)));
        queue.resolveGroup(group3);
    }

    @Test
    public void testNextReadyGroupAwait()
    {
        var queue = queue();
        var ready = new Monitor();
        KivaKitThread.run(this, "await", () ->
        {
            queue.resolveGroup(dependencies(c, e, f));
            ready.await();
            queue.resolveGroup(dependencies(b, d));
            ready.await();
            queue.resolveGroup(dependencies(a));
        });
        ensureEqual(queue.nextReadyGroup(), dependencies(c, e, f));
        ready.signal();
        ensureEqual(queue.nextReadyGroup(), dependencies(b, d));
        ready.signal();
        ensureEqual(queue.nextReadyGroup(), dependencies(a));
    }

    @NotNull
    private DependencyTree<Library> libraryTree()
    {
        //       a
        //      / \
        //     b   d
        //    /   / \
        //   c   e   f

        b = b.dependsOn(c);
        d = d.dependsOn(e, f);
        a = a.dependsOn(b, d);

        return dependencyTree(a, Library.class);
    }

    private DependencyResolutionQueue<Library> queue()
    {
        return libraryTree().asQueue();
    }
}
