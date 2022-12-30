package digital.fiasco.runtime.build.builder;

import com.telenav.kivakit.core.value.count.Count;

public class BuilderSettings
{
    private Count threads;

    public BuilderSettings()
    {
    }

    public BuilderSettings(BuilderSettings that)
    {
        this.threads = that.threads;
    }

    public BuilderSettings copy()
    {
        return new BuilderSettings(this);
    }

    public Count threads()
    {
        return threads;
    }

    public BuilderSettings withThreads(Count threads)
    {
        var copy = copy();
        copy.threads = threads;
        return copy;
    }
}
