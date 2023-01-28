package digital.fiasco.runtime.dependency;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static digital.fiasco.runtime.dependency.artifact.lists.ArtifactList.artifacts;
import static digital.fiasco.runtime.dependency.artifact.lists.LibraryList.libraries;

public class LibraryListTest extends FiascoTest
{
    @Test
    public void testAsLibraryList()
    {
        var libraries = libraries(kivakitCore(), kivakitApplication());
        ensure(libraries.equals(artifacts(kivakitCore(), kivakitApplication()).asLibraryList()));
    }

    @Test
    public void testCreation()
    {
        {
            var libraries = libraries(set(kivakitCore(), kivakitApplication()));
            ensure(libraries.contains(kivakitCore()));
            ensure(libraries.contains(kivakitApplication()));
        }
        {
            var libraries = libraries(set(kivakitCore().descriptor(), kivakitApplication().descriptor()));
            ensure(libraries.contains(kivakitCore()));
            ensure(libraries.contains(kivakitApplication()));
        }
        {
            var libraries = libraries(kivakitCore().descriptor(), kivakitApplication().descriptor());
            ensure(libraries.contains(kivakitCore()));
            ensure(libraries.contains(kivakitApplication()));
        }
        {
            var libraries = libraries(kivakitCore().descriptor().name(), kivakitApplication().descriptor().name());
            ensure(libraries.contains(kivakitCore()));
            ensure(libraries.contains(kivakitApplication()));
        }
    }
}
