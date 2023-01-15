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
    public void testDepthFirst()
    {
        var tree = libraryTree();
        var depthFirst = tree.depthFirst().asArtifactDescriptors();
        ensureEqual(depthFirst, artifactList(c, b, e, f, d, a).asArtifactDescriptors());
    }

    @Test
    public void testGrouped()
    {
        var groups = libraryTree().grouped();

        //       a       <--- Group 2
        //      / \
        //     b   d     <--- Group 1
        //    /   / \
        //   c   e   f   <--- Group 0

        ensureEqual(groups.size(), 3);

        ensureEqual(groups.get(0), dependencyList(c, e, f));
        ensureEqual(groups.get(1), dependencyList(b, d));
        ensureEqual(groups.get(2), dependencyList(a));
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
