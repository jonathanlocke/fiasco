package digital.fiasco.runtime.dependency.collections.lists;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.ReadOnlyList;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.object.Copyable;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.ListIterator;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static digital.fiasco.runtime.dependency.collections.lists.AssetList.assets;
import static digital.fiasco.runtime.dependency.collections.lists.LibraryList.libraries;

/**
 * A read-only list of {@link Dependency} objects.
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
 *     <li>{@link #contains(D)}</li>
 *     <li>{@link #containsAll(Collection)}</li>
 *     <li>{@link #containsAll(L)}</li>
 *     <li>{@link #count()}</li>
 *     <li>{@link #first()}</li>
 *     <li>{@link #first(Maximum)}</li>
 *     <li>{@link #get(int)}</li>
 *     <li>{@link #indexOf(Object)}</li>
 *     <li>{@link #isEmpty()}</li>
 *     <li>{@link #isNonEmpty()}</li>
 *     <li>{@link #iterator()}</li>
 *     <li>{@link #join(String)}</li>
 *     <li>{@link #last()}</li>
 *     <li>{@link #lastIndexOf(Object)}</li>
 *     <li>{@link #size()}</li>
 *     <li>{@link #sorted()}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asArtifactDescriptors()}</li>
 *     <li>{@link #asArtifactList()}</li>
 *     <li>{@link #asAssetList()}</li>
 *     <li>{@link #asDependencyList()}</li>
 *     <li>{@link #asLibraryList()}</li>
 *     <li>{@link #asMutableList()}</li>
 *     <li>{@link #asMutableSet()}</li>
 *     <li>{@link #asStringList()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #deduplicated()}</li>
 *     <li>{@link #with(BaseDependencyList)}</li>
 *     <li>{@link #with(Dependency)}</li>
 *     <li>{@link #with(Dependency, Dependency...)}</li>
 *     <li>{@link #without(Matcher)}</li>
 *     <li>{@link #without(Collection)}</li>
 * </ul>
 *
 * @param <D> Dependency subtype
 * @param <L> Dependency list subtype
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
@TypeQuality
    (
        documentation = DOCUMENTED,
        testing = TESTING_INSUFFICIENT,
        stability = STABILITY_UNDETERMINED
    )
public abstract class BaseDependencyList<D extends Dependency, L extends BaseDependencyList<D, L>> implements
    ReadOnlyList<D>,
    Iterable<D>,
    Sized,
    Copyable<L>
{
    /** The underlying dependencies */
    @FormatProperty
    ObjectList<D> dependencies = list();

    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTING_NOT_NEEDED)
    public BaseDependencyList()
    {
    }

    protected BaseDependencyList(L that)
    {
        this.dependencies = that.dependencies.copy();
    }

    protected BaseDependencyList(Collection<D> dependencies)
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
     * Returns the {@link Asset} artifacts in this list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public AssetList asAssetList()
    {
        return assets(matching(at -> at instanceof Asset).asArtifactDescriptors());
    }

    /**
     * Converts this list to a dependency list
     *
     * @return The dependency list
     */
    public DependencyList asDependencyList()
    {
        var dependencies = new DependencyList();
        for (var at : this)
        {
            dependencies = dependencies.with(at);
        }
        return dependencies;
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
    public ObjectList<D> asMutableList()
    {
        return dependencies.copy();
    }

    /**
     * Returns this dependency list as an object list
     *
     * @return The list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ObjectSet<D> asMutableSet()
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
    public boolean contains(D dependency)
    {
        return dependencies.contains(dependency);
    }

    @Override
    public boolean contains(Object that)
    {
        return dependencies.contains(that);
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
        return this.dependencies.containsAll(that);
    }

    /**
     * Returns true if this dependency list contains any dependency in the given collection
     *
     * @param that The collection to check
     * @return True if there is a dependency in this list that is also in the given list
     */
    public boolean containsAny(Collection<D> that)
    {
        return dependencies.containsAny(that);
    }

    /**
     * Returns an immutable copy of this dependency list.
     *
     * @return The copy
     */
    @Override
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

    public L deduplicated()
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
        if (object instanceof BaseDependencyList<?, ?> that)
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

    /**
     * {@inheritDoc}
     *
     * @param that {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int indexOf(Object that)
    {
        if (that instanceof Dependency dependency)
        {
            return dependencies.indexOf(dependency);
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     *
     * @param that {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object that)
    {
        if (that instanceof Dependency dependency)
        {
            return dependencies.lastIndexOf(dependency);
        }
        return -1;
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
     * Returns a list of dependencies of the given class
     *
     * @param type The type of dependency to match
     * @return The dependencies
     */
    @SuppressWarnings("unchecked")
    public DependencyList matching(Class<? extends Dependency> type)
    {
        return DependencyList.dependencies((ObjectList<Dependency>) dependencies.matching(at -> type.isAssignableFrom(at.getClass())));
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
        return mutatedCopy(it -> it.dependencies = dependencies.matching(matcher));
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
    public L sorted()
    {
        var sorted = (L) newList().with(dependencies.sorted());
        return newList().with(sorted);
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray()
    {
        unsupported();
        return null;
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] ignored)
    {
        return unsupported();
    }

    /**
     * Returns the JSON for this list
     *
     * @return JSON text
     */
    public String toJson()
    {
        var json = stringList();
        for (var at : sorted())
        {
            json.add(at.toJson());
        }
        return json.join("\n");
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
        return mutatedCopy(it ->
        {
            it.dependencies.add(first);
            it.dependencies.addAll(rest);
        });
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
        return mutatedCopy(it -> it.dependencies.add(inclusion));
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
        return mutatedCopy(it -> it.dependencies.addAll(inclusions));
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
        return mutatedCopy(it -> it.dependencies.addAll(inclusions));
    }

    /**
     * Returns a copy of this list with the given dependencies
     *
     * @param inclusions The dependencies to include
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public L with(Collection<D> inclusions)
    {
        return mutatedCopy(it -> it.dependencies.addAll(inclusions));
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
        return mutatedCopy(it -> it.dependencies.removeAll(exclusions));
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
        return mutatedCopy(it -> it.dependencies.remove(exclusion));
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
        return mutatedCopy(it -> it.dependencies.removeAll(exclusions));
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
        return mutatedCopy(it -> it.dependencies.removeIf(exclusionPattern::matches));
    }

    protected abstract L newList(L that);

    protected abstract L newList();
}
