package digital.fiasco.runtime.repository.artifact;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.identifier.StringIdentifier;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.naming.Named;

import java.util.regex.Pattern;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Artifact implements Named
{
    private static final Pattern pattern = Pattern.compile("(?<group>[A-Za-z.]+)"
            + ":"
            + "(?<identifier>[A-Za-z-.]+)"
            + "(:"
            + "(?<version>\\d+\\.\\d+(\\.\\d+)?(-(snapshot|alpha|beta|rc|final))?)"
            + ")?");

    public static Artifact parseArtifact(Listener listener, String descriptor)
    {
        var matcher = pattern.matcher(descriptor);
        if (matcher.matches())
        {
            var group = new Group(matcher.group("group"));
            var identifier = new Identifier(matcher.group("identifier"));
            var version = Version.parseVersion(matcher.group("version"));
            return new Artifact(group, identifier, version);
        }
        listener.problem("Unable to parse artifact descriptor: $", descriptor);
        return null;
    }

    public static class Identifier extends StringIdentifier
    {
        public Identifier(String identifier)
        {
            super(identifier);
        }
    }

    private Group group;

    private Identifier identifier;

    private Version version;

    public Artifact(Group group, Identifier identifier)
    {
        this.group = group;
        this.identifier = identifier;
        version = null;
    }

    public Artifact(Group group, Identifier identifier, Version version)
    {
        this.group = group;
        this.identifier = identifier;
        this.version = version;
    }

    public Artifact(Artifact that)
    {
        group = that.group;
        identifier = that.identifier;
        version = that.version;
    }

    public Group group()
    {
        return group;
    }

    public Identifier identifier()
    {
        return identifier;
    }

    @Override
    public String name()
    {
        return group + ":" + identifier + (version == null ? "" : ":" + version);
    }

    @Override
    public String toString()
    {
        return name();
    }

    public Version version()
    {
        return version;
    }

    public Artifact withGroup(Group group)
    {
        var copy = new Artifact(this);
        copy.group = group;
        return copy;
    }

    public Artifact withIdentifier(Identifier group)
    {
        var copy = new Artifact(this);
        copy.identifier = identifier;
        return copy;
    }

    public Artifact withVersion(Version version)
    {
        var copy = new Artifact(this);
        copy.version = version;
        return copy;
    }
}
