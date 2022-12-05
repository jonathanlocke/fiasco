package fiasco.tools.cleaner;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.ResourcePathed;
import fiasco.BaseBuild;
import fiasco.tools.BaseTool;

/**
 * Removes files matching the given pattern from the build output folder
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Cleaner extends BaseTool
{
    private final Matcher<ResourcePathed> matcher;

    public Cleaner(BaseBuild build, Matcher<ResourcePathed> matcher)
    {
        super(build);
        this.matcher = matcher;
    }

    @Override
    protected void onRun()
    {
        build().outputFolder()
                .nestedFiles(matcher)
                .forEach(File::delete);
    }
}
