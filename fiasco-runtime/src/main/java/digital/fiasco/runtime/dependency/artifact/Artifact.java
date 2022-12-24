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

/**
 * Represents an artifact, either an {@link ArtifactType#ASSET}, or an {@link ArtifactType#LIBRARY}.
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
    A version(Version version);

    /**
     * Convenience method for {@link #withVersion(Version)}
     */
    A version(String version);

    /**
     * Attaches the resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param attachment The content to attach
     */
    A withAttachment(ArtifactAttachment attachment);

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
    A withIdentifier(String identifier);

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
    A withVersion(Version version);

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    A withoutDependencies(ArtifactDescriptor... exclude);

    /**
     * Returns a copy of this artifact that excludes all descriptors matching the given pattern from its dependencies
     *
     * @param pattern The pattern to exclude
     * @return The new artifact
     */
    A withoutDependencies(Matcher<ArtifactDescriptor> pattern);
}
