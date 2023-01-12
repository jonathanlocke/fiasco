package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.Repository;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.interfaces.comparison.Filter.acceptNone;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAR_ATTACHMENT;

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
 *     <li>{@link #isExcluded(ArtifactDescriptor)}</li>
 *     <li>{@link #withDependencies(DependencyList)}</li>
 *     <li>{@link #excluding(ArtifactDescriptor...)}</li>
 *     <li>{@link #excluding(String...)}</li>
 *     <li>{@link #excluding(Matcher)}</li>
 * </ul>
 *
 * <p><b>Attachments</b></p>
 *
 * <ul>
 *     <li>{@link #attachment(ArtifactAttachmentType)}</li>
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
 *     <li>{@link #excluding(ArtifactDescriptor...)} - Returns this artifact without the given dependencies</li>
 *     <li>{@link #excluding(String...)} - Returns this artifact without the given dependencies</li>
 *     <li>{@link #excluding(Matcher)} - Returns this artifact without the given dependencies</li>
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
public abstract class BaseArtifact<T extends BaseArtifact<T>> implements Artifact<T>
{
    /** The repository where this artifact is hosted */
    private Repository repository;

    /** The descriptor for this artifact */
    protected ArtifactDescriptor descriptor;

    /** List of dependent artifacts */
    protected DependencyList<Artifact<?>> dependencies = dependencyList();

    /** Dependency exclusions for this artifact */
    protected ObjectList<Matcher<ArtifactDescriptor>> exclusions = list(acceptNone());

    /** The content attachments by type */
    private ObjectMap<ArtifactAttachmentType, ArtifactAttachment> typeToAttachment = new ObjectMap<>();

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
    protected BaseArtifact(BaseArtifact<T> that)
    {
        this.repository = that.repository();
        this.descriptor = that.descriptor();
        this.dependencies = that.dependencies().copy();
        this.exclusions = that.exclusions().copy();
        this.typeToAttachment = that.typeToAttachment.copy();
    }

    protected BaseArtifact()
    {
    }

    /**
     * Returns the attached resource for the given type, such as <i>.jar</i> or <i>-sources.jar</i>.
     *
     * @param type The artifact type
     * @return Any attached resource with the given name, or null if there is none
     */
    @Override
    public ArtifactAttachment attachment(ArtifactAttachmentType type)
    {
        return typeToAttachment.get(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<ArtifactAttachment> attachments()
    {
        return list(typeToAttachment.values());
    }

    @Override
    public abstract T copy();

    /**
     * Returns a list of artifacts without any excluded artifacts
     *
     * @return The artifacts
     */
    @Override
    public DependencyList<Artifact<?>> dependencies()
    {
        var copy = dependencies.copy();
        for (var exclusion : exclusions)
        {
            copy = copy.without(at -> exclusion.matches(at.descriptor()));
        }
        return copy;
    }

    /**
     * Returns the named dependency of this artifact
     *
     * @param name The name of the dependency
     * @return The dependency
     */
    public Artifact<?> dependency(String name)
    {
        for (var at : dependencies)
        {
            if (at.name().equals(name))
            {
                return at;
            }
        }
        return illegalState("No dependency $ found", name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ArtifactDescriptor descriptor()
    {
        return descriptor;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Artifact<?> artifact)
        {
            return descriptor().equals(artifact.descriptor());
        }
        return false;
    }

    /**
     * Returns a copy of this artifact that excludes all descriptors matching the given pattern from its dependencies
     *
     * @param pattern The pattern to exclude
     * @return The new artifact
     */
    @Override
    public T excluding(Matcher<ArtifactDescriptor> pattern)
    {
        var copy = copy();
        copy.exclusions.add(pattern);
        return copy;
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    @Override
    public T excluding(ArtifactDescriptor... exclude)
    {
        return excluding(library -> arrayContains(exclude, library));
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    @Override
    public T excluding(String... exclude)
    {
        var descriptors = list(exclude).map(ArtifactDescriptor::descriptor);
        return excluding(descriptors::contains);
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

    @Override
    public int hashCode()
    {
        return descriptor().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExcluded(ArtifactDescriptor descriptor)
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
     * Returns this artifact metadata as a Maven POM file
     *
     * @return The POM file
     */
    @Override
    public String mavenPom()
    {
        var dependencies = stringList();
        for (var artifact : dependencies())
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
                descriptor.artifact(),
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
            descriptor.artifact(),
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
     * Returns a copy of this artifact with the given identifier
     *
     * @param artifact The new artifact identifier
     * @return The new artifact
     */
    @Override
    public T withArtifactIdentifier(String artifact)
    {
        return withDescriptor(descriptor().withArtifact(artifact));
    }

    /**
     * Attaches the resource for the given artifact type, such as <i>.jar</i>
     *
     * @param attachment The content to attach
     */
    @Override
    public T withAttachment(ArtifactAttachment attachment)
    {
        var copy = copy();
        ((BaseArtifact<T>) copy).typeToAttachment.put(attachment.type(), attachment);
        return copy;
    }

    @Override
    public T withAttachments(ObjectMap<ArtifactAttachmentType, ArtifactAttachment> attachments)
    {
        var copy = copy();
        ((BaseArtifact<T>) copy).typeToAttachment = attachments;
        return copy;
    }

    /**
     * Returns primary content attachment for this asset
     *
     * @return The content
     */
    @Override
    public T withContent(ArtifactContent content)
    {
        var copy = copy();
        copy.withAttachment(new ArtifactAttachment(this, JAR_ATTACHMENT, content));
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    @Override
    public T withDependencies(DependencyList<Artifact<?>> dependencies)
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
    public T withDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copy();
        copy.descriptor = descriptor;
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given version
     *
     * @param version The new version
     * @return The new artifact
     */
    @Override
    public T withVersion(Version version)
    {
        return withDescriptor(descriptor().withVersion(version));
    }
}
