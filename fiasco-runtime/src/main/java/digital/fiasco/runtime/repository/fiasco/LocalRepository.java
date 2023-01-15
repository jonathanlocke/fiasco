package digital.fiasco.runtime.repository.fiasco;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.resources.StringResource;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactContentSignatures;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.Repository;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.filesystem.Folder.folder;
import static com.telenav.kivakit.resource.WriteMode.APPEND;
import static digital.fiasco.runtime.FiascoRuntime.fiascoCacheFolder;
import static digital.fiasco.runtime.dependency.artifact.Artifact.artifactFromJson;

/**
 * A repository of artifacts and their metadata on the local filesystem.
 *
 * <p>
 * All artifact metadata is stored in a single human-readable text file called <i>artifacts.txt</i> at the root of the
 * repository. This file is loaded into memory for fast resolution of artifacts. Artifact content attachments are stored
 * in a Maven-like folder hierarchy based on the artifact descriptor. For example, the artifact attachments for
 * <i>com.telenav.kivakit:kivakit-application:1.11.0</i> would be stored in
 * <i>com/telenav/kivakit/kivakit-application/1.11.0</i>.
 * </p>
 *
 * <p><b>Artifact Metadata</b></p>
 *
 * <p>
 * The artifact metadata stored in <i>artifacts.txt</i> is in JSON format, separated by <i>========</i> bars. Since the
 * metadata in this file is relatively small, it is loaded into a memory index, where it can be used to quickly locate
 * artifact metadata and content attachments, including library, Javadoc, and source code JARs.
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
 *     <li>{@link Repository#resolveArtifacts(ObjectList)} - Resolves the given descriptors to a list of {@link Artifact}s, complete with {@link ArtifactContent} attachments</li>
 * </ul>
 *
 * <p><b>Installing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #installArtifact(Artifact)} - Installs the given artifact in this repository, along with its attached resources</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see BaseRepository
 * @see Repository
 * @see CacheRepository
 */
@SuppressWarnings({ "unused", "SameParameterValue" })
public class LocalRepository extends BaseRepository
{
    /** Separator to use between artifact entries in the artifacts.txt file */
    private final String ARTIFACT_SEPARATOR = "\n========\n";

    /** The root folder of this repository */
    private final Folder rootFolder;

    /** The file for storing all artifact metadata in JSON format */
    private final File metadataFile;

    /** The append-only download cache repository */
    private final CacheRepository downloads = new CacheRepository("download-cache-repository");

    /**
     * Creates a local Fiasco repository in the given folder
     *
     * @param name The name of the repository
     * @param uri The uri of the folder for this repository
     */
    public LocalRepository(@NotNull String name, @NotNull URI uri)
    {
        super(name, uri);
        this.rootFolder = folder(uri);
        metadataFile = repositoryRootFile("artifacts.txt");
        loadAllArtifactMetadata();
    }

    /**
     * Creates a local Fiasco repository in the given folder
     *
     * @param name The name of the repository
     * @param rootFolder The root folder of the repository
     */
    public LocalRepository(@NotNull String name, @NotNull Folder rootFolder)
    {
        this(name, rootFolder.uri());
    }

    /**
     * Creates a repository in the Fiasco cache folder with the given name
     *
     * @param name The name of the repository
     */
    public LocalRepository(@NotNull String name)
    {
        this(name, fiascoCacheFolder()
            .folder(name)
            .mkdirs()
            .ensureExists());
    }

    /**
     * Adds the given content is stored in the repository in a folder hierarchy, and the {@link Artifact} artifact to
     * artifacts.txt in JSON format.
     *
     * @param artifact The artifact to install
     */
    @Override
    public void installArtifact(Artifact<?> artifact)
    {
        lock().write(() ->
        {
            saveArtifactMetadata(artifact);
            artifact.attachments().forEach(attachment ->
                attachment.content()
                    .resource()
                    .safeCopyTo(artifactAttachmentFile(attachment), APPEND));
        });
    }

