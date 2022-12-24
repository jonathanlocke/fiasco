package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.collections.list.ObjectList;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * Base class for multi-project builds
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public abstract class MultiBuild extends BaseBuild
{
    private final ObjectList<BaseBuild> builds = list();

    private String[] arguments;

    public MultiBuild addBuild(BaseBuild build)
    {
        builds.add(build);
        return this;
    }

    public MultiBuild arguments(String[] arguments)
    {
        this.arguments = arguments;
        return this;
    }

    @Override
    protected void onRun()
    {
        builds.forEach(at -> at.run(arguments));
    }
}
