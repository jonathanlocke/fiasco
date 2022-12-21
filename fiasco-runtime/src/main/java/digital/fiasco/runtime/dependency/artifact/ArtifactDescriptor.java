package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.naming.Named;

import java.util.regex.Pattern;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.version.Version.Strictness.LENIENT;

/**
 * An identifier that uniquely identifies an artifact, including its group, artifact identifier and version.
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public record ArtifactDescriptor(ArtifactGroup group,
                                 ArtifactIdentifier identifier,
                                 Version version) implements Named
{
    private static final Pattern pattern = Pattern.compile("(?<group>[A-Za-z0-9._-]+)"
            + ":"
            + "(?<identifier>[A-Za-z0-9._-]+)"
            + "(:"
            + "(?<version>[A-Za-z0-9._-]+)"
            + ")?");

    public static ArtifactDescriptor artifactDescriptor(String descriptor)
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
            var version = Version.version(matcher.group("version"), LENIENT);
            return new ArtifactDescriptor(group, identifier, version);
        }
        listener.problem("Unable to parse artifact descriptor: $", descriptor);
        return null;
    }

    /**
     * Returns true if this descriptor is complete, having a group, identifier and version.
     *
     * @return True if this descriptor is valid
     */
    public boolean isValid()
    {
        return group != null && identifier != null && version != null;
    }

    /**
     * Returns the name of this descriptor as [group]:[identifier]:[version]
     */
    @Override
    public String name()
    {
        return group
                + (identifier == null ? "" : ":" + identifier)
                + (version == null ? "" : ":" + version);
    }

    @Override
    public String toString()
    {
        return name();
    }

    /**
     * Returns a copy of this descriptor with the given group
     *
     * @param group The group
     * @return The copy
     */
    public ArtifactDescriptor withGroup(ArtifactGroup group)
    {
        return new ArtifactDescriptor(group, identifier, version);
    }

    /**
     * Returns a copy of this descriptor with the given identifier
     *
     * @param identifier The identifier
     * @return The copy
     */
    public ArtifactDescriptor withIdentifier(ArtifactIdentifier identifier)
    {
        return new ArtifactDescriptor(group, identifier, version);
    }

    /**
     * Returns a copy of this descriptor with the given identifier
     *
     * @param identifier The identifier
     * @return The copy
     */
    public ArtifactDescriptor withIdentifier(String identifier)
    {
        return withIdentifier(new ArtifactIdentifier(identifier));
    }

    /**
     * Returns a copy of this descriptor with the given version
     *
     * @param version The version
     * @return The descriptor
     */
    public ArtifactDescriptor withVersion(Version version)
    {
        return new ArtifactDescriptor(group, identifier, version);
    }
}
