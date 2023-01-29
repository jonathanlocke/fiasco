package digital.fiasco.runtime.build.builder.tools;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.FileList;
import digital.fiasco.runtime.build.builder.Builder;

import java.util.Collection;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;

/**
 * Base class for tools that work on files
 *
 * @author Jonathan Locke
 */
public abstract class BaseFileTool<T extends BaseFileTool<T>> extends BaseTool<T>
{
    /** The files to be processed by this tool */
    FileList files;

    /**
     * Creates a tool associated with the given builder
     *
     * @param builder The builder
     */
    public BaseFileTool(Builder builder)
    {
        super(builder);
    }

    /**
     * Creates a copy of the given tool subclass
     *
     * @param that The tool to copy
     */
    public BaseFileTool(T that)
    {
        super(that);
        this.files = that.files().copy();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public abstract T copy();

    /**
     * Returns the files selected for processing by this tool
     *
     * @return The list of files
     */
    public FileList files()
    {
        return files;
    }

    /**
     * Returns the paths of the files returned by {@link #files()} in a {@link StringList}
     *
     * @return The paths
     */
    public StringList pathsAsStringList()
    {
        var paths = stringList();
        for (var file : files())
        {
            paths.add(file.path().asString());
        }
        return paths;
    }

    /**
     * Returns a copy of this tool with the given files added
     *
     * @param files The files to add
     * @return This for chaining
     */
    public T withFiles(Collection<File> files)
    {
        var copy = copy();
        copy.files = this.files.with(files);
        return copy;
    }

    /**
     * Returns a copy of this tool with the given files excluded
     *
     * @param files The files to exclude
     * @return This for chaining
     */
    public T withoutFiles(Collection<File> files)
    {
        var copy = copy();
        copy.files = this.files.without(files);
        return copy;
    }
}
