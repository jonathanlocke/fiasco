package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
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
public class DependencyList<T extends Dependency> extends ObjectList<T>
{
    /**
     * Creates a list of dependencies
     *
     * @param dependencies The dependencies to add
     * @return The dependency list
     */
    @SafeVarargs
    public static <T extends Dependency> DependencyList<T> dependencyList(T... dependencies)
    {
        return new DependencyList<>(list(dependencies));
    }

    /**
     * Creates a list of dependencies
     *
     * @param dependencies The dependencies to add
     * @return The dependency list
     */
    public static <T extends Dependency> DependencyList<T> dependencyList(Collection<T> dependencies)
    {
        return new DependencyList<>(dependencies);
    }

    /** The underlying dependencies */
    private ObjectList<T> dependencies = list();

    public DependencyList()
    {
    }

    protected DependencyList(Collection<T> dependencies)
    {
        this.dependencies.addAll(dependencies);
    }

    public ObjectList<ArtifactDescriptor> artifactDescriptors()
    {
        return map(Dependency::artifactDescriptor);
    }

    public DependencyList<Artifact<?>> asArtifactList()
    {
        var wildcard = new DependencyList<Artifact<?>>();
        for (var at : this)
        {
            wildcard.add((Artifact<?>) at);
        }
        return wildcard;
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    @Override
    public ObjectList<T> asList()
    {
        return dependencies.copy();
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    @Override
    public ObjectSet<T> asSet()
    {
        return new ObjectSet<>(dependencies.copy());
    }

    /**
     * Returns an immutable copy of this dependency list.
     *
     * @return The copy
     */
    @Override
    public DependencyList<T> copy()
    {
        return new DependencyList<>(dependencies.copy());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<T> iterator()
    {
        return dependencies.iterator();
    }

    @Override
    public DependencyList<T> matching(Matcher<T> matcher)
    {
        var matching = new DependencyList<T>();
        matching.dependencies = matching.dependencies.matching(matcher);
        return matching;
    }

    /**
     * Returns this list with the given dependencies
     *
     * @param dependencies A variable-argument list of dependencies to include
     * @return A copy of this list with the given dependencies
     */
    @SafeVarargs
    public final DependencyList<T> with(T first, T... dependencies)
    {
        var copy = copy();
        copy.dependencies.add(first);
        copy.dependencies.addAll(dependencies);
        return copy;
    }

    @Override
    public DependencyList<T> with(T value)
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
    public DependencyList<T> with(DependencyList<T> dependencies)
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
    public DependencyList<T> with(Iterable<T> dependencies)
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
    public DependencyList<T> without(Collection<T> exclusions)
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
    public DependencyList<T> without(Matcher<T> pattern)
    {
        var copy = copy();
        copy.dependencies.removeIf(pattern::matches);
        return copy;
    }
}
