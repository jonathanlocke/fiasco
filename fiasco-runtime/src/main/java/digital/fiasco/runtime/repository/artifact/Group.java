package digital.fiasco.runtime.repository.artifact;

import com.telenav.kivakit.core.value.name.Name;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Group extends Name
{
    public Group(final String name)
    {
        super(name);
    }

    public Artifact artifact(final Artifact.Identifier identifier)
    {
        return new Artifact(this, identifier);
    }
}
