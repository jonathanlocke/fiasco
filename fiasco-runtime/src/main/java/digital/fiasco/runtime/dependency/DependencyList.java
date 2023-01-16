package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * An immutable, ordered list of {@link Dependency} objects.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies(Dependency[])} - Variable arguments factory method</li>
 *     <li>{@link #dependencies(Collection)} - List factory method</li>
 * </ul>
 *
 * <p><b>Filtering</b></p>
 *
 * <ul>
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
 *     <li>{@link #containsAll(DependencyList)}</li>
 *     <li>{@link #sorted()}</li>
 *     <li>{@link #join(String)}</li>
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
 *     <li>{@link #with(Dependency)}</li>
 *     <li>{@link #with(Dependency, Dependency...)}</li>
 *     <li>{@link #without(Matcher)}</li>
 *     <li>{@link #without(Collection)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
@TypeQuality
    (
        documentation = DOCUMENTED,
        testing = TESTING_INSUFFICIENT,
        stability = STABILITY_UNDETERMINED
    )
public class DependencyList<T extends Dependency> implements
    Iterable<T>,
    Sized
{
    /**
     * Creates a list of dependencies
     *
     * @param dependencies The dependencies to add
     * @return The dependency list
     */
    @SafeVarargs
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static <T extends Dependency> DependencyList<T> dependencies(T... dependencies)
    {
        return new DependencyList<>(list(dependencies));
    }

    /**
     * Creates a list of dependencies
     *
     * @param dependencies The dependencies to add
     * @return The dependency list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static <T extends Dependency> DependencyList<T> dependencies(Collection<T> dependencies)
    {
        return new DependencyList<>(dependencies);
    }

    /** The underlying dependencies */
    @FormatProperty
    private ObjectList<T> dependencies = list();

    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTING_NOT_NEEDED)
    public DependencyList()
    {
    }

    protected DependencyList(DependencyList<T> that)
    {
        this.dependencies = that.dependencies.copy();
    }

    protected DependencyList(Collection<T> dependencies)
    {
        this.dependencies.addAll(dependencies);
    }

    /**
     * Returns the dependencies in this list as {@link ArtifactDescriptor}s
     *
     * @return The list of descriptors
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ObjectList<ArtifactDescriptor> asArtifactDescriptors()
    {
        return dependencies.map(Dependency::descriptor);
    }

    /**
     * Returns an {@link ArtifactList} containing all artifacts in this dependency list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactList asArtifactList()
    {
        var artifacts = ArtifactList.artifacts();
        for (var at : dependencies)
        {
            if (at instanceof Artifact<?> artifact)
            {
                artifacts = artifacts.with(artifact);
            }
        }
        return artifacts;
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ObjectList<T> asList()
    {
        return dependencies.copy();
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ObjectSet<T> asSet()
    {
        return new ObjectSet<>(new LinkedHashSet<>(dependencies.copy()));
    }

    /**
     * Returns this dependency list as a string
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public String asString()
    {
        return dependencies.asString();
    }

    /**
     * Returns the descriptors of artifacts in this list as a string list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public StringList asStringList()
    {
        return dependencies.asStringList();
    }

    /**
     * Returns true if this list contains the given dependency
     *
     * @param dependency The dependency
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean contains(T dependency)
    {
        return dependencies.contains(dependency);
    }

    /**
     * Returns true if this dependency list contains all the dependencies in the given list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean containsAll(DependencyList<T> dependencies)
    {
        return this.dependencies.containsAll(dependencies.dependencies);
    }

    /**
     * Returns an immutable copy of this dependency list.
     *
     * @return The copy
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> copy()
    {
        return new DependencyList<>(dependencies.copy());
    }

    /**
     * Returns the count of dependencies in this list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public Count count()
    {
        return dependencies.count();
    }

    public DependencyList<T> deduplicate()
    {
        var deduplicated = new DependencyList<T>();
        for (var at : this)
        {
            if (!deduplicated.contains(at))
            {
                deduplicated = deduplicated.with(at);
            }
        }
        return deduplicated;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTED)
    public boolean equals(Object object)
    {
        if (object instanceof DependencyList<?> that)
        {
            return this.dependencies.size() == that.dependencies.size()
                && this.dependencies.containsAll(that.dependencies)
                && that.dependencies.containsAll(this.dependencies);
        }
        return false;
    }

    /**
     * Returns the first dependency in this list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public T first()
    {
        return dependencies.first();
    }

    /**
     * Returns the dependency at the given index in this list
     *
     * @param index The index
     * @return The dependency at the given index
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >= size()})
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public T get(int index)
    {
        return dependencies.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTED)
    public int hashCode()
    {
        return dependencies.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTING_NOT_NEEDED)
    public Iterator<T> iterator()
    {
        return dependencies.iterator();
    }

    /**
     * Returns the dependencies in this list as a string of artifact descriptors separated with the given separator
     *
     * @param separator The separator
     * @return The joined string
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public String join(String separator)
    {
        return dependencies.join(separator);
    }

    /**
     * Returns the last dependency in this list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public T last()
    {
        return dependencies.last();
    }

    /**
     * Returns a new list with only the dependencies in this list that match the given matcher
     *
     * @param matcher The matcher
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> matching(Matcher<T> matcher)
    {
        var copy = copy();
        copy.dependencies = copy.dependencies.matching(matcher);
        return copy;
    }

    /**
     * Returns the size of this list in elements
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public int size()
    {
        return dependencies.size();
    }

    /**
     * Returns a copy of this list in natural sorted order
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> sorted()
    {
        return dependencies(dependencies.sorted());
    }

    @Override
    public String toString()
    {
        return dependencies.join(", ");
    }

    /**
     * Returns a copy of this list with the given dependencies added
     *
     * @param first The first dependency to include
     * @param rest The dependencies to include
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> with(T first, T[] rest)
    {
        var copy = copy();
        copy.dependencies.add(first);
        copy.dependencies.addAll(rest);
        return copy;
    }

    /**
     * Returns a copy of this list with the given dependency added
     *
     * @param inclusion The dependency to included
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> with(T inclusion)
    {
        var copy = copy();
        copy.dependencies = dependencies.with(inclusion);
        return copy;
    }

    /**
     * Returns a copy of this list with the given dependencies added
     *
     * @param inclusions The dependencies to included
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> with(T[] inclusions)
    {
        var copy = copy();
        copy.dependencies = dependencies.with(inclusions);
        return copy;
    }

    /**
     * Returns a copy of this list with the given dependencies added
     *
     * @param inclusions The dependencies to included
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> with(DependencyList<T> inclusions)
    {
        var copy = copy();
        copy.dependencies.addAll(inclusions);
        return copy;
    }

    /**
     * Returns a copy of this list without the given dependencies
     *
     * @param exclusions The dependencies to exclude
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> without(Collection<T> exclusions)
    {
        var copy = copy();
        copy.dependencies.removeAll(exclusions);
        return copy;
    }

    /**
     * Returns a copy of this list without the given dependency
     *
     * @param exclusion The dependencies to exclude
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> without(T exclusion)
    {
        var copy = copy();
        copy.dependencies.remove(exclusion);
        return copy;
    }

    /**
     * Returns a copy of this list without the given dependencies
     *
     * @param exclusions The dependencies to exclude
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> without(DependencyList<T> exclusions)
    {
        var copy = copy();
        copy.dependencies.removeAll(exclusions.dependencies);
        return copy;
    }

    /**
     * Returns a copy of this dependency list without all dependencies matching the given pattern
     *
     * @param exclusionPattern The pattern to match
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public DependencyList<T> without(Matcher<T> exclusionPattern)
    {
        var copy = copy();
        copy.dependencies.removeIf(exclusionPattern::matches);
        return copy;
    }
}
