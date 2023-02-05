package digital.fiasco.runtime.dependency.collections;

import com.telenav.kivakit.core.thread.KivaKitThread;
import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.types.Library;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.telenav.kivakit.core.thread.Threads.threadPool;
import static com.telenav.kivakit.core.time.Duration.milliseconds;
import static com.telenav.kivakit.core.time.Duration.minutes;
import static com.telenav.kivakit.core.value.count.Count._5;
import static com.telenav.kivakit.core.value.count.Count._6;
import static com.telenav.kivakit.core.value.count.Minimum._10;
import static com.telenav.kivakit.interfaces.time.WakeState.COMPLETED;
import static digital.fiasco.runtime.dependency.artifact.types.Library.library;
import static digital.fiasco.runtime.dependency.collections.lists.LibraryList.libraries;

public class DependencyQueueTest extends FiascoTest
{
    Library a = library("a:a:1");

    Library b = library("b:b:1");

    Library c = library("c:c:1");

    Library d = library("d:d:1");

    Library e = library("e:e:1");

    Library f = library("f:f:1");

    @Test
    public void testParallelProcessing()
    {
        _10.loop(() ->
        {
            var queue = testDependencyQueue();

            var executor = threadPool("Processor", _5);

            _5.loop(() ->
                executor.submit(() ->
                {
                    while (queue.canTakeWork())
                    {
                        var next = queue.takeNextReadyForProcessing();
                        milliseconds(3).sleep();
                        queue.processed(next);
                    }
                }));

            var wake = queue.awaitProcessingFinished(minutes(1));

            ensureEqual(wake, COMPLETED);
            ensure(!queue.canTakeWork());

            var processed = queue.processed().asLibraryList();
            ensure(_6.equals(processed.count()));

            ensure(processed.contains(a));
            ensure(processed.contains(b));
            ensure(processed.contains(c));
            ensure(processed.contains(d));
            ensure(processed.contains(e));
            ensure(processed.contains(f));
        });
    }

    @Test
    public void testTakeAll()
    {
        var queue = testDependencyQueue();

        var group1 = queue.takeAllReadyForProcessing();
        ensure(group1.equals(libraries(c, e, f)));
        queue.processed(group1);

        var group2 = queue.takeAllReadyForProcessing();
        ensure(group2.equals(libraries(b, d)));
        queue.processed(group2);

        var group3 = queue.takeAllReadyForProcessing();
        ensure(group3.equals(libraries(a)));
        queue.processed(group3);
    }

    @Test
    public void testTakeAllAndAwait()
    {
        _10.loop(() ->
        {
            var queue = testDependencyQueue();

            KivaKitThread.run(this, "processor", () ->
            {
                while (queue.canTakeWork())
                {
                    var next = queue.takeAllReadyForProcessing();
                    milliseconds(1).sleep();
                    queue.processed(next);
                }
            });

            var wake = queue.awaitProcessingFinished(minutes(1));

            ensureEqual(wake, COMPLETED);
            ensure(!queue.canTakeWork());

            var processed = queue.processed();
            ensure(_6.equals(processed.count()));

            // group 1
            ensureEqual(processed.get(0), c);
            ensureEqual(processed.get(1), e);
            ensureEqual(processed.get(2), f);

            // group 2
            ensureEqual(processed.get(3), b);
            ensureEqual(processed.get(4), d);

            // group 3
            ensureEqual(processed.get(5), a);
        });
    }

    @Test
    public void testTakeOneAndAwait()
    {
        _10.loop(() ->
        {
            var queue = testDependencyQueue();

            KivaKitThread.run(this, "processor", () ->
            {
                while (queue.canTakeWork())
                {
                    var next = queue.takeNextReadyForProcessing();
                    milliseconds(1).sleep();
                    queue.processed(next);
                }
            });

            var wake = queue.awaitProcessingFinished(minutes(1));

            ensureEqual(wake, COMPLETED);
            ensure(!queue.canTakeWork());

            var processed = queue.processed();
            ensure(_6.equals(processed.count()));

            ensureEqual(processed.get(0), c);
            ensureEqual(processed.get(1), b);
            ensureEqual(processed.get(2), e);
            ensureEqual(processed.get(3), f);
            ensureEqual(processed.get(4), d);
            ensureEqual(processed.get(5), a);
        });
    }

    @Test
    public void testToString()
    {
        ensure(!testDependencyQueue().toString().isEmpty());
    }

    @NotNull
    private DependencyTree testDependencies()
    {
        //       a
        //      / \
        //     b   d
        //    /   / \
        //   c   e   f

        b = b.withDependencies(c);
        d = d.withDependencies(e, f);
        a = a.withDependencies(b, d);

        return new DependencyTree(a);
    }

    private DependencyQueue testDependencyQueue()
    {
        return testDependencies().asQueue(Library.class);
    }

    {
        var queue = testDependencyQueue();

        ensureEqual(queue.takeNextReadyForProcessing(), c);
        queue.processed(c);

        ensureEqual(queue.takeNextReadyForProcessing(), b);
        queue.processed(b);

        ensureEqual(queue.takeNextReadyForProcessing(), e);
        queue.processed(e);

        ensureEqual(queue.takeNextReadyForProcessing(), f);
        queue.processed(f);

        ensureEqual(queue.takeNextReadyForProcessing(), d);
        queue.processed(d);

        ensureEqual(queue.takeNextReadyForProcessing(), a);
        queue.processed(a);
    }
}
