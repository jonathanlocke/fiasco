package digital.fiasco.application;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.Build;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import static com.telenav.kivakit.commandline.SwitchParsers.threadCountSwitchParser;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static com.telenav.kivakit.resource.Extension.JAVA;
import static javax.tools.ToolProvider.getSystemJavaCompiler;

/**
 * Fiasco is a pure-Java build tool.
 *
 * @author jonathan
 */
public class Fiasco extends Application
{
    public static void main(String[] arguments)
    {
        new Fiasco().run(arguments);
    }

    /** Number of threads to use when extracting and converting */
    private final SwitchParser<Count> THREADS = threadCountSwitchParser(this, Count._8);

    @Override
    protected void onRun()
    {
        var fiasco = currentFolder().folder("fiasco");
        if (compile(fiasco))
        {
            execute(fiasco);
        }
        else
        {
            fail("Fiasco '$' couldn't be compiled", fiasco);
        }
    }

    @Override
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return set(THREADS);
    }

    private boolean compile(Folder folder)
    {
        if (folder.exists())
        {
            var sourceFile = folder.file("Fiasco.java");
            if (sourceFile.exists())
            {
                var compiler = getSystemJavaCompiler();
                var fileManager = compiler.getStandardFileManager(null, null, null);
                var files = fileManager.getJavaFileObjectsFromFiles(folder
                        .nestedFiles(JAVA.matcher())
                        .asJavaFiles());

                return compiler.getTask(null, fileManager, null, null, null, files).call();
            }
            else
            {
                fail("Fiasco source file '$' does not exist", sourceFile);
                return false;
            }
        }
        fail("Fiasco folder '$' does not exist", folder);
        return false;
    }

    @SuppressWarnings("resource")
    private void execute(Folder folder)
    {
        try
        {
            // Attempt to load the Fiasco class from the fiasco folder
            URLClassLoader classLoader = new URLClassLoader(new URL[] { folder.asUrl() });
            Class<?> type = classLoader.loadClass("Fiasco");
            if (type != null)
            {
                // get its constructor
                Constructor<?> constructor = type.getConstructor(Folder.class);

                // create the project object
                var project = (Build) constructor.newInstance(folder);

                // and ask it to build the project
                project.build(commandLine().get(THREADS));
            }
            else
            {
                problem("Unable to load Fiasco");
            }
        }
        catch (Exception e)
        {
            problem(e, "Unable to build Fiasco");
        }
    }
}
