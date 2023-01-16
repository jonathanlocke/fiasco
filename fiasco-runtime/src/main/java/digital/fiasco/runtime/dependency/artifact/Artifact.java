package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.string.AsString;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.Repository;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_TYPE;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.attachment;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAR_ATTACHMENT;

/**
 * Defines an artifact, either an {@link Asset} or a {@link Library}. Libraries are artifacts that have a source and
 * Javadoc content attachment as well as a JAR attachment.
 *
 * <p><b>Artifact Descriptors</b></p>
 *
 * <p>
 * Artifacts are referenced by {@link ArtifactDescriptor}s, which take the form [group:artifact:version], for example,
 * "com.telenav.kivakit:kivakit-application:1.10.0". The descriptor for an artifact can be retrieved with
 * {@link #descriptor()}.
 * </p>
 *
 * <p><b>Artifact Content Attachments</b></p>
 *
 * <p>
 * Each artifact has a set of {@link ArtifactAttachment}s, one for each {@link ArtifactAttachmentType}. The attachment
 * for a particular type, like {@link ArtifactAttachmentType#JAVADOC_ATTACHMENT}, can be retrieved with
 * {@link #attachmentOfType(ArtifactAttachmentType)}. The list of all attachments can be retrieved with
 * {@link #attachments()}. Each attachment has {@link ArtifactContent}, which includes the content {@link Resource} and
 * a set of {@link ArtifactContentSignatures} to allow verification of the accuracy of the content. The primary JAR
 * content for an artifact can be retrieved with {@link #content()}
 * </p>
 *
 * <p><b>Dependencies</b></p>
 *
 * <p>
 * An artifact can have zero or more dependencies. The method {@link #dependencies()} returns a {@link DependencyList}
 * of {@link Artifact}s (note that there are non-artifact dependencies, such as {@link Builder}s). Artifact dependencies
 * can be filtered by excluding certain descriptors with {@link #excluding(String...)},
 * {@link #excluding(ArtifactDescriptor...)}, or {@link #excluding(Matcher)}.
 * </p>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #repository()}</li>
 *     <li>{@link #mavenPom()}</li>
 *     <li>{@link #attachments()}</li>
 *     <li>{@link #content()}</li>
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
 *     <li>{@link #withDependencies(ArtifactList)}</li>
 *     <li>{@link #excluding(ArtifactDescriptor...)}</li>
 *     <li>{@link #excluding(String...)}</li>
 *     <li>{@link #excluding(Matcher)}</li>
 * </ul>
 *
 * <p><b>Attachments</b></p>
 *
 * <ul>
 *     <li>{@link #attachments()}</li>
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
 *     <li>{@link #withArtifact(String)} - Returns this artifact with the given name</li>
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
public interface Artifact<T extends Artifact<T>> extends Dependency, AsString
{
    /**
     * Returns an artifact for a given JSON string
     *
     * @param json The JSON
     * @return The artifact
     */
    @SuppressWarnings("unchecked")
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    static <T extends Artifact<T>> T artifactFromJson(String json)
    {
        var serialized = new StringResource(json);
        var serializer = new GsonObjectSerializer();
        return (T) serializer.readObject(serialized, METADATA_OBJECT_TYPE).object();
    }

    /**
     * Returns a copy of this artifact with the given name
     *
     * @param artifactName The new artifact name
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T artifact(String artifactName)
    {
        return withArtifact(artifactName);
    }

    /**
     * Returns a copy of this artifact with the given name
     *
     * @param artifact The new artifact name
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T artifact(ArtifactName artifact)
    {
        return withArtifact(artifact);
    }

    /**
     * Returns the attached resource for the given artifact type, such as <i>.jar</i>
     *
     * @param type The artifact type
     * @return The attached resource
     */
    ArtifactAttachment attachmentOfType(ArtifactAttachmentType type);

    /**
     * Returns a list of all attachments to this artifact
     *
     * @return The attachments
     */
    ObjectList<ArtifactAttachment> attachments();

    /**
     * Returns primary content attachment for this asset
     *
     * @return The content
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default ArtifactContent content()
    {
        return attachmentOfType(JAR_ATTACHMENT).content();
    }

    /**
     * Returns a copy of this artifact
     *
     * @return The new artifact
     */
    T copy();

    /**
     * Returns the list of dependencies for this artifact
     *
     * @return The dependency list
     */
    @Override
    ArtifactList dependencies();

    /**
     * Returns the dependencies matching the given dependency pattern
     *
     * @param pattern The pattern to match, like "a:b:" or "a::"
     * @return Any matching dependencies
     */
    ArtifactList dependenciesMatching(String pattern);

    /**
     * Returns the named dependency of this artifact
     *
     * @param name The name of the dependency
     * @return The dependency
     */
    Artifact<?> dependencyNamed(String name);

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    <D extends Artifact<D>> T dependsOn(D[] dependencies);

    /**
     * Returns the descriptor for this artifact
     *
     * @return The artifact descriptor
     */
    @Override
    ArtifactDescriptor descriptor();

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T excluding(ArtifactDescriptor... exclude)
    {
        return excluding(at -> arrayContains(exclude, at));
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclusions The descriptors to exclude
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T excluding(String... exclusions)
    {
        return excluding(list(exclusions).map(ArtifactDescriptor::descriptor));
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclusions The descriptors to exclude
     * @return The new artifact
     */
    T excluding(ObjectList<ArtifactDescriptor> exclusions);

    /**
     * Returns a copy of this artifact that excludes the given artifacts from its dependencies
     *
     * @param exclusions The artifacts to exclude
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T excluding(Artifact<?>... exclusions)
    {
        return excluding(list(exclusions).map(Artifact::descriptor));
    }

    /**
     * Returns a copy of this artifact that excludes all descriptors matching the given pattern from its dependencies
     *
     * @param pattern The pattern to exclude
     * @return The new artifact
     */
    T excluding(Matcher<ArtifactDescriptor> pattern);

    /**
     * Returns true if this artifact excludes the given artifact
     *
     * @param descriptor The artifact descriptor
     * @return True if the descriptor is excluded
     */
    boolean isExcluded(ArtifactDescriptor descriptor);

    /**
     * Returns true if this artifact excludes the given artifact
     *
     * @param artifact The artifact
     * @return True if the descriptor is excluded
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default boolean isExcluded(Artifact<?> artifact)
    {
        return isExcluded(artifact.descriptor());
    }

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
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default String toJson()
    {
        var serializer = new GsonObjectSerializer();
        var serialized = new StringOutputResource();
        serializer.writeObject(serialized, new SerializableObject<>(this), METADATA_OBJECT_TYPE);
        return serialized.string();
    }

    /**
     * Convenience method for {@link #withVersion(Version)}
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T version(Version version)
    {
        return withVersion(version);
    }

    /**
     * Convenience method for {@link #withVersion(Version)}
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T version(String version)
    {
        return withVersion(Version.version(version));
    }

    /**
     * Returns a copy of this artifact with the given name
     *
     * @param artifactName The new artifact name
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T withArtifact(String artifactName)
    {
        return withDescriptor(descriptor().withArtifact(artifactName));
    }

    /**
     * Returns a copy of this artifact with the given name
     *
     * @param artifactName The new artifact name
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T withArtifact(ArtifactName artifactName)
    {
        return withDescriptor(descriptor().withArtifact(artifactName));
    }

    /**
     * Attaches the given artifact attachment to this artifact
     *
     * @param attachment The content to attach
     */
    T withAttachment(ArtifactAttachment attachment);

    /**
     * Attaches the resource for the given artifact type, such as <i>.jar</i>
     *
     * @param attachments The content to attach
     */
    T withAttachments(ObjectMap<ArtifactAttachmentType, ArtifactAttachment> attachments);

    /**
     * Returns primary content attachment for this asset
     *
     * @return The content
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T withContent(ArtifactContent content)
    {
        return copy().withAttachment(attachment(JAR_ATTACHMENT, content));
    }

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    T withDependencies(ArtifactList dependencies);

    /**
     * Returns a copy of this artifact with the given descriptor
     *
     * @param descriptor The new descriptor
     * @return The new artifact
     */
    T withDescriptor(ArtifactDescriptor descriptor);

    /**
     * Returns a copy of this library with the given Javadoc attachment
     *
     * @param jar The Javadoc content
     * @return The new library
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T withJar(ArtifactContent jar)
    {
        return copy().withAttachment(attachment(JAR_ATTACHMENT, jar));
    }

    /**
     * Returns a copy of this artifact with the given repository
     *
     * @param repository The repository
     * @return A copy of this artifact in the given repository
     */
    T withRepository(Repository repository);

    /**
     * Returns a copy of this artifact with the given version
     *
     * @param version The new version
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T withVersion(Version version)
    {
        return withDescriptor(descriptor().withVersion(version));
    }

    /**
     * Returns a copy of this artifact with the given version
     *
     * @param version The new version
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default T withVersion(String version)
    {
        return withDescriptor(descriptor().withVersion(version));
    }

    /**
     * Returns this artifact without any attachments
     */
    T withoutAttachments();
}
