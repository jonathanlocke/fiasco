package digital.fiasco.runtime.repository.cache;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import digital.fiasco.runtime.repository.Library;
import digital.fiasco.runtime.repository.artifact.ArtifactDescriptor;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * Holds the metadata for an artifact
 *
 * @param descriptor The artifact descriptor
 * @param dependencies Any dependent artifacts
 * @param jar The attached artifact jar
 * @param javadoc The attached javadoc jar
 * @param source The attached source jar
 */
@SuppressWarnings("unused")
public record ArtifactMetadata(ArtifactDescriptor descriptor,
                               ObjectList<Library> dependencies,
                               ContentMetadata jar,
                               ContentMetadata javadoc,
                               ContentMetadata source)
{
    public static ArtifactMetadata fromJson(String json)
    {
        var serialized = new StringResource(json);
        var serializer = new GsonObjectSerializer();
        return serializer.readObject(serialized, ArtifactMetadata.class).object();
    }

    public String toJson()
    {
        var serializer = new GsonObjectSerializer();
        var serialized = new StringOutputResource();
        serializer.writeObject(serialized, new SerializableObject<>(this));
        return serialized.string();
    }

    public ArtifactMetadata withJar(ContentMetadata jar)
    {
        return new ArtifactMetadata(descriptor, dependencies, ensureNotNull(jar), javadoc, source);
    }

    public ArtifactMetadata withJavadoc(ContentMetadata javadoc)
    {
        return new ArtifactMetadata(descriptor, dependencies, jar, ensureNotNull(javadoc), source);
    }

    public ArtifactMetadata withSource(ContentMetadata source)
    {
        return new ArtifactMetadata(descriptor, dependencies, jar, javadoc, ensureNotNull(source));
    }
}
