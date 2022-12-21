package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.Repository;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.version.Version.parseVersion;
import static com.telenav.kivakit.interfaces.comparison.Filter.acceptAll;

/**
 * Represents an artifact, either an {@link ArtifactType#ASSET}, or an {@link ArtifactType#LIBRARY}.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies()} - The list of dependencies for this artifact</li>
 *     <li>{@link #descriptor()} - This artifact's descriptor (group:identifier:version)</li>
 *     <li>{@link #jar()} - The JAR content for this artifact</li>
 *     <li>{@link #mavenPom()} - Returns a skeletal Maven POM for this artifact</li>
 *     <li>{@link #repository()} - The repository where this artifact can be found</li>
 *     <li>{@link #type()} - The type of artifact</li>
 * </ul>
 *
 * <p><b>Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies()} - The list of dependencies for this artifact</li>
 *     <li>{@link #excludes(ArtifactDescriptor)} - Returns true if this artifact excludes the given artifact</li>
 *     <li>{@link #withoutDependencies(ArtifactDescriptor...)} - Returns this artifact without the given dependencies</li>
 *     <li>{@link #withoutDependencies(Matcher)} - Returns this artifact without the given dependencies</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #copy()} - Returns a copy of this artifact</li>
 *     <li>{@link #version(String)} - Returns this artifact with the given version</li>
 *     <li>{@link #version(Version)} - Returns this artifact with the given version</li>
 *     <li>{@link #withDependencies(DependencyList)} - Returns this artifact with the given dependencies</li>
 *     <li>{@link #withDescriptor(ArtifactDescriptor)} - Returns this artifact with the given descriptor</li>
 *     <li>{@link #withIdentifier(String)} - Returns this artifact with the given identifier</li>
 *     <li>{@link #withJar(ArtifactContentMetadata)} - Returns this artifact with the given JAR content</li>
 *     <li>{@link #withType(ArtifactType)} - Returns this artifact with the given type</li>
 *     <li>{@link #withVersion(Version)} - Returns this artifact with the given version</li>
 *     <li>{@link #withoutDependencies(ArtifactDescriptor...)} - Returns this artifact without the given dependencies</li>
 *     <li>{@link #withoutDependencies(Matcher)} - Returns this artifact without the given dependencies</li>
 * </ul>
 *
 * @author Jonathan Locke
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
    protected ArtifactContentMetadata jar;

    /** Dependency exclusions for this artifact */
    private ObjectList<Matcher<ArtifactDescriptor>> exclusions = list(acceptAll());

    /**
     * Create artifact
     *
     * @param descriptor The artifact desdriptor
     */
    protected BaseArtifact(ArtifactDescriptor descriptor)
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
        this.exclusions = that.exclusions().copy();
    }

    protected BaseArtifact()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final DependencyList dependencies()
    {
        return dependencies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ArtifactDescriptor descriptor()
    {
        return descriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean excludes(ArtifactDescriptor descriptor)
    {
        for (var at : exclusions)
        {
            if (!at.matches(descriptor))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the list of exclusions for this artifact
     *
     * @return The list of matchers to exclude
     */
    public ObjectList<Matcher<ArtifactDescriptor>> exclusions()
    {
        return exclusions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ArtifactContentMetadata jar()
    {
        return jar;
    }

    /**
     * Returns this artifact metadata as a Maven POM file
     *
     * @return The POM file
     */
    @Override
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

    /**
     * {@inheritDoc}
     */
    @Override
    public final Repository repository()
    {
        return repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ArtifactType type()
    {
        return type;
    }

    /**
     * Convenience method for {@link #withVersion(Version)}
     */
    public A version(Version version)
    {
        return withVersion(version);
    }

    /**
     * Convenience method for {@link #withVersion(Version)}
     */
    public A version(String version)
    {
        return withVersion(parseVersion(version));
    }

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    public A withDependencies(DependencyList dependencies)
    {
        var copy = copy();
        copy.dependencies = dependencies.copy();
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given descriptor
     *
     * @param descriptor The new descriptor
     * @return The new artifact
     */
    public A withDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copy();
        copy.descriptor = descriptor;
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given identifier
     *
     * @param identifier The new identifier
     * @return The new artifact
     */
    public A withIdentifier(String identifier)
    {
        var copy = copy();
        copy.descriptor = descriptor.withIdentifier(identifier);
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given jar
     *
     * @param jar The new jar
     * @return The new artifact
     */
    public A withJar(ArtifactContentMetadata jar)
    {
        var copy = copy();
        copy.jar = jar;
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given artifact type
     *
     * @param type The new artifact type
     * @return The new artifact
     */
    public A withType(ArtifactType type)
    {
        var copy = copy();
        copy.type = type;
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given version
     *
     * @param version The new version
     * @return The new artifact
     */
    public A withVersion(Version version)
    {
        var copy = copy();
        copy.descriptor = descriptor.withVersion(version);
        return copy;
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    public A withoutDependencies(ArtifactDescriptor... exclude)
    {
        return withoutDependencies(library -> arrayContains(exclude, library));
    }

    /**
     * Returns a copy of this artifact that excludes all descriptors matching the given pattern from its dependencies
     *
     * @param pattern The pattern to exclude
     * @return The new artifact
     */
    public A withoutDependencies(Matcher<ArtifactDescriptor> pattern)
    {
        var copy = copy();
        ((BaseArtifact<?>) copy).exclusions.add(pattern);
        return copy;
    }

    /**
     * Returns a copy of this artifact
     *
     * @return The new artifact
     */
    protected abstract A copy();
}
