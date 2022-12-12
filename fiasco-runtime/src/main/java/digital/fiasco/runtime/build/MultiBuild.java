package digital.fiasco.runtime.build;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.core.collections.list.ObjectList;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class MultiBuild extends Application
{
    private final ObjectList<Build> builds = list();

    private String[] arguments;

    public MultiBuild addBuild(Build build)
    {
        builds.add(build);
        return this;
    }

    public String[] arguments()
    {
        return arguments;
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
