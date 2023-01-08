package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

/**
 * An immutable, ordered list of {@link Dependency} objects.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #dependencyList(Dependency[])} - Variable arguments factory method</li>
 *     <li>{@link #dependencyList(Collection)} - List factory method</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asList()}</li>
 *     <li>{@link #asSet()}</li>
 *     <li>{@link #asArtifactList()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #with(DependencyList)}</li>
 *     <li>{@link #with(Iterable)}</li>
 *     <li>{@link #with(Dependency, Dependency...)}</li>
 *     <li>{@link #without(Matcher)} - Returns a copy of this list without the given dependencies</li>
 *     <li>{@link #without(Collection) - Returns a copy of this list without the given dependencies}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class DependencyList extends ObjectList<Dependency>
{
    /**
     * Creates a list of dependencies
     *
     * @param dependencies The dependencies to add
     * @return The dependency list
     */
    public static DependencyList dependencyList(Dependency... dependencies)
    {
        return new DependencyList(list(dependencies));
    }

    /**
     * Creates a list of dependencies
     *
     * @param dependencies The dependencies to add
     * @return The dependency list
     */
    public static DependencyList dependencyList(Collection<Dependency> dependencies)
    {
        return new DependencyList(dependencies);
    }

    /** The underlying dependencies */
    private ObjectList<Dependency> dependencies = list();

    protected DependencyList()
    {
    }

    protected DependencyList(Collection<Dependency> dependencies)
    {
        this.dependencies.addAll(dependencies);
    }

    public ObjectList<Artifact> asArtifactList()
    {
        var artifacts = new ObjectList<Artifact>();
        dependencies.forEach(at -> artifacts.add((Artifact) at));
        return artifacts;
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    @Override
    public ObjectList<Dependency> asList()
    {
        return dependencies.copy();
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    @Override
    public ObjectSet<Dependency> asSet()
    {
        return new ObjectSet<>(dependencies.copy());
    }

    /**
     * Returns an immutable copy of this dependency list.
     *
     * @return The copy
     */
    @Override
    public DependencyList copy()
    {
        return new DependencyList(dependencies.copy());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<Dependency> iterator()
    {
        return dependencies.iterator();
    }

    @Override
    public DependencyList matching(Matcher<Dependency> matcher)
    {
        var matching = new DependencyList();
        matching.dependencies = matching.dependencies.matching(matcher);
        return matching;
    }

    /**
     * Returns this list with the given dependencies
     *
     * @param dependencies A variable-argument list of dependencies to include
     * @return A copy of this list with the given dependencies
     */
    public final DependencyList with(Dependency first, Dependency... dependencies)
    {
        var copy = copy();
        copy.dependencies.add(first);
        copy.dependencies.addAll(dependencies);
        return copy;
    }

    @Override
    public DependencyList with(Dependency value)
    {
        var copy = copy();
        copy.dependencies = dependencies.with(value);
        return copy;
    }

    /**
     * Returns this list with the given dependencies
     *
     * @return A copy of this list with the given dependencies
     */
    public DependencyList with(DependencyList dependencies)
    {
        var copy = copy();
        copy.dependencies.addAll(dependencies.dependencies);
        return copy;
    }

    /**
     * Returns this list with the given dependencies
     *
     * @return A copy of this list with the given dependencies
     */
    @Override
    public DependencyList with(Iterable<Dependency> dependencies)
    {
        var copy = copy();
        copy.dependencies.addAll(dependencies);
        return copy;
    }

    /**
     * Returns a copy of this list without the given dependencies
     *
     * @param exclusions The dependencies to exclude
     * @return The dependency list
     */
    public DependencyList without(Collection<Dependency> exclusions)
    {
        var copy = copy();
        copy.dependencies.removeAll(exclusions);
        return copy;
    }

    /**
     * Returns a copy of this dependency list without all dependencies matching the given pattern
     *
     * @param pattern The pattern to match
     * @return A copy of this list without the specified dependencies
     */
    @Override
    public DependencyList without(Matcher<Dependency> pattern)
    {
        var copy = copy();
        copy.dependencies.removeIf(pattern::matches);
        return copy;
    }
}
