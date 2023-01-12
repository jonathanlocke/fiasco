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
 *     <li>{@link #descriptor(String)} - Returns the given descriptor, or throws an exception</li>
 *     <li>{@link #parseDescriptor(Listener, String)} - Parses the given descriptor, broadcasting a problem if parsing fails</li>
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
 * <p><b>Matching</b></p>
 *
 * <p>
 * Both the artifact and version are optional, and can be omitted (but the colons cannot). When either the artifact or
 * version is omitted (or both), the missing values act as wildcards when {@link #matches(ArtifactDescriptor)} is
 * called. For example, "com.telenav.kivakit:kivakit-core:" matches all versions of kivakit-core, the descriptor
 * "com.telenav.kivakit::" matches all versions of all kivakit artifacts, and the descriptor "com.telenav.kivakit::1.8"
 * matches all kivakit 1.8.x artifact descriptors.
 * </p>
 *
 * <ul>
 *     <li>{@link #matches(ArtifactDescriptor)} - Returns true if the given descriptor matches this one, allowing for wildcard
 *     matching if the artifact or version values are missing from either descriptor</li>
 * </ul>
 *
 * <p><b>Validity</b></p>
 *
 * <ul>
 *     <li>{@link #isComplete()} - Returns true if this descriptor fully describes an artifact, having a group, identifier, and version.</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withGroup(String)}</li>
 *     <li>{@link #withGroup(ArtifactGroup)}</li>
 *     <li>{@link #withArtifact(String)}</li>
 *     <li>{@link #withArtifact(ArtifactIdentifier)}</li>
 *     <li>{@link #withVersion(String)}</li>
 *     <li>{@link #withVersion(Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public record ArtifactDescriptor(ArtifactGroup group,
                                 ArtifactIdentifier artifact,
                                 Version version) implements
    Named
{
    /**
     * A lenient pattern for matching artifact descriptors.
     */
    private static final Pattern DESCRIPTOR_PATTERN = Pattern.compile("(?<group>[A-Za-z0-9._-]+)"
        + ":(?<artifact>[A-Za-z0-9._-]+)?"
        + ":(?<version>[A-Za-z0-9._-]+)?");

    /**
     * Returns the artifact descriptor for the given text
     *
     * @param text The descriptor text
     * @return The new artifact descriptor
     * @throws RuntimeException Throws a subclass of {@link RuntimeException} if parsing fails
     */
    public static ArtifactDescriptor descriptor(String text)
    {
        return parseDescriptor(throwingListener(), text);
    }

    /**
     * Parses the given artifact desriptor
     *
     * @param listener The listener to call with any problems
     * @param text The descriptor text
     * @return The artifact descriptor, or null if parsing fails
     */
    public static ArtifactDescriptor parseDescriptor(Listener listener, String text)
    {
        var matcher = DESCRIPTOR_PATTERN.matcher(text);
        if (matcher.matches())
        {
            var group = matcher.group("group");
            var artifact = matcher.group("artifact");
            var version = matcher.group("version");
            ensureNotNull(group);
            return new ArtifactDescriptor(new ArtifactGroup(group),
                artifact != null
                    ? ArtifactIdentifier.artifact(artifact)
                    : null,
                version != null
                    ? Version.version(version, LENIENT)
                    : null);
        }
        listener.problem("Unable to parse artifact descriptor: $", text);
        return null;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof ArtifactDescriptor that)
        {
            return group.equals(that.group)
                && (artifact == null || that.artifact == null || artifact.equals(that.artifact))
                && (version == null || that.version == null || version.equals(that.version));
        }
        return false;
    }

    /**
     * Returns true if this descriptor is complete, having a group, identifier and version.
     *
     * @return True if this descriptor is valid
     */
    public boolean isComplete()
    {
        return group != null && artifact != null && version != null;
    }

    /**
     * Returns true if this descriptor matches the given descriptor. If either descriptor is missing the artifact
     * identifier, artifact identifiers are considered to match like a wildcard. The same goes for versions (which also
     * do their own wildcard matches if the minor or patch version is omitted). For example,
     * "com.telenav.kivakit:kivakit-core" will match "com.telenav.kivakit:kivakit-core:1.8.0". Similarly,
     * "com.telenav.kivakit::1.8" will match "com.telenav.kivakit:kivakit-converter:1.8.7".
     *
     * @param that The version to match against
     * @return True if this version matches the given version
     */
    public boolean matches(ArtifactDescriptor that)
    {
        return group.equals(that.group)
            && (artifact == null || that.artifact == null || artifact.equals(that.artifact))
            && (version == null || that.version == null || version.equals(that.version));
    }

    /**
     * Returns the name of this descriptor as [group]:[identifier]:[version]
     */
    @Override
    public String name()
    {
        return group
            + ":"
            + (artifact == null ? "" : artifact)
            + ":"
            + (version == null ? "" : version);
    }

    @Override
    public String toString()
    {
        return name();
    }

    /**
     * Returns a copy of this descriptor with the given version
     *
     * @param version The version
     * @return The descriptor
     */
    public ArtifactDescriptor version(String version)
    {
        return withVersion(Version.version(version));
    }

    /**
     * Returns a copy of this descriptor with the given version
     *
     * @param version The version
     * @return The descriptor
     */
    public ArtifactDescriptor version(Version version)
    {
        return new ArtifactDescriptor(group, artifact, version);
    }

    /**
     * Returns a copy of this descriptor with the given identifier
     *
     * @param artifact The identifier
     * @return The copy
     */
    public ArtifactDescriptor withArtifact(ArtifactIdentifier artifact)
    {
        return new ArtifactDescriptor(group, artifact, version);
    }

    /**
     * Returns a copy of this descriptor with the given identifier
     *
     * @param artifact The identifier
     * @return The copy
     */
    public ArtifactDescriptor withArtifact(String artifact)
    {
        return withArtifact(ArtifactIdentifier.artifact(artifact));
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

    /**
     * Returns a copy of this descriptor without any version
     */
    public ArtifactDescriptor withoutArtifact()
    {
        return descriptor(group.name() + "::")
            .withVersion(version);
    }

    /**
     * Returns a copy of this descriptor without any version
     */
    public ArtifactDescriptor withoutVersion()
    {
        return group.artifact(artifact);
    }
}
