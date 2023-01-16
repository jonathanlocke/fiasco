package digital.fiasco.runtime.dependency;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.Library;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.DependencyTree.dependencyTree;
import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifactList;
import static digital.fiasco.runtime.dependency.artifact.Library.library;

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
        var queue = libraryTree().asQueue();

        var group1 = queue.nextReadyGroup();
        ensure(group1.equals(dependencyList(c, e, f)));
        queue.resolveGroup(group1);

        var group2 = queue.nextReadyGroup();
        ensure(group2.equals(dependencyList(b, d)));
        queue.resolveGroup(group2);

        var group3 = queue.nextReadyGroup();
        ensure(group3.equals(dependencyList(a)));
        queue.resolveGroup(group3);
    }

    @Test
    public void testRoot()
    {
        var tree = libraryTree();
        ensure(tree.root().equals(a));
    }

    @Test
    public void testDepthFirst()
    {
        var tree = libraryTree();
        var depthFirst = tree.depthFirst().asArtifactDescriptors();
        ensureEqual(depthFirst, artifactList(c, b, e, f, d, a).asArtifactDescriptors());
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
}
