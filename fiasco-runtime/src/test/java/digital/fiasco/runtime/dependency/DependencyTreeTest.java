package digital.fiasco.runtime.dependency;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.Library;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifacts;
import static digital.fiasco.runtime.dependency.artifact.Library.library;
import static digital.fiasco.runtime.dependency.artifact.LibraryList.libraries;

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

        var group1 = queue.nextReadyGroup(Library.class);
        ensure(group1.equals(libraries(c, e, f)));
        queue.resolveGroup(group1);

        var group2 = queue.nextReadyGroup(Library.class);
        ensure(group2.equals(libraries(b, d)));
        queue.resolveGroup(group2);

        var group3 = queue.nextReadyGroup(Library.class);
        ensure(group3.equals(DependencyList.dependencies(a)));
        queue.resolveGroup(group3);
    }

    @Test
    public void testDepthFirst()
    {
        var tree = libraryTree();
        var depthFirst = tree.depthFirst().asArtifactDescriptors();
        ensureEqual(depthFirst, artifacts(c, b, e, f, d, a).asArtifactDescriptors());
    }

    @Test
    public void testRoot()
    {
        var tree = libraryTree();
        ensure(tree.root().equals(a));
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
