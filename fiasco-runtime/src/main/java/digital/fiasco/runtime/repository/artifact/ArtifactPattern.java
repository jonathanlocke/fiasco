package digital.fiasco.runtime.repository.artifact;

import com.telenav.kivakit.interfaces.comparison.Matcher;

/**
 * @author jonathan
 */
public class ArtifactPattern implements Matcher<Artifact>
{
    @Override
    public boolean matches(final Artifact artifact)
    {
        return false;
    }
}
