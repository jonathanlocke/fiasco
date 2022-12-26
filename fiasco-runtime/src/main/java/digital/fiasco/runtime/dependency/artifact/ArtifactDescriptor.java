package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.naming.Named;

import java.util.regex.Pattern;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.version.Version.Strictness.LENIENT;

/**
 * An identifier that uniquely identifies an artifact, including its group, artifact identifier and version. Artifact
 * descriptors take the text form group:identifier:version.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #artifactDescriptor(String)} - Returns the given descriptor, or throws an exception</li>
 *     <li>{@link #parseArtifactDescriptor(Listener, String)} - Parses the given descriptor, broadcasting a problem if parsing fails</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #name()} - The full name of this descriptor (group:identifier:version)</li>
 *     <li>{@link #group()} - The artifact group, like <i>com.telenav.kivakit</i></li>
 *     <li>{@link #identifier()} - The artifact identifier, like <i>kivakit-application</i></li>
 *     <li>{@link #version()} - The artifact version, like <i>1.8.0</i></li>
 * </ul>
 *
 * <p><b>Validity</b></p>
 *
 * <ul>
 *     <li>{@link #isValid()} - Returns true if this descriptor fully describes an artifact, having a group, identifier, and version.</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withGroup(ArtifactGroup)}</li>
 *     <li>{@link #withIdentifier(String)}</li>
 *     <li>{@link #withIdentifier(ArtifactIdentifier)}</li>
 *     <li>{@link #withVersion(Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public record ArtifactDescriptor(ArtifactGroup group,
                                 ArtifactIdentifier identifier,
                                 Version version) implements Named
{
    /** A lenient pattern for artifact descriptors */
    private static final Pattern DESCRIPTOR_PATTERN = Pattern.compile("(?<group>[A-Za-z0-9._-]+)"
        + ":"
        + "(?<identifier>[A-Za-z0-9._-]+)"
        + "(:"
        + "(?<version>[A-Za-z0-9._-]+)"
        + ")?");

    /**
     * Returns the artifact descriptor for the given text
     *
     * @param text The descriptor text
     * @return The new artifact descriptor
     * @throws RuntimeException Throws a subclass of {@link RuntimeException} if parsing fails
     */
    public static ArtifactDescriptor artifactDescriptor(String text)
    {
        return parseArtifactDescriptor(throwingListener(), text);
    }

    /**
     * Parses the given artifact desriptor
     *
     * @param listener The listener to call with any problems
     * @param text The descriptor text
     * @return The artifact descriptor, or null if parsing fails
     */
    public static ArtifactDescriptor parseArtifactDescriptor(Listener listener, String text)
    {
        var matcher = DESCRIPTOR_PATTERN.matcher(text);
        if (matcher.matches())
        {
            var group = new ArtifactGroup(matcher.group("group"));
            var identifier = new ArtifactIdentifier(matcher.group("identifier"));
            var version = Version.version(matcher.group("version"), LENIENT);
            return new ArtifactDescriptor(group, identifier, version);
        }
        listener.problem("Unable to parse artifact descriptor: $", text);
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
