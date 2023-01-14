package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.Repository;

import java.util.LinkedHashMap;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.split;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.interfaces.comparison.Filter.acceptNone;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.attachment;
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
 *     <li>{@link #withArtifact(String)}</li>
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
 *     <li>{@link #attachmentOfType(ArtifactAttachmentType)}</li>
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
 *     <li>{@link #withArtifact(String)} - Returns this artifact with the given artifact name</li>
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
    @FormatProperty
    private Repository repository;

    /** The descriptor for this artifact */
    @FormatProperty
    protected ArtifactDescriptor descriptor;

    /** List of dependent artifacts */
    protected DependencyList<Artifact<?>> dependencies = dependencyList();

    /** Dependency exclusions for this artifact */
    protected ObjectList<Matcher<ArtifactDescriptor>> exclusions = list(acceptNone());

    /** The content attachments by type */
    private ObjectMap<ArtifactAttachmentType, ArtifactAttachment> typeToAttachment = new ObjectMap<>(new LinkedHashMap<>());

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

    public ArtifactName artifact()
    {
        return descriptor.artifact();
    }

    /**
     * Returns the attached resource for the given type, such as <i>.jar</i> or <i>-sources.jar</i>.
     *
     * @param type The artifact type
     * @return The attachment of the given type with the given name, or null if there is none
     */
    @Override
    @MethodQuality
        (
            documentation = DOCUMENTATION_COMPLETE,
            testing = TESTED
        )
    public ArtifactAttachment attachmentOfType(ArtifactAttachmentType type)
    {
        return typeToAttachment.get(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FormatProperty
    @MethodQuality
        (
            documentation = DOCUMENTATION_COMPLETE,
            testing = TESTED
        )
    public ObjectList<ArtifactAttachment> attachments()
    {
        return list(typeToAttachment.values());
    }

    /**
     * Returns a copy of this artifact, of type T, where T is either Library or Asset.
     *
     * @return The copy
     */
    @Override
    @MethodQuality
        (
            documentation = DOCUMENTATION_COMPLETE,
            testing = TESTED
        )
    public abstract T copy();

    /**
     * Returns a list of artifacts without any excluded artifacts
     *
     * @return The artifacts
     */
    @Override
    @FormatProperty
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
     * Returns the dependency matching the given dependency pattern
     *
     * @param pattern The pattern to match, like "a:b:" or "a::"
     * @return Any matching dependency
     */
    @Override
    public DependencyList<Artifact<?>> dependenciesMatching(String pattern)
    {
        var matches = new DependencyList<Artifact<?>>();
        var matcher = ArtifactDescriptor.descriptor(pattern);
        for (var at : dependencies)
        {
            if (matcher.matches(at.descriptor()))
            {
                matches.add(at);
            }
        }
        return matches;
    }

    /**
     * Returns the dependency matching the given dependency pattern
     *
     * @param pattern The pattern to match, like "a:b:" or "a::"
     * @return Any matching dependency
     */
    @Override
    public Artifact<?> dependencyMatching(String pattern)
    {
        for (var at : dependencies)
        {
            if (ArtifactDescriptor.descriptor(pattern).matches(at.descriptor()))
            {
                return at;
            }
        }
        return illegalState("No dependency $ found", pattern);
    }

    /**
     * Returns the named dependency of this artifact
     *
     * @param name The name of the dependency
     * @return The dependency
     */
    @Override
    public Artifact<?> dependencyNamed(String name)
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
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    @Override
    public T dependsOn(Artifact<?>... dependencies)
    {
        return withDependencies(dependencyList(dependencies));
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
     * Returns the JAR attachment for this library,
     *
     * @return The JAR content
     */
    public ArtifactContent jar()
    {
        return attachmentOfType(JAR_ATTACHMENT).content();
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
            dependencies.addAll(split(format("""
                       <dependency>
                           <groupId>$</groupId>
                           <artifactId>$</artifactId>
                           <version>$</version>
                       </dependency>
                    """,
                descriptor.group(),
                descriptor.artifact(),
                descriptor.version()), "\n"));
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
                .prefixedWith("    ")
                .join("\n")
                .replaceFirst(AsciiArt.repeat(7, ' '), ""));
    }

    @Override
    @FormatProperty
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

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * Attaches the resource for the given artifact type, such as <i>.jar</i>
     *
     * @param attachment The content to attach
     */
    @Override
    @MethodQuality
        (
            documentation = DOCUMENTATION_COMPLETE,
            testing = TESTED
        )
    public T withAttachment(ArtifactAttachment attachment)
    {
        var copy = copy();
        attachment = attachment.withArtifact(copy);
        ((BaseArtifact<T>) copy).typeToAttachment.put(attachment.attachmentType(), attachment);
        return copy;
    }

    @MethodQuality
        (
            audience = AUDIENCE_INTERNAL,
            documentation = DOCUMENTATION_NOT_NEEDED,
            testing = TESTED
        )
    @Override
    public T withAttachments(ObjectMap<ArtifactAttachmentType, ArtifactAttachment> attachments)
    {
        var copy = copy();
        for (var entry : attachments.entrySet())
        {
            copy = copy.withAttachment(entry.getValue());
        }
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
        return copy().withAttachment(attachment(JAR_ATTACHMENT, content));
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
     * Returns a copy of this library with the given Javadoc attachment
     *
     * @param jar The Javadoc content
     * @return The new library
     */
    public T withJar(ArtifactContent jar)
    {
        return copy().withAttachment(attachment(JAR_ATTACHMENT, jar));
    }

    /**
     * Returns a copy of this artifact with the given repository
     *
     * @param repository The new descriptor
     * @return The new artifact
     */
    @Override
    public T withRepository(Repository repository)
    {
        var copy = copy();
        ((BaseArtifact<?>) copy).repository = repository;
        return copy;
    }
}
