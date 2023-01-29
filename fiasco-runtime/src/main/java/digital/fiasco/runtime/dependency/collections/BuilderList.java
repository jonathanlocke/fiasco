package digital.fiasco.runtime.dependency.collections;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * A list of {@link Artifact}s ({@link Library}s or {@link Asset}s).
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #builders(Builder[])}</li>
 *     <li>{@link #builders(Collection)}</li>
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
 *     <li>{@link #asList()}</li>
 *     <li>{@link #asSet()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #with(Builder)}</li>
 *     <li>{@link #with(Builder, Builder[])}</li>
 *     <li>{@link #with(Builder[])}</li>
 *     <li>{@link #without(Matcher)}</li>
 *     <li>{@link #without(Collection)}</li>
 *     <li>{@link #without(Builder)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see Artifact
 * @see Library
 * @see Asset
 * @see DependencyList
 */
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class BuilderList extends DependencyList<Builder, BuilderList>
{
    /**
     * Creates a list of artifacts
     *
     * @param builders The builders to add
     * @return The dependency list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static BuilderList builders(Collection<Builder> builders)
    {
        return new BuilderList(builders);
    }

    /**
     * Creates a list of dependencies
     *
     * @param builders The dependencies to add
     * @return The dependency list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static BuilderList builders(Builder... builders)
    {
        return new BuilderList(list(builders));
    }

    public BuilderList()
    {
    }

    protected BuilderList(BuilderList that)
    {
        super(that);
    }

    protected BuilderList(Collection<Builder> builders)
    {
        super(builders);
    }

    public String toJson()
    {
        return unsupported();
    }

    @Override
    protected BuilderList newList()
    {
        return new BuilderList();
    }

    @Override
    protected BuilderList newList(BuilderList that)
    {
        return new BuilderList(that);
    }
}
