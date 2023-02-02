package digital.fiasco.runtime.dependency.collections.lists;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import digital.fiasco.runtime.dependency.Dependency;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;

@SuppressWarnings("unused")
public class DependencyList extends BaseDependencyList<Dependency, DependencyList>
{
    /**
     * Creates a list of dependencies
     *
     * @param dependencies The dependencies to add
     * @return The dependency list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static DependencyList dependencies(Dependency... dependencies)
    {
        return dependencies(list(dependencies));
    }

    /**
     * Creates a list of dependencies
     *
     * @param dependencies The dependencies to add
     * @return The dependency list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static DependencyList dependencies(Collection<Dependency> dependencies)
    {
        return new DependencyList(dependencies);
    }

    public DependencyList()
    {
    }

    public DependencyList(DependencyList that)
    {
        super(that);
    }

    public DependencyList(Collection<Dependency> dependencies)
    {
        super(dependencies);
    }

    @Override
    protected DependencyList newList(DependencyList that)
    {
        return new DependencyList(that);
    }

    @Override
    protected DependencyList newList()
    {
        return new DependencyList();
    }
}
