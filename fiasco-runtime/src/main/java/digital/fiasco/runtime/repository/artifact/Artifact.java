package digital.fiasco.runtime.repository.artifact;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.string.Formatter.format;

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
public record Artifact(ArtifactDescriptor descriptor,
                       ObjectList<Artifact> dependencies,
                       ArtifactContent jar,
                       ArtifactContent javadoc,
                       ArtifactContent source)
{
    public static Artifact fromJson(String json)
    {
        var serialized = new StringResource(json);
        var serializer = new GsonObjectSerializer();
        return serializer.readObject(serialized, Artifact.class).object();
    }

    /**
     * Returns this artifact metadata as a Maven POM file
     *
     * @return The POM file
     */
    public String asMavenPom()
    {
        var dependencies = stringList();
        for (var at : dependencies())
        {
            var descriptor = at.descriptor();
            dependencies.add("""
                            <dependency>
                              <groupId>$</groupId>
                              <artifactId>$</artifactId>
                              <version>$</version>
                            </dependency>
                             """,
                    descriptor.group(),
                    descriptor.identifier(),
                    descriptor.version());
        }

        return format("""
                        <project
                          xmlns="http://maven.apache.org/POM/4.0.0"
                          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                         <modelVersion>4.0.0</modelVersion>
                         <groupId>$</groupId>
                         <artifactId>$</artifactId>
                         <version>$</version>
                         <dependencies>
                         $
                         </dependencies>
                        </project>
                          """,
                descriptor.group(),
                descriptor.identifier(),
                descriptor.version(),
                dependencies
                        .indented(2)
                        .join("\n"));
    }

    public String toJson()
    {
        var serializer = new GsonObjectSerializer();
        var serialized = new StringOutputResource();
        serializer.writeObject(serialized, new SerializableObject<>(this));
        return serialized.string();
    }

    public Artifact withJar(ArtifactContent jar)
    {
        return new Artifact(descriptor, dependencies, ensureNotNull(jar), javadoc, source);
    }

    public Artifact withJavadoc(ArtifactContent javadoc)
    {
        return new Artifact(descriptor, dependencies, jar, ensureNotNull(javadoc), source);
    }

    public Artifact withSource(ArtifactContent source)
    {
        return new Artifact(descriptor, dependencies, jar, javadoc, ensureNotNull(source));
    }
}
