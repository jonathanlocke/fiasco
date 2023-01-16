package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.naming.Named;

import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.version.Version.Strictness.LENIENT;

/**
 * A string that uniquely identifies an artifact, including its group, artifact name and version. Artifact descriptors
 * take the text form [group:artifact:version].
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
 *     <li>{@link #name()} - The full name of this descriptor as [group:artifact:version]</li>
 *     <li>{@link #group()} - The artifact group, like <i>com.telenav.kivakit</i></li>
 *     <li>{@link #artifact()} - The artifact name, like <i>kivakit-application</i></li>
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
 *     matching if the artifact or version values are missing from this descriptor</li>
 * </ul>
 *
 * <p><b>Validity</b></p>
 *
 * <ul>
 *     <li>{@link #isComplete()} - Returns true if this descriptor fully describes an artifact, having a group, name, and version.</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withGroup(String)}</li>
 *     <li>{@link #withGroup(ArtifactGroup)}</li>
 *     <li>{@link #withArtifact(String)}</li>
 *     <li>{@link #withArtifact(ArtifactName)}</li>
 *     <li>{@link #withVersion(String)}</li>
 *     <li>{@link #withVersion(Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see ArtifactGroup
 * @see ArtifactName
 * @see Version
 */
@SuppressWarnings("unused")
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public record ArtifactDescriptor(ArtifactGroup group,
                                 ArtifactName artifact,
                                 Version version) implements Named
{

    /**
     * A lenient pattern for matching artifact descriptors.
     */
    private static final Pattern DESCRIPTOR_PATTERN = Pattern.compile("(?<group>[A-Za-z0-9._-]+)"
        + ":(?<artifact>[A-Za-z0-9._-]+)?"
        + ":(?<version>[A-Za-z0-9._-]+)?");

    /**
     * Returns an artifact descriptor for the given text. The text format for an artifact descriptor is
     * [group:artifact:version], for example "com.telenav.kivakit:kivakit-core:1.8.0". The group is always required, but
     * the artifact name and version are both optional. For example, "com.telenav.kivakit:kivakit-core:" or
     * "com.telenav.kivakit::". If the artifact name or version (or both) is missing, that portion of the descriptor
     * functions like a wildcard, and can take on any value in calls to {@link #matches(ArtifactDescriptor)}.
     *
     * @param text The descriptor text
     * @return The new artifact descriptor
     * @throws RuntimeException Throws a subclass of {@link RuntimeException} if parsing fails
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static ArtifactDescriptor descriptor(String text)
    {
        return parseDescriptor(throwingListener(), text);
    }

    /**
     * Returns an artifact descriptor for the given text. The text format for an artifact descriptor is
     * [group:artifact:version], for example "com.telenav.kivakit:kivakit-core:1.8.0". The group is always required, but
     * the artifact name and version are both optional. For example, "com.telenav.kivakit:kivakit-core:" or
     * "com.telenav.kivakit::". If the artifact name or version (or both) is missing, that portion of the descriptor
     * functions like a wildcard, and can take on any value in calls to {@link #matches(ArtifactDescriptor)}.
     *
     * @param listener The listener to call with any problems
     * @param text The descriptor text
     * @return The artifact descriptor, or null if parsing fails
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
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
                    ? ArtifactName.artifact(artifact)
                    : null,
                version != null
                    ? Version.version(version, LENIENT)
                    : null);
        }
        listener.problem("Unable to parse artifact descriptor: $", text);
        return null;
    }

    public static class ArtifactDescriptorConverter extends BaseStringConverter<ArtifactDescriptor>
    {
        public ArtifactDescriptorConverter(Listener listener)
        {
            super(listener, ArtifactDescriptor.class);
        }

        @Override
        protected String onToString(ArtifactDescriptor descriptor)
        {
            return descriptor.name();
        }

        @Override
        protected ArtifactDescriptor onToValue(String text)
        {
            return descriptor(text);
        }
    }

    /**
     * Returns true if this descriptor is complete, having a group, artifact and version.
     *
     * @return True if this descriptor is a fully-specified descriptor
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean isComplete()
    {
        return group != null && artifact != null && version != null;
    }

    /**
     * Returns true if this descriptor matches the given descriptor (but not necessarily vice versa!). If this
     * descriptor is missing the artifact name, it is considered to match like a wildcard against that descriptor. The
     * same goes for versions (which also do their own wildcard matching if the minor or patch version is omitted). For
     * example, "com.telenav.kivakit:kivakit-core:" will match "com.telenav.kivakit:kivakit-core:1.8.0" (but
     * "com.telenav.kivakit-core:1.8.0" will not match against "com.telenav.kivakit-core:". Similarly,
     * "com.telenav.kivakit::1.8" will match "com.telenav.kivakit:kivakit-converter:1.8.7".
     *
     * @param that The version to match against
     * @return True if this version matches the given version
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean matches(ArtifactDescriptor that)
    {
        return group.equals(that.group)
            && (artifact == null || artifact.equals(that.artifact))
            && (version == null || version.equals(that.version));
    }

    /**
     * Returns the name of this descriptor as [group]:[artifact]:[version]. If the artifact or version is null, it will
     * be blank in the returned descriptor, as in "com.telenav.kivakit:kivakit:", "com.telenav.kivakit::1.0" or
     * "com.telenav.kivakit::".
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public String name()
    {
        return group
            + ":"
            + (artifact == null ? "" : artifact)
            + ":"
            + (version == null ? "" : version);
    }

    /**
     * Returns the fully-specified name of this descriptor (see {@link #name()}).
     */
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
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor version(String version)
    {
        return withVersion(version);
    }

    /**
     * Returns a copy of this descriptor with the given version
     *
     * @param version The version
     * @return The descriptor
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor version(Version version)
    {
        return new ArtifactDescriptor(group, artifact, version);
    }

    /**
     * Returns a copy of this descriptor with the given artifact name
     *
     * @param artifact The artifact name
     * @return The copy
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor withArtifact(ArtifactName artifact)
    {
        return new ArtifactDescriptor(group, artifact, version);
    }

    /**
     * Returns a copy of this descriptor with the given artifact name
     *
     * @param artifact The artifact name
     * @return The copy
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor withArtifact(String artifact)
    {
        return withArtifact(ArtifactName.artifact(artifact));
    }

    /**
     * Returns a copy of this descriptor with the given group
     *
     * @param group The group
     * @return The copy
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
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
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
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
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
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
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor withVersion(Version version)
    {
        return new ArtifactDescriptor(group, artifact, version);
    }

    /**
     * Returns a copy of this descriptor without any version
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor withoutArtifact()
    {
        return descriptor(group.name() + "::")
            .withVersion(version);
    }

    /**
     * Returns a copy of this descriptor without any version
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor withoutVersion()
    {
        return group.artifact(artifact);
    }
}
