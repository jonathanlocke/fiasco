package digital.fiasco.runtime.dependency.collections;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.types.Library;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.types.Library.library;
import static digital.fiasco.runtime.dependency.collections.DependencyList.dependencies;
import static digital.fiasco.runtime.dependency.collections.LibraryList.libraries;

public class DependencyTreeTest extends FiascoTest
{
    Library a = library("a:a:1");

    Library b = library("b:b:1");

    Library c = library("c:c:1");

    Library d = library("d:d:1");

    Library e = library("e:e:1");

    Library f = library("f:f:1");

    @Test
    public void testAsQueue()
    {
        var queue = queue();

        var group1 = queue.takeAllReady();
        ensure(group1.equals(libraries(c, e, f)));
        queue.completed(group1);

        var group2 = queue.takeAllReady();
        ensure(group2.equals(libraries(b, d)));
        queue.completed(group2);

        var group3 = queue.takeAllReady();
        ensure(group3.equals(dependencies(a)));
        queue.completed(group3);
    }

    @Test
    public void testDepthFirst()
    {
        var tree = libraryTree();
        var depthFirst = tree.asDepthFirstList();
        ensureEqual(depthFirst, dependencies(c, b, e, f, d, a));
    }

    @NotNull
    private DependencyTree libraryTree()
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

    private DependencyQueue queue()
    {
        return libraryTree().asQueue(Library.class)
            .withIsReady((queue, it) -> queue.hasCompleted(it.dependencies().asDependencyList()));
    }
}
