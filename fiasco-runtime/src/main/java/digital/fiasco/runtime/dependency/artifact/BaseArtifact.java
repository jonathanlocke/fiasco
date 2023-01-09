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
import static com.telenav.kivakit.interfaces.comparison.Filter.acceptAll;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.CONTENT_SUFFIX;

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
 *     <li>{@link #artifactDescriptor()}</li>
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
 *     <li>{@link #attachmentMap()}</li>
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
 *     <li>{@link #withArtifactIdentifier(String)} - Returns this artifact with the given identifier</li>
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
public abstract class BaseArtifact implements Artifact
{
    /** The repository where this artifact is hosted */
    private Repository repository;

    /** The descriptor for this artifact */
    protected ArtifactDescriptor descriptor;

    /** List of dependent artifacts */
    protected DependencyList<Artifact> dependencies = dependencyList();

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
    protected BaseArtifact(BaseArtifact that)
    {
        this.repository = that.repository();
        this.descriptor = that.artifactDescriptor();
        this.dependencies = that.dependencies().copy();
        this.exclusions = that.exclusions().copy();
        this.attachments = that.attachments.copy();
    }

    protected BaseArtifact()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ArtifactDescriptor artifactDescriptor()
    {
        return descriptor;
    }

    /**
     * Returns the attached resource for the given suffix, such as <i>.jar</i> or <i>-sources.jar</i>.
     *
     * @param suffix The artifact suffix
     * @return Any attached resource with the given name, or null if there is none
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
    public final ObjectMap<String, ArtifactAttachment> attachmentMap()
    {
        return attachments;
    }

    /**
     * Returns a list of artifacts without any excluded artifacts
     *
     * @return The artifacts
     */
    @Override
    public DependencyList<Artifact> dependencies()
    {
        var copy = dependencies.copy();
        for (var exclusion : exclusions)
        {
            copy = copy.without(at -> exclusion.matches(at.artifactDescriptor()));
        }
        return copy;
    }

    /**
     * Returns the named dependency of this artifact
     *
     * @param name The name of the dependency
     * @return The dependency
     */
    public Artifact dependency(String name)
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
        for (var artifact : dependencies())
        {
            var descriptor = artifact.artifactDescriptor();
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
    public Artifact withArtifactIdentifier(String artifact)
    {
        return withDescriptor(artifactDescriptor().withArtifactIdentifier(artifact));
    }

    /**
     * Attaches the resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param attachment The content to attach
     */
    @Override
    public Artifact withAttachment(ArtifactAttachment attachment)
    {
        var copy = copy();
        copy.attachmentMap().put(attachment.suffix(), attachment);
        return copy;
    }

    @Override
    public Artifact withAttachments(ObjectMap<String, ArtifactAttachment> attachments)
    {
        var copy = copy();
        ((BaseArtifact) copy).attachments = attachments;
        return copy;
    }

    /**
     * Returns primary content attachment for this asset
     *
     * @return The content
     */
    @Override
    public Artifact withContent(ArtifactContent content)
    {
        var copy = copy();
        copy.withAttachment(new ArtifactAttachment(this, CONTENT_SUFFIX, content));
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    @Override
    public Artifact withDependencies(DependencyList<Artifact> dependencies)
    {
        var copy = (BaseArtifact) copy();
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
    public Artifact withDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = (BaseArtifact) copy();
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
    public Artifact withVersion(Version version)
    {
        return withDescriptor(artifactDescriptor().withVersion(version));
    }

    /**
     * Returns a copy of this artifact that excludes all descriptors matching the given pattern from its dependencies
     *
     * @param pattern The pattern to exclude
     * @return The new artifact
     */
    @Override
    public Artifact withoutDependencies(Matcher<ArtifactDescriptor> pattern)
    {
        var copy = (BaseArtifact) copy();
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
    public Artifact withoutDependencies(ArtifactDescriptor... exclude)
    {
        return withoutDependencies(library -> arrayContains(exclude, library));
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    @Override
    public Artifact withoutDependencies(String... exclude)
    {
        var descriptors = list(exclude).map(ArtifactDescriptor::artifactDescriptor);
        return withoutDependencies(descriptors::contains);
    }
}
