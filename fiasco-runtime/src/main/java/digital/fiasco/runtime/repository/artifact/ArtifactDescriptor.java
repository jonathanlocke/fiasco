package digital.fiasco.runtime.repository.artifact;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.ResourcePath;

import java.util.regex.Pattern;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.resource.FileName.parseFileName;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;

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

    /**
     * Returns this descriptor as a filename with the given suffix.
     *
     * @param listener The listener to call if something goes wrong
     * @param suffix The suffix to add to the filename
     * @return The filename
     */
    public FileName asFileName(String suffix)
    {
        return parseFileName(throwingListener(), format("$-$-suffix", group(), version(), suffix));
    }

    /**
     * Returns this artifact descriptor as a Maven path (relative to some repository root) such as:
     * <pre>
     * com/telenav/kivakit/kivakit-application/1.9.0</pre>
     *
     * @return The resource path
     */
    public ResourcePath asMavenPath()
    {
        var groupPath = group.name().replaceAll("\\.", "/");
        return parseResourcePath(throwingListener(), groupPath + "/" + identifier() + "/" + version());
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

    public ArtifactDescriptor withVersion(Version version)
    {
        return new ArtifactDescriptor(group, identifier, version);
    }
}
