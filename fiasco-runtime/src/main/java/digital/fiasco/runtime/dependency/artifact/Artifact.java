package digital.fiasco.runtime.dependency.artifact;

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
    static <A extends Artifact<A>> A fromJson(String json)
    {
        var serialized = new StringResource(json);
        var serializer = new GsonObjectSerializer();
        return (A) serializer.readObject(serialized, BaseArtifact.class).object();
    }

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
     * Returns the primary JAR for this artifact
     *
     * @return The artifact content JAR
     */
    ArtifactContent jar();

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
}
