package digital.fiasco.runtime.build.tools.cleaner;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.FileList;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.tools.BaseTool;

import java.util.Collection;
import java.util.List;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.filesystem.FileList.fileList;

/**
 * Removes files matching the given pattern from the build output folder
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class Cleaner extends BaseTool
{
    /** The files to be removed */
    private FileList files;

    public Cleaner(Build build)
    {
        super(build);
    }

    public Cleaner copy()
    {
        var copy = new Cleaner(this);
        copy.files = files.copy();
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description()
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

    /**
     * Records the list of files to remove
     *
     * @param files The files to remove
     * @return This for chaining
     */
    public Cleaner withAdditionalFiles(Collection<File> files)
    {
        var copy = copy();
        this.files = fileList(this.files).with(files);
        return this;
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

    /**
     * {@inheritDoc}
     */
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
