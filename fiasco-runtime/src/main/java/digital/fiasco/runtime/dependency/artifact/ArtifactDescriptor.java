package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.naming.Named;

import java.util.regex.Pattern;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.version.Version.Strictness.LENIENT;

/**
 * An identifier that uniquely identifies an artifact, including its group, artifact identifier and version. Artifact
 * descriptors take the text form <b>group:artifact:version</b>.
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
 *     <li>{@link #artifact()} - The artifact identifier, like <i>kivakit-application</i></li>
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
 *     <li>{@link #withGroup(String)}</li>
 *     <li>{@link #withGroup(ArtifactGroup)}</li>
 *     <li>{@link #withArtifactIdentifier(String)}</li>
 *     <li>{@link #withArtifactIdentifier(ArtifactIdentifier)}</li>
 *     <li>{@link #withVersion(String)}</li>
 *     <li>{@link #withVersion(Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public record ArtifactDescriptor(ArtifactGroup group,
                                 ArtifactIdentifier artifact,
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
            var group = matcher.group("group");
            var identifier = matcher.group("identifier");
            var version = matcher.group("version");
            ensureNotNull(group);
            return new ArtifactDescriptor(new ArtifactGroup(group),
                identifier != null
                    ? new ArtifactIdentifier(identifier)
                    : null,
                version != null
                    ? Version.version(version, LENIENT)
                    : null);
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
        return group != null && artifact != null && version != null;
    }

    /**
     * Returns the name of this descriptor as [group]:[identifier]:[version]
     */
    @Override
    public String name()
    {
        return group
            + (artifact == null ? "" : ":" + artifact)
            + (version == null ? "" : ":" + version);
    }

    @Override
    public String toString()
    {
        return name();
    }

    /**
     * Returns a copy of this descriptor with the given identifier
     *
     * @param artifact The identifier
     * @return The copy
     */
    public ArtifactDescriptor withArtifactIdentifier(ArtifactIdentifier artifact)
    {
        return new ArtifactDescriptor(group, artifact, version);
    }

    /**
     * Returns a copy of this descriptor with the given identifier
     *
     * @param artifact The identifier
     * @return The copy
     */
    public ArtifactDescriptor withArtifactIdentifier(String artifact)
    {
        return withArtifactIdentifier(new ArtifactIdentifier(artifact));
    }

    /**
     * Returns a copy of this descriptor with the given group
     *
     * @param group The group
     * @return The copy
     */
    public ArtifactDescriptor withGroup(ArtifactGroup group)
    {
        return new ArtifactDescriptor(group, artifact, version);
    }

    /**
     * Returns a copy of this descriptor with the given group
     *
     * @param group The group
     * @return The copy
     */
    public ArtifactDescriptor withGroup(String group)
    {
        return withGroup(ArtifactGroup.group(group));
    }

    /**
     * Returns a copy of this descriptor with the given version
     *
     * @param version The version
     * @return The descriptor
     */
    public ArtifactDescriptor withVersion(String version)
    {
        return withVersion(Version.version(version));
    }

    /**
     * Returns a copy of this descriptor with the given version
     *
     * @param version The version
     * @return The descriptor
     */
    public ArtifactDescriptor withVersion(Version version)
    {
        return new ArtifactDescriptor(group, artifact, version);
    }
}
