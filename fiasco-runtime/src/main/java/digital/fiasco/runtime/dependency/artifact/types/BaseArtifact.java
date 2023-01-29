package digital.fiasco.runtime.dependency.artifact.types;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.version.Version;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName;
import digital.fiasco.runtime.dependency.collections.ArtifactList;
import digital.fiasco.runtime.dependency.collections.BuilderList;
import digital.fiasco.runtime.repository.Repository;

import java.util.LinkedHashMap;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.split;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.string.Formatter.format;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.JAR_ATTACHMENT;

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
 *     <li>{@link #artifactDependencies()} ()}</li>
 *     <li>{@link #isExcluded(ArtifactDescriptor)}</li>
 *     <li>{@link #withDependencies(ArtifactList)}</li>
 *     <li>{@link #excluding(ArtifactDescriptor...)}</li>
 *     <li>{@link #excluding(String...)}</li>
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
 *     <li>{@link #withDependencies(ArtifactList)} - Returns this artifact with the given dependencies</li>
 *     <li>{@link #withDescriptor(ArtifactDescriptor)} - Returns this artifact with the given descriptor</li>
 *     <li>{@link #withArtifact(String)} - Returns this artifact with the given artifact name</li>
 *     <li>{@link #withVersion(Version)} - Returns this artifact with the given version</li>
 *     <li>{@link #excluding(ArtifactDescriptor...)} - Returns this artifact without the given dependencies</li>
 *     <li>{@link #excluding(String...)} - Returns this artifact without the given dependencies</li>
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
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public abstract class BaseArtifact<A extends BaseArtifact<A>> implements Artifact<A>
{
    /** The repository where this artifact is hosted */
    @Expose
    @FormatProperty
    private Repository repository;

    /** The descriptor for this artifact */
    @Expose
    @FormatProperty
    protected ArtifactDescriptor descriptor;

    /** List of dependent artifacts */
    @Expose
    protected ArtifactList dependencies = ArtifactList.artifacts();

    /** Dependency exclusions for this artifact */
    protected transient ObjectList<ArtifactDescriptor> exclusions;

    /** The content attachments by type */
    @Expose
    private ObjectMap<ArtifactAttachmentType, ArtifactAttachment> typeToAttachment;

    /**
     * Create an artifact
     *
     * @param descriptor The artifact descriptor
     */
    @MethodQuality(audience = AUDIENCE_INTERNAL, documentation = DOCUMENTED, testing = TESTED)
    protected BaseArtifact(ArtifactDescriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    /**
     * Create a copy of the given artifact
     *
     * @param that The artifact to copy
     */
    @MethodQuality(audience = AUDIENCE_INTERNAL, documentation = DOCUMENTED, testing = TESTED)
    protected BaseArtifact(BaseArtifact<A> that)
    {
        this.repository = that.repository();
        this.descriptor = that.descriptor();
        this.dependencies = that.dependencies.copy();
        this.exclusions = that.exclusions().copy();
        this.typeToAttachment = that.typeToAttachment().copy();
    }

    /**
     * Returns the artifact name for this artifact
     *
     * @return The artifact name
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactName artifact()
    {
        return descriptor.artifact();
    }

    /**
     * Returns a list of artifacts without any excluded artifacts
     *
     * @return The artifacts
     */
    @Override
    @FormatProperty
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactList artifactDependencies()
    {
        return dependencies.matching(at -> !isExcluded(at));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString(Format format)
    {
        return switch (format)
            {
                case DEBUG -> new ObjectFormatter(this).toString();
                default -> name();
            };
    }

    /**
     * Returns the attached resource for the given type, such as <i>.jar</i> or <i>-sources.jar</i>.
     *
     * @param type The artifact type
     * @return The attachment of the given type with the given name, or null if there is none
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactAttachment attachmentOfType(ArtifactAttachmentType type)
    {
        return typeToAttachment().get(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FormatProperty
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ObjectList<ArtifactAttachment> attachments()
    {
        return list(typeToAttachment().values());
    }

    /**
     * Artifacts do not depend on builders
     *
     * @throws RuntimeException Always thrown
     */
    @Override
    public BuilderList builderDependencies()
    {
        return unsupported();
    }

    /**
     * Returns a copy of this artifact, of type A, where A is either Library or Asset.
     *
     * @return The copy
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public abstract A copy();

    /**
     * Returns the dependency matching the given dependency pattern
     *
     * @param pattern The pattern to match, like ":a:b:" or ":a::"
     * @return Any matching dependency
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactList dependenciesMatching(String pattern)
    {
        var matches = ArtifactList.artifacts();
        var matcher = ArtifactDescriptor.descriptor(pattern);
        for (var at : dependencies)
        {
            if (matcher.matches(at.descriptor()))
            {
                matches = matches.with(at);
            }
        }
        return matches;
    }

    /**
     * Returns the named dependency of this artifact
     *
     * @param name The name of the dependency
     * @return The dependency
     * @throws RuntimeException Thrown if the named dependency cannot be found
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
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
    @SafeVarargs
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public final <D extends Artifact<D>> A dependsOn(D... dependencies)
    {
        return withDependencies(ArtifactList.artifacts(dependencies));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public final ArtifactDescriptor descriptor()
    {
        return descriptor;
    }

    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
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
     * @param exclusion The pattern to exclude
     * @return The new artifact
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public A excluding(ArtifactDescriptor exclusion)
    {
        var copy = copy();
        copy.exclusions().add(exclusion);
        return copy;
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclusions The descriptors to exclude
     * @return The new artifact
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public A excluding(ObjectList<ArtifactDescriptor> exclusions)
    {
        var copy = copy();
        copy.exclusions.addAll(exclusions);
        return copy;
    }

    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public int hashCode()
    {
        return descriptor().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean isExcluded(ArtifactDescriptor descriptor)
    {
        for (var at : exclusions())
        {
            if (at.matches(descriptor))
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
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
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
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public String mavenPom()
    {
        var pom = stringList();

        var artifacts = artifactDependencies();
        if (artifacts.isNonEmpty())
        {
            for (var artifact : artifacts)
            {
                var descriptor = artifact.descriptor();
                pom.addAll(split(format("""
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

            pom = pom.indented(4);
            pom = pom.prepending("");
            pom = pom.prepending("<dependencies>");
            pom = pom.prepending("");
            pom = pom.appending("</dependencies>");
            pom = pom.appending("");
            pom = pom.indented(4);
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
                $
                </project>
                  """,
            descriptor.group(),
            descriptor.artifact(),
            descriptor.version(),
            pom.join("\n"));
    }

    @Override
    @FormatProperty
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public String name()
    {
        return descriptor.name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public final Repository repository()
    {
        return repository;
    }

    @Override
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTING_NOT_NEEDED)
    public String toString()
    {
        return name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public A withAttachment(ArtifactAttachment attachment)
    {
        var copy = copy();
        attachment = attachment.withArtifact(copy);
        ((BaseArtifact<A>) copy).typeToAttachment().put(attachment.attachmentType(), attachment);
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTED)
    @Override
    public A withAttachments(ObjectMap<ArtifactAttachmentType, ArtifactAttachment> attachments)
    {
        var copy = copy();
        for (var entry : attachments.entrySet())
        {
            copy = copy.withAttachment(entry.getValue());
        }
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public A withDependencies(ArtifactList dependencies)
    {
        var copy = copy();
        copy.dependencies = this.dependencies
            .with(dependencies)
            .deduplicate();
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public A withDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copy();
        copy.descriptor = descriptor;
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public A withRepository(Repository repository)
    {
        var copy = copy();
        ((BaseArtifact<?>) copy).repository = repository;
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A withoutAttachments()
    {
        var copy = copy();
        ((BaseArtifact<A>) copy).typeToAttachment = new ObjectMap<>();
        return copy;
    }

    ObjectList<ArtifactDescriptor> exclusions()
    {
        if (exclusions == null)
        {
            exclusions = list();
        }
        return exclusions;
    }

    private ObjectMap<ArtifactAttachmentType, ArtifactAttachment> typeToAttachment()
    {
        if (typeToAttachment == null)
        {
            typeToAttachment = new ObjectMap<>(new LinkedHashMap<>());
        }
        return typeToAttachment;
    }
}
