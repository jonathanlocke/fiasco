package digital.fiasco.runtime.dependency.collections;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.lists.ArtifactList.artifacts;
import static digital.fiasco.runtime.dependency.artifact.artifacts.Library.library;
import static digital.fiasco.runtime.dependency.artifact.lists.LibraryList.libraries;

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

        var group1 = queue.takeAll(Library.class);
        ensure(group1.equals(libraries(c, e, f)));
        queue.processed(group1);

        var group2 = queue.takeAll(Library.class);
        ensure(group2.equals(libraries(b, d)));
        queue.processed(group2);

        var group3 = queue.takeAll(Library.class);
        ensure(group3.equals(DependencyList.dependencies(a)));
        queue.processed(group3);
    }

    @Test
    public void testDepthFirst()
    {
        var tree = libraryTree();
        var depthFirst = tree.asDepthFirstList().asArtifactDescriptors();
        ensureEqual(depthFirst, artifacts(c, b, e, f, d, a).asArtifactDescriptors());
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

    private DependencyQueue queue()
    {
        return libraryTree().asQueue();
    }
}
