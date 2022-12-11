package digital.fiasco.runtime.repository.artifact;

import com.telenav.kivakit.interfaces.comparison.Matcher;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public class ArtifactPattern implements Matcher<ArtifactDescriptor>
{
    @Override
    public boolean matches(ArtifactDescriptor artifact)
    {
        return false;
    }
}
