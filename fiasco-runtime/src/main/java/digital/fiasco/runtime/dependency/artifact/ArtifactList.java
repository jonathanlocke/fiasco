package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.DependencyList;

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
 *     <li>{@link #assets()}</li>
 *     <li>{@link #libraries()}</li>
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
 *     <li>{@link #with(DependencyList)}</li>
 *     <li>{@link #with(Artifact)}</li>
 *     <li>{@link #with(Artifact, Artifact[])}</li>
 *     <li>{@link #with(Artifact[])}</li>
 *     <li>{@link #without(Matcher)}</li>
 *     <li>{@link #without(Collection)}</li>
 *     <li>{@link #without(DependencyList)}</li>
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
public class ArtifactList extends DependencyList<Artifact>
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

    /**
     * Returns the {@link Asset} artifacts in this list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactList assets()
    {
        return matching(at -> at instanceof Asset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactList copy()
    {
        return new ArtifactList(this);
    }

    @Override
    public ArtifactList deduplicate()
    {
        var deduplicated = artifacts();
        for (var at : this)
        {
            if (!deduplicated.contains(at))
            {
                deduplicated = deduplicated.with(at);
            }
        }
        return deduplicated;
    }

    /**
     * Returns the {@link Library} artifacts in this list
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactList libraries()
    {
        return matching(at -> at instanceof Library);
    }

    /**
     * Returns the artifacts in this list that match the given matcher
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    @Override
    public ArtifactList matching(Matcher<Artifact> matcher)
    {
        return (ArtifactList) super.matching(matcher);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    @Override
    public ArtifactList with(DependencyList<Artifact> inclusions)
    {
        return (ArtifactList) super.with(inclusions);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    @Override
    public ArtifactList with(Artifact inclusion)
    {
        return (ArtifactList) super.with(inclusion);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    @Override
    public ArtifactList with(Artifact first, Artifact... rest)
    {
        return (ArtifactList) super.with(first, rest);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    @Override
    public ArtifactList with(Artifact[] inclusions)
    {
        return (ArtifactList) super.with(inclusions);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    @Override
    public ArtifactList without(Matcher<Artifact> exclusionPattern)
    {
        return (ArtifactList) super.without(exclusionPattern);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    @Override
    public ArtifactList without(DependencyList<Artifact> exclusions)
    {
        return (ArtifactList) super.without(exclusions);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    @Override
    public ArtifactList without(Collection<Artifact> exclusions)
    {
        return (ArtifactList) super.without(exclusions);
    }
}
