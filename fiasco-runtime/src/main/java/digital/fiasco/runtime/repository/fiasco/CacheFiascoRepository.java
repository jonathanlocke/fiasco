package digital.fiasco.runtime.repository.fiasco;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.value.mutable.MutableValue;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.resources.ResourceSection;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;

import java.util.Collection;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.resource.WriteMode.APPEND;
import static digital.fiasco.runtime.FiascoRuntime.fiascoCacheFolder;

/**
 * A high performance repository of artifacts and their metadata.
 *
 * <p>
 * An instance of this repository is used as a download cache to avoid unnecessary downloads when a user wipes out their
 * repository, causing it to repopulate. Instead of repopulating from Maven Central or another remote repository, the
 * artifacts in this cache can be used. Because downloaded artifacts are not mutable in Maven Central (and should not be
 * mutable in any other repository), it should rarely be necessary to remove the download repository.
 * </p>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #name()}</li>
 *     <li>{@link #uri()}</li>
 * </ul>
 *
 * <p><b>Retrieving Artifacts and Content</b></p>
 *
 * <ul>
 *     <li>{@link #resolveArtifacts(Collection)} - Gets the {@link Artifact} for the given descriptor, including its content attachments</li>
 * </ul>
 *
 * <p><b>Adding and Removing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #installArtifact(Artifact)} - Adds the given artifact with the given attached resources</li>
 *     <li>{@link #clearArtifacts()} - Removes all data from this repository</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class CacheFiascoRepository extends LocalFiascoRepository
{
    /** The binary file containing artifacts, laid out end-to-end */
    private final File attachmentsFile = cacheFile("attachments.binary");

    public CacheFiascoRepository(String name, Folder rootFolder)
    {
        super(name, rootFolder);
    }

    public CacheFiascoRepository(String name)
    {
        this(name, fiascoCacheFolder().folder(name));
    }

    /**
     * Adds the given content to the attachments file, and the {@link Artifact} metadata to artifacts.txt in JSON
     * format.
     *
     * @param artifact The artifact to install
     */
    @Override
    public void installArtifact(final Artifact<?> artifact)
    {
        lock().write(() ->
        {
            try
            {
                // Append each attachment to the attachments file,
                var updated = new MutableValue<Artifact<?>>(artifact);
                visitArtifactAttachments(artifact, attachment ->
                    updated.set(updated.get()
                        .withAttachment(appendAttachment(attachment))));

                // then append the updated metadata.
                appendArtifactMetadata(updated.get());
            }
            catch (Exception e)
            {
                problem(e, "Unable to install artifact: $", artifact);
            }
        });
    }

    /**
     * Gets the artifacts for the given artifact descriptors
     *
     * @param descriptors The artifact descriptors
     * @return The artifacts
     */
    @Override
    public ObjectList<Artifact<?>> resolveArtifacts(Collection<ArtifactDescriptor> descriptors)
    {
        return lock().read(() ->
        {
            ObjectList<Artifact<?>> resolved = list();

            // For each artifact attachment,
            visitArtifactAttachments(descriptors, attachment ->
            {
                // get the section of the attachments file for the attachment,
                var content = attachment.content();
                var start = content.offset();
                var end = start + content.size().asBytes();
                var resource = new ResourceSection(attachmentsFile, start, end);

                // and resolve the artifact content.
                resolved.add(attachment.artifact().withAttachment(
                    attachment.withContent(content.withResource(resource))));
            });

            return resolved;
        });
    }

    /**
     * Saves the given content into the content file, returning the given {@link ArtifactContent} with the offset, size,
     * and last modified time populated.
     *
     * @param attachment The artifact attachment to append to the attachments file
     * @throws IllegalStateException Thrown if the content cannot be attached
     */
    private ArtifactAttachment appendAttachment(ArtifactAttachment attachment)
    {
        var content = attachment.content();

        try
        {
            // Get the start of this content in the attachments file,
            var start = attachmentsFile.sizeInBytes().asLong();

            // get the size of the content and its time of last modification
            var size = content.resource().sizeInBytes();
            var lastModified = content.lastModified();

            // append the content to the attachments file,
            content.resource().copyTo(attachmentsFile, APPEND);

            // and return the artifact with its new content information.
            return attachment.withContent(content
                .withOffset(start)
                .withLastModified(lastModified)
                .withSize(size));
        }
        catch (Exception e)
        {
            return illegalState(e, "Unable to attach content: $", content);
        }
    }
}
