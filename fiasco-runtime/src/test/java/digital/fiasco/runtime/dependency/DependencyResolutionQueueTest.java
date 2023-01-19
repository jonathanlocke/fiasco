package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.core.thread.Monitor;
import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.Library;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.Library.library;
import static digital.fiasco.runtime.dependency.artifact.LibraryList.libraries;

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

        ensureEqual(queue.nextReady(Library.class), c);
        queue.resolve(c);

        ensureEqual(queue.nextReady(Library.class), b);
        queue.resolve(b);

        ensureEqual(queue.nextReady(Library.class), e);
        queue.resolve(e);

        ensureEqual(queue.nextReady(Library.class), f);
        queue.resolve(f);

        ensureEqual(queue.nextReady(Library.class), d);
        queue.resolve(d);

        ensureEqual(queue.nextReady(Library.class), a);
        queue.resolve(a);
    }

    @Test
    public void testNextReadyAwait()
    {
        var queue = queue();
        KivaKitThread.run(this, "await", () ->
        {
            queue.resolve(c);
            queue.resolve(b);
            queue.resolve(e);
            queue.resolve(f);
            queue.resolve(d);
            queue.resolve(a);
        });
        ensureEqual(queue.nextReady(Library.class), c);
        ensureEqual(queue.nextReady(Library.class), b);
        ensureEqual(queue.nextReady(Library.class), e);
        ensureEqual(queue.nextReady(Library.class), f);
        ensureEqual(queue.nextReady(Library.class), d);
        ensureEqual(queue.nextReady(Library.class), a);
    }

    @Test
    public void testNextReadyGroup()
    {
        var queue = queue();

        var group1 = queue.nextReadyGroup(Library.class);
        ensure(group1.equals(libraries(c, e, f)));
        queue.resolveGroup(group1.asLibraryList());

        var group2 = queue.nextReadyGroup(Library.class);
        ensure(group2.equals(libraries(b, d)));
        queue.resolveGroup(group2);

        var group3 = queue.nextReadyGroup(Library.class);
        ensure(group3.equals(libraries(a)));
        queue.resolveGroup(group3);
    }

    @Test
    public void testNextReadyGroupAwait()
    {
        var queue = queue();
        var ready = new Monitor();
        KivaKitThread.run(this, "await", () ->
        {
            queue.resolveGroup(libraries(c, e, f));
            ready.await();
            queue.resolveGroup(libraries(b, d));
            ready.await();
            queue.resolveGroup(libraries(a));
        });
        ensureEqual(queue.nextReadyGroup(Library.class), libraries(c, e, f));
        ready.signal();
        ensureEqual(queue.nextReadyGroup(Library.class), libraries(b, d));
        ready.signal();
        ensureEqual(queue.nextReadyGroup(Library.class), libraries(a));
    }

    @NotNull
    private DependencyTree libraryTree()
    {
        //       a
        //      / \
        //     b   d
        //    /   / \
        //   c   e   f

        b = b.dependsOn(c);
        d = d.dependsOn(e, f);
        a = a.dependsOn(b, d);

        return new DependencyTree(a);
    }

    private DependencyResolutionQueue queue()
    {
        return libraryTree().asQueue();
    }
}
