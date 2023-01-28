package digital.fiasco.runtime.dependency.artifact.lists;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.artifacts.Asset;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;

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
 *     <li>{@link #with(Artifact)}</li>
 *     <li>{@link #with(Artifact, Artifact[])}</li>
 *     <li>{@link #with(Artifact[])}</li>
 *     <li>{@link #without(Matcher)}</li>
 *     <li>{@link #without(Collection)}</li>
 *     <li>{@link #without(Artifact)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see Artifact
 * @see Library
 * @see Asset
 * @see DependencyList
 */
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
@SuppressWarnings("rawtypes")
public class ArtifactList extends DependencyList<Artifact, ArtifactList>
{
    /**
     * Creates a list of artifacts
     *
     * @param artifacts The artifacts to add
     * @return The dependency list
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
     * @return The dependency list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static ArtifactList artifacts(Artifact... artifacts)
    {
        return new ArtifactList(list(artifacts));
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

    public String toJson()
    {
        var artifacts = stringList();
        for (var at : sorted())
        {
            artifacts.add(at.toJson());
        }
        return artifacts.join("\n");
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
