package digital.fiasco.runtime.build;

import com.telenav.kivakit.filesystem.FileList;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.Rooted;
import com.telenav.kivakit.resource.ResourceList;

import static com.telenav.kivakit.resource.ResourceGlob.glob;

/**
 * Information about the folder structure of a build.
 *
 * <p><b>Source Folders</b></p>
 *
 * <ul>
 *     <li>{@link #sourceFolder()} - src</li>
 *     <li>{@link #sourceMainJavaFolder()} - src/main/java</li>
 *     <li>{@link #sourceMainResourcesFolder()} - src/main/resources</li>
 *     <li>{@link #sourceTestFolder()} - src/test</li>
 *     <li>{@link #sourceTestJavaFolder()} - src/test/java</li>
 *     <li>{@link #sourceTestResourcesFolder()} - src/test/resources</li>
 * </ul>
 *
 * <p><b>Target Folders</b></p>
 *
 * <ul>
 *     <li>{@link #targetClassesFolder()} - target/classes</li>
 *     <li>{@link #targetTestClassesFolder()} - target/test-classes</li>
 * </ul>
 *
 * <p><b>Sources</b></p>
 *
 * <ul>
 *     <li>{@link #sourceMainJavaSources()} - src/main/java/**.java</li>
 *     <li>{@link #sourceMainResources()} - src/main/resources/**</li>
 *     <li>{@link #sourceTestJavaSources()} - src/test/java/**.java</li>
 *     <li>{@link #sourceTestResources()} - src/test/resources/**</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface BuildStructured extends Rooted
{
    /**
     * src
     */
    default Folder sourceFolder()
    {
        return rootFolder().folder("src");
    }

    /**
     * src/main
     */
    default Folder sourceMainFolder()
    {
        return sourceFolder().folder("main");
    }

    /**
     * src/main/java
     */
    default Folder sourceMainJavaFolder()
    {
        return sourceMainFolder().folder("java");
    }

    /**
     * src/main/java/**&#47;*.java
     */
    default FileList sourceMainJavaSources()
    {
        return sourceMainJavaFolder().nestedFiles(glob("**/*.java"));
    }

    /**
     * src/main/resources/**
     */
    default FileList sourceMainResources()
    {
        return sourceMainResourcesFolder().nestedFiles(glob("**"));
    }

    /**
     * src/main/resources
     */
    default Folder sourceMainResourcesFolder()
    {
        return sourceMainFolder().folder("resources");
    }

    /**
     * src/test
     */
    default Folder sourceTestFolder()
    {
        return sourceFolder().folder("test");
    }

    /**
     * src/test/java
     */
    default Folder sourceTestJavaFolder()
    {
        return sourceTestFolder().folder("java");
    }

    /**
     * src/test/java/**&#47;*.java
     */
    default ResourceList sourceTestJavaSources()
    {
        return sourceTestJavaFolder().nestedResources(glob("**/*.java"));
    }

    /**
     * src/test/resources/**
     */
    default FileList sourceTestResources()
    {
        return sourceTestResourcesFolder().nestedFiles(glob("**"));
    }

    /**
     * src/test/resources
     */
    default Folder sourceTestResourcesFolder()
    {
        return sourceTestFolder().folder("resources");
    }

    /**
     * target/classes
     */
    default Folder targetClassesFolder()
    {
        return targetFolder().folder("classes");
    }

    /**
     * target
     */
    default Folder targetFolder()
    {
        return rootFolder().folder("target");
    }

    /**
     * target/test-classes
     */
    default Folder targetTestClassesFolder()
    {
        return targetFolder().folder("test-classes");
    }
}
