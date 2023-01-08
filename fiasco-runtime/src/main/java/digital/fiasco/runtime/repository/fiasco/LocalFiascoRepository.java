package digital.fiasco.runtime.repository.fiasco;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.resources.StringResource;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactSignatures;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.Repository;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.resource.WriteMode.APPEND;
import static digital.fiasco.runtime.FiascoRuntime.fiascoCacheFolder;
import static digital.fiasco.runtime.dependency.artifact.Artifact.artifactFromJson;

/**
 * A high performance repository of artifacts and their metadata.
 *
 * <p>
 * This repository is used to store artifacts and metadata on the local filesystem for Fiasco users. Artifact metadata
 * is in a human-readable text file called <i>artifacts.txt</i> at the root of the repository, and can be searched with
 * grep or viewed in a text editor. Artifact content attachments are stored in a folder hierarchy.
 * </p>
 *
 * <p><b>Artifact Metadata</b></p>
 *
 * <p>
 * Artifact metadata is stored in an append-only file, <i>artifacts.txt</i>. The metadata for each artifact in this file
 * is separated with <i>========</i> bars. Since the metadata is relatively small, it is loaded into memory, where it
 * can be used to quickly locate artifact content attachment files (library, Javadoc, and source code JARs).
 * </p>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #uri()}</li>
 * </ul>
 *
 * <p><b>Retrieving Artifacts and Content</b></p>
 *
 * <ul>
 *     <li>{@link #resolveArtifacts(Collection)} - Gets the {@link Artifact} for the given descriptor</li>
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
 * @see BaseRepository
 * @see Repository
 * @see CacheFiascoRepository
 */
@SuppressWarnings({ "unused", "SameParameterValue" })
public class LocalFiascoRepository extends BaseRepository
{
    /** Separator to use between artifact entries in the artifacts.txt file */
    private final String ARTIFACT_SEPARATOR = "\n========\n";

    /** The root folder of this repository */
    private final Folder rootFolder;

    /** The file for storing all artifact metadata in JSON format */
    private final File metadataFile;

    /**
     * Creates a local Fiasco repository in the given folder
     *
     * @param name The name of the repository
     * @param rootFolder The root folder of the repository
     */
    public LocalFiascoRepository(@NotNull String name, @NotNull Folder rootFolder)
    {
        super(name, rootFolder.uri());
        this.rootFolder = rootFolder;
        metadataFile = repositoryRootFile("artifacts.txt");
        loadAllArtifactMetadata();
    }

    /**
     * Creates a repository in the Fiasco cache folder with the given name
     *
     * @param name The name of the repository
     */
    public LocalFiascoRepository(@NotNull String name)
    {
        this(name, fiascoCacheFolder()
            .folder(name)
            .mkdirs()
            .ensureExists());
    }

    /**
     * Removes all data from this repository
     */
    @Override
    public void clearArtifacts()
    {
        lock().write(() -> ensure(rootFolder.delete()));
    }

    /**
     * Adds the given content is stored in the repository in a folder hierarchy, and the {@link Artifact} artifact to
     * artifacts.txt in JSON format.
     *
     * @param artifact The artifact to install
     */
    @Override
    public void installArtifact(Artifact artifact)
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
     * {@inheritDoc}
     */
    @Override
    public boolean isRemote()
    {
        return false;
    }

    /**
     * Gets the artifacts for the given artifact descriptors
     *
     * @param descriptors The artifact descriptors
     * @return The artifacts
     */
    @Override
    public final ObjectList<Artifact> resolveArtifacts(Collection<ArtifactDescriptor> descriptors)
    {
        return lock().read(() ->
        {
            ObjectList<Artifact> resolved = list();
            resolve(descriptors).forEach(artifact ->
                resolved.add(loadArtifactContent(artifact)));
            return resolved;
        });
    }

    /**
     * Resolves an artifact's attachments by reading their content
     *
     * @param artifact The artifact
     * @return The artifact with attachments populated with content
     */
    protected Artifact loadArtifactContent(Artifact artifact)
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
    protected void saveArtifactMetadata(Artifact artifact)
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
            .file(attachment.content().name() + attachment.suffix());
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
                artifacts().put(entry.artifactDescriptor(), entry);
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
    private String readSignature(Artifact artifact, ArtifactContent content, String algorithm)
    {
        return repositoryFolder(artifact).file(content.name() + "." + algorithm).readText();
    }

    /**
     * Returns the {@link ArtifactSignatures} for the given artifact and content attachment.
     *
     * @param attachment The artifact attachment
     * @return The signatures
     */
    private ArtifactSignatures readSignatures(ArtifactAttachment attachment)
    {
        var artifact = attachment.artifact();
        var content = attachment.content();
        var pgp = readSignature(artifact, content, "pgp");
        var md5 = readSignature(artifact, content, "md5");
        var sha1 = readSignature(artifact, content, "sha1");
        return new ArtifactSignatures(pgp, md5, sha1);
    }

    /**
     * Returns the folder for an artifact. The path scheme is similar to Maven repositories:
     *
     * <pre>com/telenav/kivakit/kivakit-application-1.9.0/</pre>
     *
     * @param artifact The artifact
     * @return The folder
     */
    private Folder repositoryFolder(Artifact artifact)
    {
        var descriptor = artifact.artifactDescriptor();
        return rootFolder.folder(descriptor.group().name().replaceAll("\\.", "/") + "/" + descriptor.artifact() + "-" + descriptor.version());
    }
}
