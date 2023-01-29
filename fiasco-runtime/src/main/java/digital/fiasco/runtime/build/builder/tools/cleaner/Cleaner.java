package digital.fiasco.runtime.build.builder.tools.cleaner;

import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseFileTool;

import static com.telenav.kivakit.core.string.Formatter.format;

/**
 * Removes files matching the given pattern from the build output folder
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class Cleaner extends BaseFileTool<Cleaner>
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
        step("Cleaning $ files", files().count());

        files().forEach(file ->
        {
            step(file::delete, "Deleting $", file);
            var parent = file.parent();
            if (parent.isEmpty())
            {
                step(parent::delete, "Deleting $", parent);
            }
        });
    }
}
