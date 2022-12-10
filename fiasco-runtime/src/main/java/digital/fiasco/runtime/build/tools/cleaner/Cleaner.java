package digital.fiasco.runtime.build.tools.cleaner;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.ResourcePathed;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.tools.BaseTool;
import digital.fiasco.runtime.build.tools.Matchers;

import static com.telenav.kivakit.core.string.Formatter.format;

/**
 * Removes files matching the given pattern from the build output folder
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Cleaner extends BaseTool
{
    /** The matchers that match files to be removed */
    private Matchers matchers = new Matchers();

    /** The root folder to clean */
    private Folder folder;

    public Cleaner(Build build)
    {
        super(build);
        folder = build.targetFolder();
    }

    public Cleaner(Cleaner that)
    {
        super(that.associatedBuild());
        matchers = that.matchers.copy();
        folder = that.folder;
    }

    public Cleaner copy()
    {
        return new Cleaner(this);
    }

    public Cleaner withFolder(Folder that)
    {
        var copy = copy();
        copy.folder = folder;
        return this;
    }

    public Cleaner withMatcher(Matcher<ResourcePathed> matcher)
    {
        var copy = copy();
        matchers.add(matcher);
        return this;
    }

    @Override
    protected String description()
    {
        return format("""
                Cleaner
                  files: $
                  matchers: $
                """ , folder, matchers);
    }

    @Override
    protected void onRun()
    {
        information("Cleaning");

        folder.nestedFiles(matchers)
                .forEach(File::delete);
    }
}
