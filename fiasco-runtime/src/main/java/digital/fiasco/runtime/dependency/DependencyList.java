package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.collection.Addable;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

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
 * <p><b>Filtering</b></p>
 *
 * <ul>
 *     <li>{@link #matching(Matcher)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asArtifactList()}</li>
 *     <li>{@link #asArtifactDescriptors()}</li>
 *     <li>{@link #asList()}</li>
 *     <li>{@link #asSet()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #with(DependencyList)}</li>
 *     <li>{@link #with(Iterable)}</li>
 *     <li>{@link #with(Dependency)}</li>
 *     <li>{@link #with(Dependency, Dependency...)}</li>
 *     <li>{@link #without(Matcher)}</li>
 *     <li>{@link #without(Collection)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class DependencyList<T extends Dependency> implements
    Iterable<T>,
    Addable<T>,
    Sized
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
    @FormatProperty
    private ObjectList<T> dependencies = list();

    public DependencyList()
    {
    }

    protected DependencyList(Collection<T> dependencies)
    {
        this.dependencies.addAll(dependencies);
    }

    public ObjectList<ArtifactDescriptor> asArtifactDescriptors()
    {
        return dependencies.map(Dependency::descriptor);
    }

    public DependencyList<Artifact<?>> asArtifactList()
    {
        var wildcard = new DependencyList<Artifact<?>>();
        for (var at : dependencies)
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
    public ObjectList<T> asList()
    {
        return dependencies.copy();
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    public ObjectSet<T> asSet()
    {
        return new ObjectSet<>(dependencies.copy());
    }

    public String asString()
    {
        return dependencies.asString();
    }

    public StringList asStringList()
    {
        return dependencies.asStringList();
    }

    public boolean containsAll(DependencyList<T> dependencies)
    {
        return this.dependencies.containsAll(dependencies.dependencies);
    }

    /**
     * Returns an immutable copy of this dependency list.
     *
     * @return The copy
     */
    public DependencyList<T> copy()
    {
        return new DependencyList<>(dependencies.copy());
    }

    public Count count()
    {
        return dependencies.count();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof DependencyList<?> that)
        {
            return this.dependencies.containsAll(that.dependencies)
                && that.dependencies.containsAll(this.dependencies);
        }
        return false;
    }

    public T first()
    {
        return dependencies.first();
    }

    public T get(int index)
    {
        return dependencies.get(index);
    }

    @Override
    public int hashCode()
    {
        return dependencies.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Iterator<T> iterator()
    {
        return dependencies.iterator();
    }

    public String join(String separator)
    {
        return dependencies.join(separator);
    }

    public T last()
    {
        return dependencies.last();
    }

    public DependencyList<T> matching(Matcher<T> matcher)
    {
        var copy = copy();
        copy.dependencies = copy.dependencies.matching(matcher);
        return copy;
    }

    @Override
    public boolean onAdd(T value)
    {
        return dependencies.add(value);
    }

    @Override
    public int size()
    {
        return dependencies.size();
    }

    public DependencyList<T> sorted()
    {
        return dependencyList(dependencies.sorted());
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
        copy.add(first);
        copy.addAll(dependencies);
        return copy;
    }

    public DependencyList<T> with(T value)
    {
        var copy = copy();
        copy.dependencies = dependencies.with(value);
        return copy;
    }

    public DependencyList<T> with(T[] value)
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
        copy.addAll(dependencies.dependencies);
        return copy;
    }

    /**
     * Returns this list with the given dependencies
     *
     * @return A copy of this list with the given dependencies
     */
    public DependencyList<T> with(Iterable<T> dependencies)
    {
        var copy = copy();
        copy.addAll(dependencies);
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
    public DependencyList<T> without(Matcher<T> pattern)
    {
        var copy = copy();
        copy.dependencies.removeIf(pattern::matches);
        return copy;
    }
}
