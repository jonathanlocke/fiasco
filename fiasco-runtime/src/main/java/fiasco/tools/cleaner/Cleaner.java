package fiasco.tools.cleaner;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.ResourcePathed;
import fiasco.BaseBuild;
import fiasco.tools.BaseTool;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * Removes files matching the given pattern from the build output folder
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Cleaner extends BaseTool
{
    private final ObjectList<Matcher<ResourcePathed>> matchers = list();

    public Cleaner(BaseBuild build)
    {
        super(build);
    }

    public Cleaner matching(Matcher<ResourcePathed> matcher)
    {
        matchers.add(matcher);
        return this;
    }

    @Override
    protected void onRun()
    {
        for (var matcher : matchers)
        {
            build().outputFolder()
                    .nestedFiles(matcher)
                    .forEach(File::delete);
        }
    }
}
