package fiasco.structure;

import com.telenav.kivakit.filesystem.FileList;
import com.telenav.kivakit.filesystem.Folder;
import fiasco.glob.Glob;

/**
 * Information about the folder structure of the build.
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface Structure extends Glob
{
    /**
     * output/classes
     */
    default Folder classesFolder()
    {
        return outputFolder().folder("classes");
    }

    /**
     * Returns the folder for the given key. For example "src" or "output".
     *
     * @param key The key
     * @return The folder
     */
    Folder folder(String key);

    /**
     * Sets the folder for the given key. For example "src" or "output".
     *
     * @param key The key
     * @param folder The folder
     */
    void folder(String key, Folder folder);

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
        return folder("output");
    }

    /**
     * src
     */
    default Folder sourceFolder()
    {
        return folder("src");
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
