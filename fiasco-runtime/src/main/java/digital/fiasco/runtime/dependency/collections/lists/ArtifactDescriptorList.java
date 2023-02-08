package digital.fiasco.runtime.dependency.collections.lists;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Countable;
import com.telenav.kivakit.interfaces.collection.ReadOnlyList;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.object.Copyable;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static digital.fiasco.runtime.dependency.collections.lists.ArtifactList.artifacts;

/**
 * A list of {@link ArtifactDescriptor}s.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #descriptors(String...)}</li>
 *     <li>{@link #descriptors(Collection)}</li>
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
 *     <li>{@link #asArtifacts()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #with(ArtifactDescriptor)}</li>
 *     <li>{@link #with(ArtifactDescriptor, ArtifactDescriptor[])}</li>
 *     <li>{@link #with(ArtifactDescriptor[])}</li>
 *     <li>{@link #without(Matcher)}</li>
 *     <li>{@link #without(Collection)}</li>
 *     <li>{@link #without(ArtifactDescriptor)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see Artifact
 * @see Library
 * @see Asset
 * @see BaseDependencyList
 */
public class ArtifactDescriptorList implements
    ReadOnlyList<ArtifactDescriptor>,
    Sized,
    Countable,
    Copyable<ArtifactDescriptorList>
{
    /**
     * Returns a list of artifact descriptors for the given strings. The text format for an artifact descriptor is
     * <b>[type]:[group]:[artifact]:[version]</b>, for example "library:com.telenav.kivakit:kivakit-core:1.8.0".
     *
     * @param descriptors The descriptor strings
     * @return The new artifact descriptor list
     * @throws RuntimeException Throws a subclass of {@link RuntimeException} if parsing fails
     */
    public static ArtifactDescriptorList descriptors(String... descriptors)
    {
        return descriptors(stringList(descriptors).map(ArtifactDescriptor::descriptor));
    }

    /**
     * Returns a list of artifact descriptors
     *
     * @param descriptors The descriptors
     * @return The new artifact descriptor list
     */
    public static ArtifactDescriptorList descriptors(Collection<ArtifactDescriptor> descriptors)
    {
        return new ArtifactDescriptorList(descriptors);
    }

    /** The artifact descriptors in this list */
    private final ObjectList<ArtifactDescriptor> descriptors;

    private ArtifactDescriptorList(Collection<ArtifactDescriptor> descriptors)
    {
        this.descriptors = list(descriptors);
    }

    private ArtifactDescriptorList(ArtifactDescriptorList that)
    {
        this.descriptors = that.descriptors.copy();
    }

    /**
     * Returns an artifact list for the given descriptors
     *
     * @return The artifact list
     */
    public ArtifactList asArtifacts()
    {
        return artifacts(descriptors.map(ArtifactDescriptor::asArtifact));
    }

    @Override
    public boolean contains(Object object)
    {
        return descriptors.contains(object);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> collection)
    {
        return descriptors.containsAll(collection);
    }

    @Override
    public ArtifactDescriptorList copy()
    {
        return new ArtifactDescriptorList(this);
    }

    @Override
    public Count count()
    {
        return Count.count(size());
    }

    public ArtifactDescriptor first()
    {
        return descriptors.first();
    }

    @Override
    public ArtifactDescriptor get(int index)
    {
        return descriptors.get(index);
    }

    @Override
    public int indexOf(Object object)
    {
        return descriptors.indexOf(object);
    }

    @Override
    public boolean isEmpty()
    {
        return descriptors.isEmpty();
    }

    @NotNull
    @Override
    public Iterator<ArtifactDescriptor> iterator()
    {
        return descriptors.iterator();
    }

    public String join(String separator)
    {
        return descriptors.join(separator);
    }

    @Override
    public int lastIndexOf(Object object)
    {
        return descriptors.lastIndexOf(object);
    }

    @NotNull
    @Override
    public ListIterator<ArtifactDescriptor> listIterator()
    {
        return descriptors.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<ArtifactDescriptor> listIterator(int index)
    {
        return descriptors.listIterator(index);
    }

    public ArtifactDescriptorList matching(ArtifactDescriptor descriptor)
    {
        var matches = descriptors();
        for (var at : this)
        {
            if (descriptor.matches(at))
            {
                matches = matches.with(at);
            }
        }
        return matches;
    }

    @Override
    public int size()
    {
        return descriptors.size();
    }

    public ArtifactDescriptorList sorted()
    {
        return descriptors(descriptors.sorted());
    }

    @NotNull
    @Override
    public Object[] toArray()
    {
        return descriptors.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] array)
    {
        return descriptors.toArray(array);
    }

    /**
     * Returns a copy of this list with the given descriptors added
     *
     * @param first The first dependency to include
     * @param rest The descriptors to include
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptorList with(ArtifactDescriptor first, ArtifactDescriptor... rest)
    {
        return mutatedCopy(it ->
        {
            it.descriptors.add(first);
            it.descriptors.addAll(rest);
        });
    }

    /**
     * Returns a copy of this list with the given desciptor added
     *
     * @param inclusion The dependency to included
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptorList with(ArtifactDescriptor inclusion)
    {
        return mutatedCopy(it -> it.descriptors.add(inclusion));
    }

    /**
     * Returns a copy of this list with the given descriptors added
     *
     * @param inclusions The descriptors to included
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptorList with(ArtifactDescriptor[] inclusions)
    {
        return mutatedCopy(it -> it.descriptors.addAll(inclusions));
    }

    /**
     * Returns a copy of this list with the given descriptors added
     *
     * @param inclusions The descriptors to included
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptorList with(ArtifactDescriptorList inclusions)
    {
        return mutatedCopy(it -> it.descriptors.addAll(inclusions));
    }

    /**
     * Returns a copy of this list with the given descriptors
     *
     * @param inclusions The descriptors to include
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptorList with(Collection<ArtifactDescriptor> inclusions)
    {
        return mutatedCopy(it -> it.descriptors.addAll(inclusions));
    }

    /**
     * Returns a copy of this list without the given descriptors
     *
     * @param exclusions The descriptors to exclude
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptorList without(Collection<ArtifactDescriptor> exclusions)
    {
        return mutatedCopy(it -> it.descriptors.removeAll(exclusions));
    }

    /**
     * Returns a copy of this list without the given dependency
     *
     * @param exclusion The descriptors to exclude
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptorList without(ArtifactDescriptor exclusion)
    {
        return mutatedCopy(it -> it.descriptors.remove(exclusion));
    }

    /**
     * Returns a copy of this list without the given descriptors
     *
     * @param exclusions The descriptors to exclude
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptorList without(ArtifactDescriptorList exclusions)
    {
        return mutatedCopy(it -> it.descriptors.removeAll(exclusions));
    }

    /**
     * Returns a copy of this dependency list without all descriptors matching the given pattern
     *
     * @param exclusionPattern The pattern to match
     * @return The new list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptorList without(Matcher<ArtifactDescriptor> exclusionPattern)
    {
        return mutatedCopy(it -> it.descriptors.removeIf(exclusionPattern::matches));
    }
}
