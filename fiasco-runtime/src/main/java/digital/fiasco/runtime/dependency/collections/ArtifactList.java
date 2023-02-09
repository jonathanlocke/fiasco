package digital.fiasco.runtime.dependency.collections;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * A list of {@link Artifact}s ({@link Library}s or {@link Asset}s).
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #artifacts(Artifact[])}</li>
 *     <li>{@link #artifacts(Collection)}</li>
 * </ul>
 *
 * <p><b>Matching</b></p>
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
 *     <li>{@link #sorted()}</li>
 *     <li>{@link #join(String)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asDescriptors()}</li>
 *     <li>{@link #asAssetList()}</li>
 *     <li>{@link #asLibraryList()}</li>
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
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
@SuppressWarnings("rawtypes")
public class ArtifactList extends BaseDependencyList<Artifact, ArtifactList>
{
    /**
     * Creates a list of artifacts
     *
     * @param artifacts The artifacts to add
     * @return The artifact list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static ArtifactList artifacts(Collection<Artifact> artifacts)
    {
        return new ArtifactList(artifacts);
    }

    /**
     * Creates a list of dependencies
     *
     * @param artifacts The dependencies to add
     * @return The artifact list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static ArtifactList artifacts(Artifact... artifacts)
    {
        return artifacts(list(artifacts));
    }

    public ArtifactList()
    {
    }

    protected ArtifactList(ArtifactList that)
    {
        super(that);
    }

    protected ArtifactList(Collection<Artifact> artifacts)
    {
        super(artifacts);
    }

    @Override
    protected ArtifactList newList()
    {
        return new ArtifactList();
    }

    @Override
    protected ArtifactList newList(ArtifactList that)
    {
        return new ArtifactList(that);
    }
}
