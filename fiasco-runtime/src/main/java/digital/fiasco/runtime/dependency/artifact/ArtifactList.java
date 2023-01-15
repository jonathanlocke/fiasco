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
 *     <li>{@link #artifactList(Artifact[])}</li>
 *     <li>{@link #artifactList(Collection)}</li>
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
 *     <li>{@link #with(Iterable)}</li>
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
@TypeQuality
    (
        documentation = DOCUMENTED,
        testing = TESTED,
        stability = STABLE
    )
@SuppressWarnings("rawtypes")
public class ArtifactList extends DependencyList<Artifact>
{
    /**
     * Creates a list of artifacts
     *
     * @param artifacts The artifacts to add
     * @return The dependency list
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    public static ArtifactList artifactList(Collection<Artifact> artifacts)
    {
        return new ArtifactList(artifacts);
    }

    /**
     * Creates a list of dependencies
     *
     * @param artifacts The dependencies to add
     * @return The dependency list
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    public static ArtifactList artifactList(Artifact... artifacts)
    {
        return new ArtifactList(list(artifacts));
    }

    public ArtifactList()
    {
    }

    public ArtifactList(ArtifactList that)
    {
        super(that);
    }

    public ArtifactList(Collection<Artifact> artifacts)
    {
        super(artifacts);
    }

    /**
     * Returns the {@link Asset} artifacts in this list
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    public ArtifactList assets()
    {
        return matching(at -> at instanceof Asset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    public ArtifactList copy()
    {
        return new ArtifactList(this);
    }

    /**
     * Returns the {@link Library} artifacts in this list
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    public ArtifactList libraries()
    {
        return matching(at -> at instanceof Library);
    }

    /**
     * Returns the artifacts in this list that match the given matcher
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    @Override
    public ArtifactList matching(Matcher<Artifact> matcher)
    {
        return (ArtifactList) super.matching(matcher);
    }

    @Override
    public ArtifactList with(Iterable<Artifact> dependencies)
    {
        return (ArtifactList) super.with(dependencies);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    @Override
    public ArtifactList with(DependencyList<Artifact> dependencies)
    {
        return (ArtifactList) super.with(dependencies);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    @Override
    public ArtifactList with(Artifact value)
    {
        return (ArtifactList) super.with(value);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    @Override
    public ArtifactList with(Artifact first, Artifact... dependencies)
    {
        return (ArtifactList) super.with(first, dependencies);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    @Override
    public ArtifactList with(Artifact[] value)
    {
        return (ArtifactList) super.with(value);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    @Override
    public ArtifactList without(Matcher<Artifact> pattern)
    {
        return (ArtifactList) super.without(pattern);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    @Override
    public ArtifactList without(DependencyList<Artifact> artifacts)
    {
        return (ArtifactList) super.without(artifacts);
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality
        (
            documentation = DOCUMENTED,
            testing = TESTED
        )
    @Override
    public ArtifactList without(Collection<Artifact> exclusions)
    {
        return (ArtifactList) super.without(exclusions);
    }
}
