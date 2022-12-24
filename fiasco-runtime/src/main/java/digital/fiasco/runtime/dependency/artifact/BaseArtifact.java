package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.Repository;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.interfaces.comparison.Filter.acceptAll;

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

    /** Dependency exclusions for this artifact */
    private ObjectList<Matcher<ArtifactDescriptor>> exclusions = list(acceptAll());

    /** The content attachments by suffix */
    private ObjectMap<String, ArtifactAttachment> attachments = new ObjectMap<>();

    /**
     * Create artifact
     *
     * @param descriptor The artifact descriptor
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
        this.exclusions = that.exclusions().copy();
        this.attachments = that.attachments().copy();
    }

    protected BaseArtifact()
    {
    }

    /**
     * Returns the attached resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param suffix The artifact suffix
     * @return The attached resource
     */
    @Override
    public ArtifactAttachment attachment(String suffix)
    {
        return attachments.get(suffix);
    }
 
    /**
     * Returns the list of all attached resources
     *
     * @return The attachments
     */
    @Override
    public final ObjectMap<String, ArtifactAttachment> attachments()
    {
        return attachments;
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

    @Override
    public String name()
    {
        return descriptor.name();
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
     * Attaches the resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param attachment The content to attach
     */
    @Override
    public A withAttachment(ArtifactAttachment attachment)
    {
        var copy = copy();
        copy.attachments().put(attachment.suffix(), attachment);
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    @Override
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
    @Override
    public A withDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copy();
        copy.descriptor = descriptor;
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given artifact type
     *
     * @param type The new artifact type
     * @return The new artifact
     */
    @Override
    public A withType(ArtifactType type)
    {
        var copy = copy();
        copy.type = type;
        return copy;
    }

    /**
     * Returns a copy of this artifact that excludes all descriptors matching the given pattern from its dependencies
     *
     * @param pattern The pattern to exclude
     * @return The new artifact
     */
    @Override
    public A withoutDependencies(Matcher<ArtifactDescriptor> pattern)
    {
        var copy = copy();
        ((BaseArtifact<?>) copy).exclusions.add(pattern);
        return copy;
    }
}
