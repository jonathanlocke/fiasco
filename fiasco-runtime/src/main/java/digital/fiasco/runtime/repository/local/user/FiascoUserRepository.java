package digital.fiasco.runtime.repository.local.user;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.object.Lazy;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.registry.Register;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.resources.StringResource;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContentSignatures;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptorList;
import digital.fiasco.runtime.dependency.collections.ArtifactList;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.RepositoryContentReader;
import digital.fiasco.runtime.repository.local.cache.FiascoCacheRepository;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.object.Lazy.lazy;
import static com.telenav.kivakit.core.string.AsciiArt.repeat;
import static com.telenav.kivakit.filesystem.Folder.folder;
import static com.telenav.kivakit.resource.WriteMode.APPEND;
import static com.telenav.kivakit.resource.WriteMode.OVERWRITE;
import static digital.fiasco.runtime.FiascoRuntime.fiascoCacheFolder;
import static digital.fiasco.runtime.dependency.artifact.Artifact.artifactFromJson;
import static digital.fiasco.runtime.dependency.collections.ArtifactList.artifacts;
import static digital.fiasco.runtime.repository.Repository.InstallationResult.ALREADY_INSTALLED;
import static digital.fiasco.runtime.repository.Repository.InstallationResult.INSTALLATION_FAILED;
import static digital.fiasco.runtime.repository.Repository.InstallationResult.INSTALLED;

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
 *     <li>{@link Repository#resolveArtifacts(ArtifactDescriptorList, ProgressReporter, RepositoryContentReader)} - Resolves the given descriptors to a list of {@link Artifact}s, complete with {@link ArtifactContent} attachments</li>
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
 * @see FiascoCacheRepository
 */
@SuppressWarnings({ "unused", "SameParameterValue" })
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
@Register
public class FiascoUserRepository extends BaseRepository
{
    /** Separator to use between artifact entries in the artifacts.txt file */
    private final String ARTIFACT_SEPARATOR = repeat(40, "-");

    /** The root folder of this repository */
    private final Folder rootFolder;

    /** The file for storing all artifact metadata in JSON format */
    private final File metadataFile;

    /** The append-only download cache repository */
    private final Lazy<FiascoCacheRepository> cacheRepository = lazy(() -> new FiascoCacheRepository("cache-repository"));

    /**
     * Creates a local Fiasco repository in the given folder
     *
     * @param name The name of the repository
     * @param uri The uri of the folder for this repository
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public FiascoUserRepository(@NotNull String name, @NotNull URI uri)
    {
        super(name, uri);
        this.rootFolder = folder(uri);
        metadataFile = repositoryFile("artifacts.txt");
    }

    /**
     * Creates a local Fiasco repository in the given folder
     *
     * @param name The name of the repository
     * @param rootFolder The root folder of the repository
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public FiascoUserRepository(@NotNull String name, @NotNull Folder rootFolder)
    {
        this(name, rootFolder.mkdirs().asUri());
    }

    /**
     * Creates a repository in the Fiasco cache folder with the given name
     *
     * @param name The name of the repository
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public FiascoUserRepository(@NotNull String name)
    {
        this(name, fiascoCacheFolder()
            .folder(name)
            .mkdirs()
            .ensureExists());
    }

    @Override
    public FiascoUserRepository clear()
    {
        super.clear();
        metadataFile.delete();
        rootFolder.clearAllAndDelete();
        return this;
    }

    /**
     * {@inheritDoc}
     * <p><b>Steps</b></p>
     * <ol>
     *     <li>Adds the {@link Artifact} metadata to artifacts.txt in JSON format</li>
     *     <li>Resolves the artifact in the repository</li>
     *     <li>Saves the artifact's content by calling {@link #saveAttachment(ArtifactAttachment)}</li>
     * </ol>
     *
     * @param artifact The artifact to install
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public InstallationResult installArtifact(Artifact<?> artifact)
    {
        return lock().write(() ->
        {
            // If we don't already have this artifact installed,
            if (!contains(artifact))
            {
                try
                {
                    // append each attachment to the attachments file,
                    var source = artifact;
                    for (var attachment : source.attachments())
                    {
                        source = source.withAttachment(saveAttachment(attachment));
                    }

                    // save metadata,
                    saveArtifactMetadata(source);

                    // and add the artifact to the map.
                    add(artifact.descriptor(), artifact);

                    trace("Installed $ in $", artifact, name());
                    return INSTALLED;
                }
                catch (Exception e)
                {
                    problem(e, "Unable to install artifact: $", artifact);
                    return INSTALLATION_FAILED;
                }
            }
            return ALREADY_INSTALLED;
        });
    }

    /**
     * Gets the artifacts for the given artifact descriptors
     *
     * @param descriptors The artifact descriptors
     * @return The artifacts
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactList resolveArtifacts(ArtifactDescriptorList descriptors,
                                         ProgressReporter reporter,
                                         RepositoryContentReader reader)
    {
        return lock().read(() ->
        {
            // Find the artifacts that are in this repository,
            var resolvedArtifacts = resolve(descriptors);
            var resolvedDescriptors = resolvedArtifacts.asDescriptors();

            // and those that are not added yet.
            var unresolvedDescriptors = descriptors.without(resolvedDescriptors::contains);

            // Install and resolve any unresolved artifacts that are in the downloads cache.
            if (!(this instanceof FiascoCacheRepository))
            {
                var downloadedArtifacts = cacheRepository.get().resolveArtifacts(unresolvedDescriptors, reporter, reader);
                downloadedArtifacts.forEach(this::installArtifact);
                resolvedArtifacts = resolvedArtifacts.with(downloadedArtifacts);
            }

            // Return the resolved artifacts with their content attached.
            return artifacts(resolvedArtifacts.map(this::loadAttachments));
        });
    }

    /**
     * Loads the metadata in <i>artifacts.txt</i> into the artifacts map
     */
    @Override
    protected void loadAllArtifactMetadata()
    {
        if (metadataFile.exists())
        {
            // Read the file,
            var text = metadataFile.reader().readText();

            // split it into chunks,
            for (var at : text.split(ARTIFACT_SEPARATOR))
            {
                // convert the chunk to a cache entry,
                var entry = artifactFromJson(at);

                // and put the entry into the entries map.
                add(entry.descriptor(), entry);
            }
        }
    }

    /**
     * Resolves an artifact's attachments by reading their content
     *
     * @param artifact The artifact
     * @return The artifact with attachments populated with content
     */
    protected Artifact<?> loadAttachments(Artifact<?> artifact)
    {
        var attachments = list(artifact.attachments());
        artifact = artifact.withoutAttachments();
        for (var attachment : attachments)
        {
            attachment = attachment.withArtifact(artifact);
            attachment = attachment.withContent(attachment.content()
                .withResource(artifactAttachmentFile(attachment))
                .withSignatures(readSignatures(attachment)));

            artifact = artifact.withAttachment(attachment);
        }
        return artifact;
    }

    /**
     * Returns a cache file for the given name
     *
     * @param name The name of the file
     * @return The file
     */
    protected File repositoryFile(String name)
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
        var text = artifact.toYaml().toString();

        // add a separator if necessary,
        if (metadataFile.exists())
        {
            text = ARTIFACT_SEPARATOR + "\n" + text.trim();
        }

        // then append the JSON to the metadata file.
        new StringResource(text + "\n").copyTo(metadataFile, APPEND);
    }

    /**
     * Saves the given attachment into this repository. {@link FiascoUserRepository} stores the attachment in the file
     * returned by {@link #artifactAttachmentFile(ArtifactAttachment)}.
     */
    protected ArtifactAttachment saveAttachment(ArtifactAttachment attachment)
    {
        var resource = attachment.content().resource();
        resource.safeCopyTo(artifactAttachmentFile(attachment), OVERWRITE);
        return attachment;
    }

    /**
     * Returns the file where a given artifact attachment is stored
     *
     * @param attachment The attachment
     * @return A file in the repository folder hierarchy for the given attachment
     */
    private File artifactAttachmentFile(@NotNull ArtifactAttachment attachment)
    {
        var artifact = attachment.artifact();
        var descriptor = artifact.descriptor();
        var file = descriptor.artifactName() + "-" + descriptor.version() + attachment.attachmentType().fileSuffix();
        return repositoryFolder(artifact).file(file);
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
        var file = repositoryFolder(artifact).file(content.name() + "." + algorithm);
        return file.exists() ? file.readText() : null;
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
        var asc = readSignature(artifact, content, "asc");
        var md5 = readSignature(artifact, content, "md5");
        var sha1 = readSignature(artifact, content, "sha1");
        return new ArtifactContentSignatures(asc, md5, sha1);
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
        return rootFolder.folder(descriptor.group().name()
            .replaceAll("\\.", "/")
            + "/" + descriptor.artifactName()
            + "/" + descriptor.version()).mkdirs();
    }
}
