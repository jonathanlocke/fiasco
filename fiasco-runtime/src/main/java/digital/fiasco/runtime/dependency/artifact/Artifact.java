package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.Repository;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.core.version.Version.parseVersion;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_TYPE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_VERSION;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.CONTENT_SUFFIX;

/**
 * Represents an artifact, either an {@link Asset} or a {@link Library}
 *
 * <p><b>Repository</b></p>
 *
 * <ul>
 *     <li>{@link #repository()}</li>
 * </ul>
 *
 * <p><b>Identity</b></p>
 *
 * <ul>
 *     <li>{@link #descriptor()}</li>
 *     <li>{@link #version(String)}</li>
 *     <li>{@link #version(Version)}</li>
 *     <li>{@link #withDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withArtifactIdentifier(String)}</li>
 *     <li>{@link #withVersion(Version)}</li>
 * </ul>
 *
 * <p><b>Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies()}</li>
 *     <li>{@link #excludes(ArtifactDescriptor)}</li>
 *     <li>{@link #withDependencies(DependencyList)}</li>
 *     <li>{@link #withoutDependencies(ArtifactDescriptor...)}</li>
 *     <li>{@link #withoutDependencies(String...)}</li>
 *     <li>{@link #withoutDependencies(Matcher)}</li>
 * </ul>
 *
 * <p><b>Attachments</b></p>
 *
 * <ul>
 *     <li>{@link #attachmentMap()}</li>
 *     <li>{@link #attachment(String)}</li>
 *     <li>{@link #withAttachment(ArtifactAttachment)}</li>
 * </ul>
 *
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #copy()} - Returns a copy of this artifact</li>
 *     <li>{@link #version(String)} - Returns this artifact with the given version</li>
 *     <li>{@link #version(Version)} - Returns this artifact with the given version</li>
 *     <li>{@link #withAttachment(ArtifactAttachment)} - Attaches the given content</li>
 *     <li>{@link #withContent(ArtifactContent)}</li>
 *     <li>{@link #withDependencies(DependencyList)} - Returns this artifact with the given dependencies</li>
 *     <li>{@link #withDescriptor(ArtifactDescriptor)} - Returns this artifact with the given descriptor</li>
 *     <li>{@link #withArtifactIdentifier(String)} - Returns this artifact with the given identifier</li>
 *     <li>{@link #withVersion(Version)} - Returns this artifact with the given version</li>
 *     <li>{@link #withoutDependencies(ArtifactDescriptor...)} - Returns this artifact without the given dependencies</li>
 *     <li>{@link #withoutDependencies(String...)} - Returns this artifact without the given dependencies</li>
 *     <li>{@link #withoutDependencies(Matcher)} - Returns this artifact without the given dependencies</li>
 * </ul>
 *
 *
 * <p><b>Maven</b></p>
 *
 * <ul>
 *     <li>{@link #mavenPom()}</li>
 * </ul>
 *
 * <p><b>Serialization</b></p>
 *
 * <ul>
 *     <li>{@link #artifactFromJson(String)}</li>
 *     <li>{@link #toJson()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface Artifact extends Dependency
{
    /**
     * Returns an artifact for a given JSON string
     *
     * @param json The JSON
     * @return The artifact
     */
    static Artifact artifactFromJson(String json)
    {
        var serialized = new StringResource(json);
        var serializer = new GsonObjectSerializer();
        return (Artifact) serializer.readObject(serialized, METADATA_OBJECT_TYPE, METADATA_OBJECT_VERSION).object();
    }

    /**
     * Returns the attached resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param suffix The artifact suffix
     * @return The attached resource
     */
    ArtifactAttachment attachment(String suffix);

    /**
     * Returns a map from descriptor to artifact attachment of all attached resources
     *
     * @return The attachments
     */
    ObjectMap<String, ArtifactAttachment> attachmentMap();

    default ObjectList<ArtifactAttachment> attachments()
    {
        return list(attachmentMap().values());
    }

    /**
     * Returns primary content attachment for this asset
     *
     * @return The content
     */
    default ArtifactContent content()
    {
        return attachment(CONTENT_SUFFIX).content();
    }

    /**
     * Returns a copy of this artifact
     *
     * @return The new artifact
     */
    Artifact copy();

    /**
     * Returns the list of dependencies for this artifact
     *
     * @return The dependency list
     */
    @Override
    DependencyList dependencies();

    /**
     * Returns the descriptor for this artifact
     *
     * @return The artifact descriptor
     */
    ArtifactDescriptor descriptor();

    /**
     * Returns true if this artifact excludes the given artifact
     *
     * @param descriptor The artifact descriptor
     * @return True if the descriptor is excluded
     */
    boolean excludes(ArtifactDescriptor descriptor);

    /**
     * Returns a skeletal Maven POM for this artifact
     *
     * @return The POM
     */
    String mavenPom();

    /**
     * Returns the repository where this artifact is hosted
     *
     * @return The artifact repository
     */
    @Override
    Repository repository();

    /**
     * Returns this artifact in JSON form
     */
    default String toJson()
    {
        var serializer = new GsonObjectSerializer();
        var serialized = new StringOutputResource();
        serializer.writeObject(serialized, new SerializableObject<>(this), METADATA_OBJECT_TYPE, METADATA_OBJECT_VERSION);
        return serialized.string();
    }

    /**
     * Convenience method for {@link #withVersion(Version)}
     */
    default Artifact version(Version version)
    {
        return withVersion(version);
    }

    /**
     * Convenience method for {@link #withVersion(Version)}
     */
    default Artifact version(String version)
    {
        return withVersion(parseVersion(version));
    }

    /**
     * Returns a copy of this artifact with the given identifier
     *
     * @param artifact The new artifact identifier
     * @return The new artifact
     */
    default Artifact withArtifactIdentifier(String artifact)
    {
        return withDescriptor(descriptor().withArtifactIdentifier(artifact));
    }

    /**
     * Attaches the resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param attachment The content to attach
     */
    Artifact withAttachment(ArtifactAttachment attachment);

    /**
     * Returns primary content attachment for this asset
     *
     * @return The content
     */
    default Artifact withContent(ArtifactContent content)
    {
        var copy = copy();
        copy.withAttachment(new ArtifactAttachment(this, CONTENT_SUFFIX, content));
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    Artifact withDependencies(DependencyList dependencies);

    /**
     * Returns a copy of this artifact with the given descriptor
     *
     * @param descriptor The new descriptor
     * @return The new artifact
     */
    Artifact withDescriptor(ArtifactDescriptor descriptor);

    /**
     * Returns a copy of this artifact with the given version
     *
     * @param version The new version
     * @return The new artifact
     */
    default Artifact withVersion(Version version)
    {
        return withDescriptor(descriptor().withVersion(version));
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    default Artifact withoutDependencies(ArtifactDescriptor... exclude)
    {
        return withoutDependencies(library -> arrayContains(exclude, library));
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    default Artifact withoutDependencies(String... exclude)
    {
        var descriptors = list(exclude).map(ArtifactDescriptor::artifactDescriptor);
        return withoutDependencies(descriptors::contains);
    }

    /**
     * Returns a copy of this artifact that excludes all descriptors matching the given pattern from its dependencies
     *
     * @param pattern The pattern to exclude
     * @return The new artifact
     */
    Artifact withoutDependencies(Matcher<ArtifactDescriptor> pattern);
}
