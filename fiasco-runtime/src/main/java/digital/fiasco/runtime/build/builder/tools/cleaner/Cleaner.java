package digital.fiasco.runtime.build.builder.tools.cleaner;

import com.telenav.kivakit.filesystem.File;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseFileTool;
import digital.fiasco.runtime.build.builder.tools.archiver.Archiver;

import java.util.Collection;

import static com.telenav.kivakit.core.string.Formatter.format;

/**
 * Removes files matching the given pattern from the build output folder
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class Cleaner extends BaseFileTool
{
    public Cleaner(Builder builder)
    {
        super(builder);
    }

    @Override
    public Cleaner copy()
    {
        return new Cleaner(associatedBuilder());
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String description()
    {
        return format("""
            Cleaner
              files:
            $
            """, pathsAsStringList().indented(4).join("\n"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRun()
    {
        information("Cleaning $ files", files().count());

        files().forEach(file ->
        {
            file.delete();
            var parent = file.parent();
            if (parent.isEmpty())
            {
                parent.delete();
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param files {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Cleaner withFiles(Collection<File> files)
    {
        return (Cleaner) super.withFiles(files);
    }

    /**
     * {@inheritDoc}
     *
     * @param files {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Cleaner withoutFiles(Collection<File> files)
    {
        return (Cleaner) super.withoutFiles(files);
    }
}
