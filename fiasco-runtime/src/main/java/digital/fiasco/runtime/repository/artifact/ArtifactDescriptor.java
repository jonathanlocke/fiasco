package digital.fiasco.runtime.repository.artifact;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.naming.Named;

import java.util.regex.Pattern;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * An identifier that uniquely identifies an artifact, including its group, artifact identifier and version.
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public record ArtifactDescriptor
        (
                ArtifactGroup group,
                ArtifactIdentifier identifier,
                Version version
        )
        implements Named
{
    private static final Pattern pattern = Pattern.compile("(?<group>[A-Za-z.]+)"
            + ":"
            + "(?<identifier>[A-Za-z-.]+)"
            + "(:"
            + "(?<version>\\d+\\.\\d+(\\.\\d+)?(-(snapshot|alpha|beta|rc|final))?)"
            + ")?");

    public static ArtifactDescriptor parseArtifactDescriptor(String descriptor)
    {
        return parseArtifactDescriptor(throwingListener(), descriptor);
    }

    public static ArtifactDescriptor parseArtifactDescriptor(Listener listener, String descriptor)
    {
        var matcher = pattern.matcher(descriptor);
        if (matcher.matches())
        {
            var group = new ArtifactGroup(matcher.group("group"));
            var identifier = new ArtifactIdentifier(matcher.group("identifier"));
            var version = Version.parseVersion(matcher.group("version"));
            return new ArtifactDescriptor(group, identifier, version);
        }
        listener.problem("Unable to parse artifact descriptor: $", descriptor);
        return null;
    }

    public boolean isValid()
    {
        return group != null && identifier != null && version != null;
    }

    @Override
    public String name()
    {
        return group
                + ":"
                + (identifier == null ? "" : ":" + identifier)
                + (version == null ? "" : ":" + version);
    }

    @Override
    public String toString()
    {
        return name();
    }

    public ArtifactDescriptor withGroup(ArtifactGroup group)
    {
        return new ArtifactDescriptor(group, identifier, version);
    }

    public ArtifactDescriptor withIdentifier(ArtifactIdentifier identifier)
    {
        return new ArtifactDescriptor(group, identifier, version);
    }

    public ArtifactDescriptor withIdentifier(String identifier)
    {
        return withIdentifier(new ArtifactIdentifier(identifier));
    }

    public ArtifactDescriptor withVersion(Version version)
    {
        return new ArtifactDescriptor(group, identifier, version);
    }
}
