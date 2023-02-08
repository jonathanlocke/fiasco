package digital.fiasco.runtime.dependency.artifact.descriptor;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.naming.Named;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;

import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.version.Version.Strictness.LENIENT;
import static digital.fiasco.runtime.dependency.artifact.types.Asset.asset;
import static digital.fiasco.runtime.dependency.artifact.types.Library.library;

/**
 * A string that uniquely identifies an artifact, including its group, artifact name and version. Artifact descriptors
 * take the text form [type:group:artifact:version]. For example, "library:com.telenav.kivakit:kivakit-core:1.8.5"
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
 *     <li>{@link #name()} - The full name of this descriptor as <b>[type]:[group]:[artifact]:[version]</b></li>
 *     <li>{@link #group()} - The artifact group, like <i>com.telenav.kivakit</i></li>
 *     <li>{@link #artifactName()} - The artifact name, like <i>kivakit-application</i></li>
 *     <li>{@link #version()} - The artifact version, like <i>1.8.0</i></li>
 * </ul>
 *
 * <p><b>Matching</b></p>
 *
 * <p>
 * Both the artifact and version are optional, and can be omitted (but the colons cannot). When either the artifact or
 * version is omitted (or both), the missing values act as wildcards when {@link #matches(ArtifactDescriptor)} is
 * called. For example, "library:com.telenav.kivakit:kivakit-core:" matches all versions of the kivakit-core library, the descriptor
 * ":com.telenav.kivakit::" matches all versions of all kivakit artifacts, and the descriptor ":com.telenav.kivakit::1.8"
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
public record ArtifactDescriptor(Class<? extends Artifact<?>> type,
                                 ArtifactGroup group,
                                 ArtifactName artifactName,
                                 Version version) implements Named
{
    /**
     * A lenient pattern for matching artifact descriptors.
     */
    private static final Pattern DESCRIPTOR_PATTERN = Pattern.compile(
        "(?<type>[A-Za-z]+)?"
            + ":(?<group>[A-Za-z0-9._-]+)?"
            + ":(?<artifactName>[A-Za-z0-9._-]+)?"
            + ":(?<version>[A-Za-z0-9._-]+)?");

    private static final StringMap<Class<? extends Artifact<?>>> typeToArtifactClass = new StringMap<>();

    static
    {
        typeToArtifactClass.put("library", Library.class);
        typeToArtifactClass.put("asset", Asset.class);
    }

    /**
     * Returns a list of artifact descriptors for the given strings. The text format for an artifact descriptor is
     * <b>[type]:[group]:[artifact]:[version]</b>, for example "library:com.telenav.kivakit:kivakit-core:1.8.0".
     *
     * <p><b>Wildcard Matches</b></p>
     *
     * <p>
     * When fields of a descriptor are omitted, they can take on any value in calls to
     * {@link #matches(ArtifactDescriptor)}. For example:
     *
     * <ul>
     *     <li>"library:com.telenav.kivakit:kivakit-core:" matches all versions of the kivakit-core library</li>
     *     <li>"library:com.telenav.kivakit::" matches all kivakit libraries</li>
     *     <li>":com.telenav.kivakit::" matches all kivakit artifacts (both assets and libraries)</li>
     * </ul>
     *
     * @param value The descriptor
     * @return The new artifact descriptors
     * @throws RuntimeException Throws a subclass of {@link RuntimeException} if parsing fails
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static ArtifactDescriptor descriptor(String value)
    {
        return parseDescriptor(throwingListener(), value);
    }

    /**
     * Returns a list of artifact descriptors for the given strings. The text format for an artifact descriptor is
     * <b>[type]:[group]:[artifact]:[version]</b>, for example "library:com.telenav.kivakit:kivakit-core:1.8.0".
     *
     * <p><b>Wildcard Matches</b></p>
     *
     * <p>
     * When fields of a descriptor are omitted, they can take on any value in calls to
     * {@link #matches(ArtifactDescriptor)}. For example:
     *
     * <ul>
     *     <li>"library:com.telenav.kivakit:kivakit-core:" matches all versions of the kivakit-core library</li>
     *     <li>"library:com.telenav.kivakit::" matches all kivakit libraries</li>
     *     <li>":com.telenav.kivakit::" matches all kivakit artifacts (both assets and libraries)</li>
     * </ul>
     *
     * @param value The descriptor
     * @return The new artifact descriptors
     * @throws RuntimeException Throws a subclass of {@link RuntimeException} if parsing fails
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static ArtifactDescriptor parseDescriptor(Listener listener, String value)
    {
        var matcher = DESCRIPTOR_PATTERN.matcher(value);
        if (matcher.matches())
        {
            var type = matcher.group("type");
            var group = matcher.group("group");
            var artifactName = matcher.group("artifactName");
            var version = matcher.group("version");

            return new ArtifactDescriptor(
                type == null ? null : typeToArtifactClass.get(type),
                new ArtifactGroup(ensureNotNull(group)),
                artifactName != null
                    ? ArtifactName.artifactName(artifactName)
                    : null,
                version != null
                    ? Version.version(version, LENIENT)
                    : null);
        }
        listener.problem("Unable to parse artifact descriptor: $", value);
        return null;
    }

    public Artifact<?> asArtifact()
    {
        if (type() == Asset.class)
        {
            return asAsset();
        }
        if (type() == Library.class)
        {
            return asLibrary();
        }
        return illegalState("Descriptor does not describe an artifact");
    }

    public Asset asAsset()
    {
        return asset(this);
    }

    public Library asLibrary()
    {
        return library(this);
    }

    public String groupAndName()
    {
        return group() + ":" + artifactName();
    }

    public boolean hasArtifact()
    {
        return artifactName != null;
    }

    public boolean hasGroup()
    {
        return group != null;
    }

    public boolean hasType()
    {
        return type != null;
    }

    public boolean hasVersion()
    {
        return version != null;
    }

    /**
     * Returns true if this descriptor is complete, having a group, artifact and version.
     *
     * @return True if this descriptor is a fully-specified descriptor
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean isComplete()
    {
        return hasType() && hasGroup() && hasArtifact() && hasVersion();
    }

    /**
     * Returns true if this descriptor matches the given descriptor (but not necessarily vice versa!). If this
     * descriptor is missing the artifact name, it is considered to match like a wildcard against that descriptor. The
     * same goes for versions (which also do their own wildcard matching if the minor or patch version is omitted),
     * types and groups. For example, ":com.telenav.kivakit:kivakit-core:" will match
     * "library:com.telenav.kivakit:kivakit-core:1.8.0" (but "library:com.telenav.kivakit-core:1.8.0" will not match
     * against "library:com.telenav.kivakit-core:". Similarly, ":com.telenav.kivakit::1.8" will match
     * ":com.telenav.kivakit:kivakit-converter:1.8.7".
     *
     * @param that The version to match against
     * @return True if this version matches the given version
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean matches(ArtifactDescriptor that)
    {
        return (type == null || type.equals(that.type))
            && (group == null || group.equals(that.group))
            && (artifactName == null || artifactName.equals(that.artifactName))
            && (version == null || version.equals(that.version));
    }

    public String mavenName()
    {
        return group + ":" + artifactName + ":" + version;
    }

    /**
     * Returns the name of this descriptor as <b>[type]:[group]:[artifact]:[version]</b>. If the artifact or version is
     * null, it will be blank in the returned descriptor, as in "library:com.telenav.kivakit:kivakit:",
     * "library:com.telenav.kivakit::1.0" or ":com.telenav.kivakit::".
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public String name()
    {
        return type.getSimpleName().toLowerCase()
            + ":"
            + group
            + ":"
            + (artifactName == null ? "" : artifactName)
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
        return new ArtifactDescriptor(type, group, artifactName, version);
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
        return new ArtifactDescriptor(type, group, artifact, version);
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
        return withArtifact(ArtifactName.artifactName(artifact));
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
        return new ArtifactDescriptor(type, group, artifactName, version);
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
        return new ArtifactDescriptor(type, group, artifactName, version);
    }

    /**
     * Returns a copy of this descriptor without any version
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor withoutArtifact()
    {
        return new ArtifactDescriptor(type, group, null, version);
    }

    /**
     * Returns a copy of this descriptor without any version
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactDescriptor withoutVersion()
    {
        return group.artifact(type, artifactName);
    }
}
