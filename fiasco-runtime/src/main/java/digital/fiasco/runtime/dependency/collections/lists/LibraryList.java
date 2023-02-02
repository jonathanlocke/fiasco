package digital.fiasco.runtime.dependency.collections.lists;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;

/**
 * A list of {@link Library} objects.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #libraries(Library[])}</li>
 *     <li>{@link #libraries(Collection)}</li>
 *     <li>{@link #libraries(ArtifactDescriptor...)}</li>
 *     <li>{@link #libraries(String...)}</li>
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
 *     <li>{@link #asArtifactDescriptors()}</li>
 *     <li>{@link #asStringList()}</li>
 *     <li>{@link #asMutableList()}</li>
 *     <li>{@link #asMutableSet()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #with(Library)}</li>
 *     <li>{@link #with(Library, Library[])}</li>
 *     <li>{@link #with(Library[])}</li>
 *     <li>{@link #without(Matcher)}</li>
 *     <li>{@link #without(Collection)}</li>
 *     <li>{@link #without(Library)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see Artifact
 * @see Library
 * @see Asset
 * @see BaseDependencyList
 */
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class LibraryList extends BaseDependencyList<Library, LibraryList>
{
    /**
     * Creates a list of libraries
     *
     * @param libraries The libraries to add
     * @return The list of libraries
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static LibraryList libraries(Collection<Library> libraries)
    {
        return new LibraryList(libraries);
    }

    /**
     * Creates a list of libraries
     *
     * @param libraries The libraries to add
     * @return The list of libraries
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static LibraryList libraries(Library... libraries)
    {
        return new LibraryList(list(libraries));
    }

    /**
     * Creates a list of libraries
     *
     * @param libraries The libraries to add
     * @return The list of libraries
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static LibraryList libraries(Iterable<ArtifactDescriptor> libraries)
    {
        return new LibraryList(list(libraries).map(Library::library));
    }

    /**
     * Creates a list of libraries
     *
     * @param libraries The libraries to add
     * @return The list of libraries
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static LibraryList libraries(ArtifactDescriptor... libraries)
    {
        return new LibraryList(list(libraries).map(Library::library));
    }

    /**
     * Creates a list of libraries from the given variable-argument list of descriptors
     *
     * @param descriptors The library descriptors
     * @return The list of libraries
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static LibraryList libraries(String... descriptors)
    {
        var libraries = stringList(descriptors).map(Library::library);
        return libraries(libraries.asArray(Library.class));
    }

    public LibraryList()
    {
    }

    protected LibraryList(LibraryList that)
    {
        super(that);
    }

    protected LibraryList(Collection<Library> libraries)
    {
        super(libraries);
    }

    @Override
    protected LibraryList newList()
    {
        return new LibraryList();
    }

    @Override
    protected LibraryList newList(LibraryList that)
    {
        return new LibraryList(that);
    }
}