    /**
     * Gets the artifacts for the given artifact descriptors
     *
     * @param descriptors The artifact descriptors
     * @return The artifacts
     */
    @Override
    public final ArtifactList resolveArtifacts(ObjectList<ArtifactDescriptor> descriptors)
    {
        return lock().read(() ->
        {
            // Find the artifacts that are in this repository,
            var resolvedArtifacts = resolve(descriptors);
            var resolvedDescriptors = resolvedArtifacts.asArtifactDescriptors();

            // and those that are not.
            var unresolvedDescriptors = descriptors.without(resolvedDescriptors::contains);

            // Install and resolve any unresolved artifacts that are in the downloads cache.
            var downloadedArtifacts = downloads.resolveArtifacts(unresolvedDescriptors);
            downloadedArtifacts.forEach(this::installArtifact);
            resolvedArtifacts = resolvedArtifacts.with(downloadedArtifacts);

            // Return the resolved artifacts with their content attached.
            resolvedArtifacts.forEach(this::loadArtifactContent);
            return resolvedArtifacts;
        });
    }

    /**
     * Resolves an artifact's attachments by reading their content
     *
     * @param artifact The artifact
     * @return The artifact with attachments populated with content
     */
    protected Artifact<?> loadArtifactContent(Artifact<?> artifact)
    {
        for (var attachment : artifact.attachments())
        {
            artifact = artifact.withAttachment(attachment
                .withContent(attachment.content()
                    .withResource(artifactAttachmentFile(attachment))
                    .withSignatures(readSignatures(attachment))));
        }
        return artifact;
    }

    /**
     * Returns a cache file for the given name
     *
     * @param name The name of the file
     * @return The file
     */
    protected File repositoryRootFile(String name)
    {
        return ensureNotNull(rootFolder).file(name);
    }

    /**
     * Adds artifact metadata to metadata.txt file
     *
     * @param artifact The artifact to add
     */
    protected void saveArtifactMetadata(Artifact<?> artifact)
    {
        // Get JSON for artifact metadata,
        var text = artifact.toJson();

        // add a separator if necessary,
        if (metadataFile.exists())
        {
            text = ARTIFACT_SEPARATOR + text;
        }

        // then append the JSON to the metadata file.
        new StringResource(text).copyTo(metadataFile, APPEND);
    }

    /**
     * Returns the file where a given artifact attachment is stored
     *
     * @param attachment The attachment
     * @return A file in the repository folder hierarchy for the given attachment
     */
    private File artifactAttachmentFile(@NotNull ArtifactAttachment attachment)
    {
        return repositoryFolder(attachment.artifact())
            .file(attachment.content().name() + attachment.attachmentType());
    }

    /**
     * Loads the metadata in <i>artifacts.txt</i> into the artifacts map
     */
    private void loadAllArtifactMetadata()
    {
        lock().write(() ->
        {
            // Read the file,
            var text = metadataFile.reader().readText();

            // split it into chunks,
            for (var at : text.split(ARTIFACT_SEPARATOR))
            {
                // convert the chunk to a cache entry,
                var entry = artifactFromJson(at);

                // and put the entry into the entries map.
                artifacts().put(entry.descriptor(), entry);
            }
        });
    }

    /**
     * Returns the signature for a given artifact's content and signature algorithm
     *
     * @param artifact The artifact
     * @param content The content attachment of the artifact
     * @param algorithm The signing algorithm
     * @return The signature
     */
    private String readSignature(Artifact<?> artifact, ArtifactContent content, String algorithm)
    {
        return repositoryFolder(artifact).file(content.name() + "." + algorithm).readText();
    }

    /**
     * Returns the {@link ArtifactContentSignatures} for the given artifact and content attachment.
     *
     * @param attachment The artifact attachment
     * @return The signatures
     */
    private ArtifactContentSignatures readSignatures(ArtifactAttachment attachment)
    {
        var artifact = attachment.artifact();
        var content = attachment.content();
        var pgp = readSignature(artifact, content, "pgp");
        var md5 = readSignature(artifact, content, "md5");
        var sha1 = readSignature(artifact, content, "sha1");
        return new ArtifactContentSignatures(pgp, md5, sha1);
    }

    /**
     * Returns the folder for an artifact. The path scheme is similar to Maven repositories:
     *
     * <pre>com/telenav/kivakit/kivakit-application-1.9.0/</pre>
     *
     * @param artifact The artifact
     * @return The folder
     */
    private Folder repositoryFolder(Artifact<?> artifact)
    {
        var descriptor = artifact.descriptor();
        return rootFolder.folder(descriptor.group().name().replaceAll("\\.", "/") + "/" + descriptor.artifact() + "-" + descriptor.version());
    }
}
