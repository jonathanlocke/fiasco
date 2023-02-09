package digital.fiasco.runtime.repository.local.cache;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.resources.InputResource;
import com.telenav.kivakit.resource.resources.ResourceSection;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptorList;
import digital.fiasco.runtime.dependency.collections.ArtifactList;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.RepositoryContentReader;
import digital.fiasco.runtime.repository.local.LocalRepository;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.filesystem.Folder.folder;
import static com.telenav.kivakit.resource.WriteMode.APPEND;
import static digital.fiasco.runtime.FiascoRuntime.fiascoCacheFolder;

/**
 * A high-performance repository of artifacts and their metadata.
 *
 * <p><b>Uses</b></p>
 *
 * <p>
 * An instance of {@link CacheRepository} is used as an artifact cache to avoid unnecessary downloads when a user wipes
 * out their {@link LocalRepository}, causing it to repopulate. Instead of repopulating from Maven Central or another
 * remote repository, the artifacts in this repository can be used since artifacts and their metadata are never altered,
 * only appended to their respective <i>artifacts.txt</i> and <i>artifact-content.binary</i>files. Because remote
 * artifacts are guaranteed by Maven Central (and other remote repositories) to be immutable, it should rarely be
 * necessary to remove a download cache repository.
 * </p>
 *
 * <p>
 * Another instance of this repository is used by FiascoServer to respond quickly to requests to resolve one or more
 * artifact descriptors.
 * </p>
 *
 * <p><b>Content Storage</b></p>
 *
 * <p>
 * This class inherits metadata storage from {@link LocalRepository}, but instead of storing content in a folder tree,
 * {@link CacheRepository} stores content end-to-end in a single, randomly-accessed binary file to increase performance.
 * The metadata for an artifact includes the offset and size of each content attachment in the binary content file.
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
 *     <li>{@link Repository#resolveArtifacts(ArtifactDescriptorList, ProgressReporter, RepositoryContentReader)}  - Resolves the given descriptors to a list of {@link Artifact}s, complete with {@link ArtifactContent} attachments</li>
 * </ul>
 *
 * <p><b>Installing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #installArtifact(Artifact)} - Adds the given artifact with the given attached resources</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class CacheRepository extends LocalRepository
{
    /** The binary file containing artifacts, laid out end-to-end */
    private final File artifactContentFile = repositoryFile("artifact-content.binary");

    /**
     * Creates a cache repository in the Fiasco cache folder
     *
     * @param name The name of the repository
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public CacheRepository(String name)
    {
        super(name, fiascoCacheFolder().folder(name));
    }

    /**
     * Creates a local Fiasco repository in the given folder
     *
     * @param name The name of the repository
     * @param uri The uri of the folder for this repository
     */
    public CacheRepository(@NotNull String name, @NotNull URI uri)
    {
        super(name, folder(uri));
    }

    /**
     * Creates a local Fiasco repository in the given folder
     *
     * @param name The name of the repository
     * @param folder The folder for this repository
     */
    public CacheRepository(@NotNull String name, @NotNull Folder folder)
    {
        super(name, folder);
    }

    @Override
    public ArtifactList resolveArtifacts(ArtifactDescriptorList descriptors,
                                         ProgressReporter reporter,
                                         RepositoryContentReader reader)
    {
        // Resolve artifacts and append them to the artifact content file.
        return super.resolveArtifacts(descriptors, reporter, (in, length) ->
            artifactContentFile.copyFrom(new InputResource(in), APPEND, reporter.steps(length)));
    }

    /**
     * Resolves an artifact's attachments by reading their content
     *
     * @param artifact The artifact
     * @return The artifact with attachments populated with content
     */
    @Override
    protected Artifact<?> loadAttachments(Artifact<?> artifact)
    {
        for (var attachment : artifact.attachments())
        {
            var content = attachment.content();
            var start = content.offset();
            var end = start + content.size().asBytes();

            artifact = artifact.withAttachment(attachment
                .withContent(attachment.content()
                    .withResource(new ResourceSection(artifactContentFile, start, end))));
        }
        return artifact;
    }

    /**
     * Saves the given content attachment into the omnibus content file, returning the given {@link ArtifactContent}
     * with the offset, size, and last modified time populated.
     *
     * @param attachment The artifact attachment to append to the attachments file
     * @throws IllegalStateException Thrown if the content cannot be attached
     */
    @Override
    protected ArtifactAttachment saveAttachment(ArtifactAttachment attachment)
    {
        var content = attachment.content();

        try
        {
            // Get the start of this content in the attachments file,
            var start = artifactContentFile.sizeInBytes().asLong();

            // get the size of the content and its time of last modification
            var size = content.resource().sizeInBytes();
            var lastModified = content.lastModified();

            // append the content to the attachments file,
            content.resource().copyTo(artifactContentFile, APPEND);

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
