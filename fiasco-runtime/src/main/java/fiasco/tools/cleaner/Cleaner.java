package fiasco.tools.cleaner;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.ResourcePathed;
import fiasco.tools.Tools;
import fiasco.tools.BaseTool;

@SuppressWarnings("unused")
public class Cleaner extends BaseTool
{
    private final Matcher<ResourcePathed> matcher;

    public Cleaner(Tools module, Matcher<ResourcePathed> matcher)
    {
        super(module);
        this.matcher = matcher;
    }

    @Override
    protected void onRun()
    {
        module().outputFolder()
                .nestedFiles(matcher)
                .forEach(File::delete);
    }
}
