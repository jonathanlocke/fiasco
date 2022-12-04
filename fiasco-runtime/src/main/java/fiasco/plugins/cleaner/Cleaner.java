package fiasco.plugins.cleaner;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.ResourcePathed;
import fiasco.Module;
import fiasco.plugins.Plugin;

@SuppressWarnings("unused")
public class Cleaner extends Plugin
{
    private final Matcher<ResourcePathed> matcher;

    public Cleaner(Module module, Matcher<ResourcePathed> matcher)
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
