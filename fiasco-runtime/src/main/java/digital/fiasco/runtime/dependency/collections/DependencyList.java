package digital.fiasco.runtime.dependency.collections;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.lists.ArtifactList;
import digital.fiasco.runtime.dependency.artifact.artifacts.Asset;
import digital.fiasco.runtime.dependency.artifact.lists.AssetList;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;
import digital.fiasco.runtime.dependency.artifact.lists.LibraryList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static digital.fiasco.runtime.dependency.artifact.lists.AssetList.assets;
import static digital.fiasco.runtime.dependency.artifact.lists.LibraryList.libraries;

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
public class DependencyList<D extends Dependency, L extends DependencyList<D, L>> implements
    List<D>,
    Iterable<D>,
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
    public static <D extends Dependency, L extends DependencyList<D, L>> DependencyList<D, L> dependencies(
        D... dependencies)
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
    public static <D extends Dependency, L extends DependencyList<D, L>> DependencyList<D, L> dependencies(
        Collection<D> dependencies)
    {
        return new DependencyList<>(dependencies);
    }

    /** The underlying dependencies */
    @FormatProperty
    ObjectList<D> dependencies = list();

    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTING_NOT_NEEDED)
    public DependencyList()
    {
    }

    protected DependencyList(L that)
    {
        this.dependencies = that.dependencies.copy();
    }

    protected DependencyList(Collection<D> dependencies)
    {
        this.dependencies.addAll(dependencies);
    }

    @Override
    public void add(int index, D element)
    {
        unsupported();
    }

    @Override
    public boolean add(D d)
    {
        unsupported();
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends D> c)
    {
        return false;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends D> c)
    {
        return false;
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
     * Returns the {@link Asset} artifacts in this list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public AssetList asAssetList()
    {
        return assets(matching(at -> at instanceof Asset).asArtifactDescriptors());
    }

    /**
     * Returns the {@link Library} artifacts in this list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public LibraryList asLibraryList()
    {
        return libraries(matching(at -> at instanceof Library).asArtifactDescriptors());
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ObjectList<D> asList()
    {
        return dependencies.copy();
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ObjectSet<D> asSet()
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

    @Override
    public void clear()
    {

    }

    /**
     * Returns true if this list contains the given dependency
     *
     * @param dependency The dependency
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean contains(D dependency)
    {
        return dependencies.contains(dependency);
    }

    @Override
    public boolean contains(Object that)
    {
        unsupported();
        return false;
    }

    /**
     * Returns true if this dependency list contains all the dependencies in the given list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean containsAll(L dependencies)
    {
        return this.dependencies.containsAll(dependencies.dependencies);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> that)
    {
        unsupported();
        return false;
    }

    /**
     * Returns an immutable copy of this dependency list.
     *
     * @return The copy
     */
    @SuppressWarnings("unchecked")
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public L copy()
    {
        return newList((L) this);
    }

    /**
     * Returns the count of dependencies in this list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public Count count()
    {
        return dependencies.count();
    }

    public L deduplicate()
    {
        var deduplicated = newList();
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
        if (object instanceof DependencyList<?, ?> that)
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
    public D first()
    {
        return dependencies.first();
    }

    /**
     * Returns a copy of this list with up to the first N dependencies
     *
     * @param maximum The maximum number of dependencies to include in the copy
     * @return The copy
     */
    public L first(Maximum maximum)
    {
        var copy = copy();
        copy.dependencies = this.dependencies.first(maximum.count());
        return copy;
    }

    /**
     * Returns the dependency at the given index in this list
     *
     * @param index The index
     * @return The dependency at the given index
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >= size()})
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public D get(int index)
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
        return dependencies.sorted().hashCode();
    }

    @Override
    public int indexOf(Object that)
    {
        return unsupported();
    }

    @Override
    public boolean isEmpty()
    {
        return dependencies.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTING_NOT_NEEDED)
    public Iterator<D> iterator()
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
    public D last()
    {
        return dependencies.last();
    }

    @Override
    public int lastIndexOf(Object that)
    {
        return unsupported();
    }

    @NotNull
    @Override
    public ListIterator<D> listIterator()
    {
        return unsupported();
    }

    @NotNull
    @Override
    public ListIterator<D> listIterator(int index)
    {
        return unsupported();
    }

    /**
     * Maps the dependencies in this list to values in an object list
     *
     * @param mapper The mapping function
     * @return The mapped list
     */
    public <To> ObjectList<To> map(Function<D, To> mapper)
    {
        return dependencies.map(mapper);
    }

    /**
     * Returns a new list with only the dependencies in this list that match the given matcher
     *
     * @param matcher The matcher
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public L matching(Matcher<D> matcher)
    {
        var copy = copy();
        copy.dependencies = copy.dependencies.matching(matcher);
        return copy;
    }

    @Override
    public D remove(int index)
    {
        return unsupported();
    }

    @Override
    public boolean remove(Object that)
    {
        unsupported();
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> that)
    {
        unsupported();
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> that)
    {
        unsupported();
        return false;
    }

    @Override
    public D set(int index, D element)
    {
        return unsupported();
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
    @SuppressWarnings("unchecked")
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public L sorted()
    {
        var sorted = (L) dependencies(dependencies.sorted());
        return newList().with(sorted);
    }

    @NotNull
    @Override
    public List<D> subList(int fromIndex, int toIndex)
    {
        return unsupported();
    }

    @NotNull
    @Override
    public Object[] toArray()
    {
        return new Object[0];
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a)
    {
        return unsupported();
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
    @SuppressWarnings("unchecked")
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public L with(D first, D... rest)
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
    public L with(D inclusion)
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
    public L with(D[] inclusions)
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
    public L with(L inclusions)
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
    public L without(Collection<D> exclusions)
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
    public L without(D exclusion)
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
    public L without(L exclusions)
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
    public L without(Matcher<D> exclusionPattern)
    {
        var copy = copy();
        copy.dependencies.removeIf(exclusionPattern::matches);
        return copy;
    }

    @SuppressWarnings("unchecked")
    protected L newList(L that)
    {
        return (L) new DependencyList<>(that);
    }

    @SuppressWarnings("unchecked")
    protected L newList()
    {
        return (L) new DependencyList<>();
    }
}
