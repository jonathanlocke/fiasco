package digital.fiasco.runtime.dependency.collections;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * A list of {@link Dependency}s.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies(Dependency[])}</li>
 *     <li>{@link #dependencies(Collection)}</li>
 * </ul>
 *
 * <p><b>Matching</b></p>
 *
 * <ul>
 *     <li>{@link #asAssetList()}</li>
 *     <li>{@link #asLibraryList()}</li>
 *     <li>{@link #matching(Matcher)}</li>
 * </ul>
 *
 * <p><b>List Operations</b></p>
 *
 * <ul>
 *     <li>{@link #count()}</li>
 *     <li>{@link #size()}</li>
 *     <li>{@link #first()}</li>
 *     <li>{@link #get(int)}</li>
 *     <li>{@link #iterator()}</li>
 *     <li>{@link #sorted()}</li>
 *     <li>{@link #join(String)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asDescriptors()}</li>
 *     <li>{@link #asStringList()}</li>
 *     <li>{@link #asMutableList()}</li>
 *     <li>{@link #asMutableSet()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link BaseDependencyList#with(Dependency)}</li>
 *     <li>{@link BaseDependencyList#with(Dependency, Dependency[])}</li>
 *     <li>{@link BaseDependencyList#with(Dependency[])}</li>
 *     <li>{@link BaseDependencyList#without(Matcher)}</li>
 *     <li>{@link BaseDependencyList#without(Collection)}</li>
 *     <li>{@link BaseDependencyList#without(Dependency)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see Artifact
 * @see Library
 * @see Asset
 * @see BaseDependencyList
 */
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
