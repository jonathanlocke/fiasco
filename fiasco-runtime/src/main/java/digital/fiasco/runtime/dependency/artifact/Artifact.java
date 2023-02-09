package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.string.AsString;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.StringOutputResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContentSignatures;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactName;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactDescriptorList;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;
import digital.fiasco.runtime.dependency.collections.lists.BuilderList;
import digital.fiasco.runtime.repository.Repository;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_TYPE;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachment.attachment;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.JAR_ATTACHMENT;
import static digital.fiasco.runtime.dependency.collections.lists.ArtifactDescriptorList.descriptors;
import static digital.fiasco.runtime.dependency.collections.lists.ArtifactList.artifacts;

/**
 * Defines an artifact, either an {@link Asset} or a {@link Library}. Libraries are artifacts that have a source and
 * Javadoc content attachment as well as a JAR attachment.
 *
 * <p><b>Artifact Descriptors</b></p>
 *
 * <p>
 * Artifacts are referenced by {@link ArtifactDescriptor}s, which take the form
 * <b>[type]:[group]:[artifact]:[version]</b>, for example, "library:com.telenav.kivakit:kivakit-application:1.10.0".
 * The descriptor for an artifact can be retrieved with {@link #descriptor()}.
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
 * An artifact can have zero or more dependencies. The method {@link #dependencies()} ()} returns an
 * {@link ArtifactList}, while the method {@link #builderDependencies()} returns a {@link BuilderList}. Artifact
 * dependencies can be filtered by excluding certain descriptors with {@link #excluding(String...)}, or
 * {@link #excluding(ArtifactDescriptor...)}.
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
 *     <li>{@link #withDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withArtifactName(String)}</li>
 *     <li>{@link #withVersion(Version)}</li>
 * </ul>
 *
 * <p><b>Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies()}</li>
 *     <li>{@link #builderDependencies()}</li>
 *     <li>{@link #isExcluded(ArtifactDescriptor)}</li>
 *     <li>{@link #withDependencies(ArtifactList)}</li>
 *     <li>{@link #excluding(ArtifactDescriptor...)}</li>
 *     <li>{@link #excluding(String...)}</li>
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
 *     <li>{@link #withAttachment(ArtifactAttachment)} - Attaches the given content</li>
 *     <li>{@link #withContent(ArtifactContent)}</li>
 *     <li>{@link #withDependencies(ArtifactList)} - Returns this artifact with the given dependencies</li>
 *     <li>{@link #withDescriptor(ArtifactDescriptor)} - Returns this artifact with the given descriptor</li>
 *     <li>{@link #withArtifactName(String)} - Returns this artifact with the given name</li>
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
public interface Artifact<A extends Artifact<A>> extends
    Dependency,
    AsString
{
    /**
     * Returns an artifact for a given JSON string
     *
     * @param json The JSON
     * @return The artifact
     */
    @SuppressWarnings("unchecked")
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    static <A extends Artifact<A>> A artifactFromJson(String json)
    {
        var serialized = new StringResource(json);
        var serializer = new GsonObjectSerializer();
        return (A) serializer.readObject(serialized, METADATA_OBJECT_TYPE).object();
    }

    /**
     * Returns a copy of this artifact with the given name
     *
     * @param artifactName The new artifact name
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default A artifact(String artifactName)
    {
        return withArtifactName(artifactName);
    }

    /**
     * Returns a copy of this artifact with the given name
     *
     * @param artifact The new artifact name
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default A artifact(ArtifactName artifact)
    {
        return withArtifactName(artifact);
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
    A copy();

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
     * Returns the descriptor for this artifact
     *
     * @return The artifact descriptor
     */
    @Override
    ArtifactDescriptor descriptor();

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclusions The descriptors to exclude
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default A excluding(ArtifactDescriptor... exclusions)
    {
        return excluding(descriptors(list(exclusions)));
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclusions The descriptors to exclude
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default A excluding(String... exclusions)
    {
        return excluding(descriptors(exclusions));
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclusions The descriptors to exclude
     * @return The new artifact
     */
    A excluding(ArtifactDescriptorList exclusions);

    /**
     * Returns a copy of this artifact that excludes the given artifacts from its dependencies
     *
     * @param exclusions The artifacts to exclude
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default A excluding(Artifact<?>... exclusions)
    {
        return excluding(artifacts(exclusions));
    }

    /**
     * Returns a copy of this artifact that excludes the given artifacts from its dependencies
     *
     * @param exclusions The artifacts to exclude
     * @return The new artifact
     */
    default A excluding(ArtifactList exclusions)
    {
        return excluding(exclusions.asDescriptors());
    }

    /**
     * Returns a copy of this artifact that excludes all descriptors matching the given pattern from its dependencies
     *
     * @param pattern The pattern to exclude
     * @return The new artifact
     */
    default A excluding(ArtifactDescriptor pattern)
    {
        return excluding(descriptors(pattern).asArtifacts());
    }

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
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default String toJson()
    {
        var serializer = new GsonObjectSerializer();
        var serialized = new StringOutputResource();
        serializer.writeObject(serialized, new SerializableObject<>(this), METADATA_OBJECT_TYPE);
        return serialized.string();
    }

    /**
     * Returns a copy of this artifact with the given version
     *
     * @param version The new version
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default A version(String version)
    {
        return withVersion(version);
    }

    /**
     * Returns a copy of this artifact with the given name
     *
     * @param artifactName The new artifact name
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default A withArtifactName(String artifactName)
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
    default A withArtifactName(ArtifactName artifactName)
    {
        return withDescriptor(descriptor().withArtifact(artifactName));
    }

    /**
     * Attaches the given artifact attachment to this artifact
     *
     * @param attachment The content to attach
     */
    A withAttachment(ArtifactAttachment attachment);

    /**
     * Attaches the resource for the given artifact type, such as <i>.jar</i>
     *
     * @param attachments The content to attach
     */
    A withAttachments(ObjectMap<ArtifactAttachmentType, ArtifactAttachment> attachments);

    /**
     * Returns primary content attachment for this asset
     *
     * @return The content
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default A withContent(ArtifactContent content)
    {
        return copy().withAttachment(attachment(JAR_ATTACHMENT, content));
    }

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    <D extends Artifact<D>> A withDependencies(D[] dependencies);

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    A withDependencies(ArtifactList dependencies);

    /**
     * Returns a copy of this artifact with the given descriptor
     *
     * @param descriptor The new descriptor
     * @return The new artifact
     */
    A withDescriptor(ArtifactDescriptor descriptor);

    /**
     * Returns a copy of this library with the given Javadoc attachment
     *
     * @param jar The Javadoc content
     * @return The new library
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default A withJar(ArtifactContent jar)
    {
        return copy().withAttachment(attachment(JAR_ATTACHMENT, jar));
    }

    /**
     * Returns a copy of this artifact with the given repository
     *
     * @param repository The repository
     * @return A copy of this artifact in the given repository
     */
    A withRepository(Repository repository);

    /**
     * Returns a copy of this artifact with the given version
     *
     * @param version The new version
     * @return The new artifact
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    default A withVersion(Version version)
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
    default A withVersion(String version)
    {
        return withDescriptor(descriptor().withVersion(version));
    }

    /**
     * Returns this artifact without any attachments
     */
    A withoutAttachments();
}
