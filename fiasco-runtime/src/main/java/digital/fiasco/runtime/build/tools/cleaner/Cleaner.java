package digital.fiasco.runtime.build.tools.cleaner;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.FileList;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.tools.BaseTool;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.string.Formatter.format;

/**
 * Removes files matching the given pattern from the build output folder
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Cleaner extends BaseTool
{
    /** The files to be removed */
    private final FileList files = new FileList();

    public Cleaner(Build build)
    {
        super(build);
    }

    public Cleaner include(Iterable<File> files)
    {
        this.files.addAll(files);
        return this;
    }

    public Cleaner exclude(Iterable<File> files)
    {
        for (var file : files)
        {
            this.files.remove(file);
        }
        return this;
    }

    @Override
    protected String description()
    {
        var paths = stringList();
        for (var file : files)
        {
            paths.add(file.path().asString());
        }
        return format("""
                Cleaner
                  files:
                $
                """, paths.indented(4).join("\n"));
    }

    @Override
    protected void onRun()
    {
        information("Cleaning $ files", files.count());

        files.forEach(file ->
                {
                    file.delete();
                    var parent = file.parent();
                    if (parent.isEmpty())
                    {
                        parent.delete();
                    }
                });
    }
}
