package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.Repository;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.version.Version.parseVersion;
import static com.telenav.kivakit.interfaces.comparison.Filter.acceptAll;

/**
 * Represents an artifact, either an {@link ArtifactType#ASSET}, or an {@link ArtifactType#LIBRARY}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public abstract class BaseArtifact<A extends BaseArtifact<A>> implements Artifact<A>
{
    /** The repository where this artifact is hosted */
    private Repository repository;

    /** The descriptor for this artifact */
    protected ArtifactDescriptor descriptor;

    /** The type of artifact, either an asset or a library */
    protected ArtifactType type;

    /** List of dependent artifacts */
    protected DependencyList dependencies;

    /** The artifact JAR */
    protected ArtifactContent jar;

    /** Dependency exclusions for this artifact */
    private Filter<ArtifactDescriptor> exclusions = acceptAll();

    /**
     * Create artifact
     *
     * @param descriptor The artifact desdriptor
     */
    public BaseArtifact(ArtifactDescriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    /**
     * Create a copy of the given artifact
     *
     * @param that The artifact to copy
     */
    protected BaseArtifact(A that)
    {
        this.repository = that.repository();
        this.descriptor = that.descriptor();
        this.type = that.type();
        this.dependencies = that.dependencies().copy();
        this.jar = that.jar();
        this.exclusions = that.exclusions();
    }

    protected BaseArtifact()
    {
    }

    /**
     * Returns this artifact metadata as a Maven POM file
     *
     * @return The POM file
     */
    public String mavenPom()
    {
        var dependencies = stringList();
        for (var at : dependencies())
        {
            if (at instanceof Artifact<?> artifact)
            {
                var descriptor = artifact.descriptor();
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

    @Override
    public final DependencyList dependencies()
    {
        return dependencies;
    }

    @Override
    public final ArtifactDescriptor descriptor()
    {
        return descriptor;
    }

    public boolean excludes(ArtifactDescriptor descriptor)
    {
        return exclusions.accepts(descriptor);
    }

    public BaseArtifact<A> excluding(ArtifactDescriptor... exclude)
    {
        return excluding(library -> arrayContains(exclude, library));
    }

    public BaseArtifact<A> excluding(Matcher<ArtifactDescriptor> pattern)
    {
        exclusions = exclusions.exclude(pattern);
        return this;
    }

    public Filter<ArtifactDescriptor> exclusions()
    {
        return exclusions;
    }

    @Override
    public final ArtifactContent jar()
    {
        return jar;
    }

    @Override
    public final Repository repository()
    {
        return repository;
    }

    @Override
    public String toJson()
    {
        var serializer = new GsonObjectSerializer();
        var serialized = new StringOutputResource();
        serializer.writeObject(serialized, new SerializableObject<>(this));
        return serialized.string();
    }

    @Override
    public final ArtifactType type()
    {
        return type;
    }

    public A version(Version version)
    {
        return withVersion(version);
    }

    public A version(String version)
    {
        return withVersion(parseVersion(version));
    }

    public A withDependencies(DependencyList dependencies)
    {
        var copy = copy();
        copy.dependencies = dependencies.copy();
        return copy;
    }

    public A withDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copy();
        copy.descriptor = descriptor;
        return copy;
    }

    public A withIdentifier(String identifier)
    {
        var copy = copy();
        copy.descriptor = descriptor.withIdentifier(identifier);
        return copy;
    }

    public A withJar(ArtifactContent jar)
    {
        var copy = copy();
        copy.jar = jar;
        return copy;
    }

    public A withType(ArtifactType type)
    {
        var copy = copy();
        copy.type = type;
        return copy;
    }

    public A withVersion(Version version)
    {
        var copy = copy();
        copy.descriptor = descriptor.withVersion(version);
        return copy;
    }

    protected abstract A copy();
}
