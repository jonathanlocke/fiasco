package digital.fiasco.runtime.build;

import com.telenav.kivakit.filesystem.FileList;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.Rooted;
import com.telenav.kivakit.resource.ResourceList;

import static com.telenav.kivakit.resource.ResourceGlob.glob;

/**
 * Information about the folder structure of a build.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface BuildStructure extends Rooted
{
    /**
     * output/classes
     */
    default Folder classesFolder()
    {
        return targetFolder().folder("classes");
    }

    /**
     * src/main/java
     */
    default Folder javaSourceFolder()
    {
        return mainSourceFolder().folder("java");
    }

    /**
     * src/main/java/**&#47;*.java
     */
    default FileList javaSources()
    {
        return javaSourceFolder().nestedFiles(glob("**/*.java"));
    }

    /**
     * src/test/java
     */
    default Folder javaTestSourceFolder()
    {
        return testSourceFolder().folder("java");
    }

    /**
     * src/main/resources
     */
    default Folder mainResourceFolder()
    {
        return mainSourceFolder().folder("resources");
    }

    /**
     * src/main
     */
    default Folder mainSourceFolder()
    {
        return sourceFolder().folder("main");
    }

    /**
     * src
     */
    default Folder sourceFolder()
    {
        return rootFolder().folder("src");
    }

    /**
     * output
     */
    default Folder targetFolder()
    {
        return rootFolder().folder("target");
    }

    /**
     * output/classes
     */
    default Folder testClassesFolder()
    {
        return targetFolder().folder("test-classes");
    }

    /**
     * src/test/java/**&#47;*.java
     */
    default ResourceList testJavaSources()
    {
        return javaTestSourceFolder().nestedResources(glob("**/*.java"));
    }

    /**
     * src/test/resources
     */
    default Folder testResourceFolder()
    {
        return testSourceFolder().folder("resources");
    }

    /**
     * src/test
     */
    default Folder testSourceFolder()
    {
        return sourceFolder().folder("test");
    }
}
