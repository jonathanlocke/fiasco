package digital.fiasco.runtime.build;

import com.telenav.kivakit.filesystem.FileList;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.ResourceList;

import static com.telenav.kivakit.filesystem.Folder.parseFolder;
import static com.telenav.kivakit.resource.ResourceGlob.glob;

/**
 * Information about the folder structure of the build.
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface BuildStructure
{
    /**
     * output/classes
     */
    default Folder classesFolder()
    {
        return outputFolder().folder("classes");
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
     * output
     */
    default Folder outputFolder()
    {
        return rootFolder().folder("output");
    }

    /**
     * root
     */
    default Folder rootFolder()
    {
        return parseFolder("root");
    }

    /**
     * src
     */
    default Folder sourceFolder()
    {
        return rootFolder().folder("src");
    }

    /**
     * src/test/java/**&#47;*.java
     */
    default ResourceList testJavaSources()
    {
        return javaTestSourceFolder().nestedResources("**/*.java");
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
