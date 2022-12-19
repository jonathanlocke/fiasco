package digital.fiasco.runtime.build.tools.cleaner;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.filesystem.File;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.tools.BaseTool;

import java.util.List;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.filesystem.FileList.fileList;

/**
 * Removes files matching the given pattern from the build output folder
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Cleaner extends BaseTool
{
    /** The files to be removed */
    private ObjectList<File> files = list();

    public Cleaner(Build build)
    {
        super(build);
    }

    public Cleaner copy()
    {
        var copy = new Cleaner(associatedBuild());
        copy.files = files.copy();
        return copy;
    }

    /**
     * Records the list of files to remove
     *
     * @param files The files to remove
     * @return This for chaining
     */
    public Cleaner withFiles(List<File> files)
    {
        var copy = copy();
        this.files = fileList(this.files.with(files));
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
