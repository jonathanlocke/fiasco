package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.DependencyList;

import java.util.Collection;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

@SuppressWarnings("rawtypes")
public class ArtifactList extends DependencyList<Artifact>
{
    /**
     * Creates a list of artifacts
     *
     * @param artifacts The artifacts to add
     * @return The dependency list
     */
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

    @Override
    public ArtifactList copy()
    {
        return new ArtifactList(this);
    }

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

    @Override
    public ArtifactList with(DependencyList<Artifact> dependencies)
    {
        return (ArtifactList) super.with(dependencies);
    }

    @Override
    public ArtifactList with(Artifact value)
    {
        return (ArtifactList) super.with(value);
    }

    @Override
    public ArtifactList with(Artifact first, Artifact... dependencies)
    {
        return (ArtifactList) super.with(first, dependencies);
    }

    @Override
    public ArtifactList with(Artifact[] value)
    {
        return (ArtifactList) super.with(value);
    }

    @Override
    public ArtifactList without(Matcher<Artifact> pattern)
    {
        return (ArtifactList) super.without(pattern);
    }

    @Override
    public ArtifactList without(Collection<Artifact> exclusions)
    {
        return (ArtifactList) super.without(exclusions);
    }
}
