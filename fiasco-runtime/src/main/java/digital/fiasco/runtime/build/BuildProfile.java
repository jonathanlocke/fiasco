package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.value.identifier.StringIdentifier;

/**
 * Identifier for a profile which can be used to enable or disable tools based on command line options
 *
 * @author Jonathan Locke
 */
public class BuildProfile extends StringIdentifier
{
    public BuildProfile(String identifier)
    {
        super(identifier);
    }
}
