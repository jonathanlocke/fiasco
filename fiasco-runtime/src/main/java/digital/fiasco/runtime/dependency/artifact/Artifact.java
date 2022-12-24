package digital.fiasco.runtime.dependency.artifact;

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
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.CONTENT_SUFFIX;

/**
 * Represents an artifact, either an {@link ArtifactType#ASSET}, or an {@link ArtifactType#LIBRARY}.
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
 *     <li>{@link #type()}</li>
 *     <li>{@link #version(String)}</li>
 *     <li>{@link #version(Version)}</li>
 *     <li>{@link #withDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withIdentifier(String)}</li>
 *     <li>{@link #withType(ArtifactType)}</li>
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
 *     <li>{@link #attachments()}</li>
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
 *     <li>{@link #withIdentifier(String)} - Returns this artifact with the given identifier</li>
 *     <li>{@link #withType(ArtifactType)} - Returns this artifact with the given type</li>
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
public interface Artifact<A extends Artifact<A>> extends Dependency<A>
{
    @SuppressWarnings("unchecked")
    static <A extends Artifact<A>> A artifactFromJson(String json)
    {
        var serialized = new StringResource(json);
        var serializer = new GsonObjectSerializer();
        return (A) serializer.readObject(serialized, BaseArtifact.class).object();
    }

    /**
     * Returns the attached resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param suffix The artifact suffix
     * @return The attached resource
     */
    ArtifactAttachment attachment(String suffix);

    /**
     * Returns the list of all attached resources
     *
     * @return The attachments
     */
    ObjectMap<String, ArtifactAttachment> attachments();

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
    A copy();

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
        serializer.writeObject(serialized, new SerializableObject<>(this));
        return serialized.string();
    }

    /**
     * Returns the type of this artifact
     *
     * @return The artifact type
     */
    ArtifactType type();

    /**
     * Convenience method for {@link #withVersion(Version)}
     */
    default A version(Version version)
    {
        return withVersion(version);
    }

    /**
     * Convenience method for {@link #withVersion(Version)}
     */
    default A version(String version)
    {
        return withVersion(parseVersion(version));
    }

    /**
     * Attaches the resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param attachment The content to attach
     */
    A withAttachment(ArtifactAttachment attachment);

    /**
     * Returns primary content attachment for this asset
     *
     * @return The content
     */
    default A withContent(ArtifactContent content)
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
    A withDependencies(DependencyList dependencies);

    /**
     * Returns a copy of this artifact with the given descriptor
     *
     * @param descriptor The new descriptor
     * @return The new artifact
     */
    A withDescriptor(ArtifactDescriptor descriptor);

    /**
     * Returns a copy of this artifact with the given identifier
     *
     * @param identifier The new identifier
     * @return The new artifact
     */
    default A withIdentifier(String identifier)
    {
        return withDescriptor(descriptor().withIdentifier(identifier));
    }

    /**
     * Returns a copy of this artifact with the given artifact type
     *
     * @param type The new artifact type
     * @return The new artifact
     */
    A withType(ArtifactType type);

    /**
     * Returns a copy of this artifact with the given version
     *
     * @param version The new version
     * @return The new artifact
     */
    default A withVersion(Version version)
    {
        return withDescriptor(descriptor().withVersion(version));
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    default A withoutDependencies(ArtifactDescriptor... exclude)
    {
        return withoutDependencies(library -> arrayContains(exclude, library));
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    default A withoutDependencies(String... exclude)
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
    A withoutDependencies(Matcher<ArtifactDescriptor> pattern);
}
