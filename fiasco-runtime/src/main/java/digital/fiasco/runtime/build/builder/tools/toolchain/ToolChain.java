package digital.fiasco.runtime.build.builder.tools.toolchain;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.object.Copyable;
import digital.fiasco.runtime.build.builder.tools.toolchain.jdk.Jdk;

import static com.telenav.kivakit.core.collections.map.ObjectMap.map;
import static com.telenav.kivakit.core.version.Version.version;

public class ToolChain extends BaseComponent implements Copyable<ToolChain>
{
    private ObjectMap<Version, Jdk> jdks = map();

    public ToolChain()
    {
        register(this);
    }

    protected ToolChain(ToolChain that)
    {
        this.jdks = that.jdks.copy();
        register(this);
    }

    @Override
    public ToolChain copy()
    {
        return new ToolChain(this);
    }

    public Jdk jdk(String version)
    {
        return jdk(version(version));
    }

    public Jdk jdk(Version version)
    {
        return jdks.get(version);
    }

    public ToolChain with(Jdk jdk)
    {
        return mutatedCopy(it -> it.jdks.put(jdk.version(), jdk));
    }
}
