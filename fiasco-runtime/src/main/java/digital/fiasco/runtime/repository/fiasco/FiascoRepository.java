package digital.fiasco.runtime.repository.fiasco;

import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.ResourceSection;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachments;
import digital.fiasco.runtime.dependency.artifact.ArtifactContentMetadata;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.Asset;
import digital.fiasco.runtime.dependency.artifact.Library;
import digital.fiasco.runtime.repository.BaseRepository;

import java.net.URI;
import java.nio.file.Files;

import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static digital.fiasco.runtime.FiascoRuntime.fiascoCacheFolder;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachments.JAR_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachments.JAVADOC_JAR_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachments.SOURCES_JAR_SUFFIX;
import static java.nio.file.Files.writeString;
import static java.nio.file.StandardOpenOption.APPEND;

/**
 * A high performance repository of artifacts and their metadata.
 *
 * <p><b>User Repositories</b></p>
 *
 * <p>
 * This repository is used to store artifacts and metadata for Fiasco users. The repository metadata is in a
 * human-readable text file called <i>artifacts.txt</i>. The metadata can be searched with grep or viewed in a text
 * editor.
 * </p>
 *
 * <p><b>Maven Download Cache</b></p>
 *
 * <p>
 * An instance of this repository is used as a download cache to avoid unnecessary downloads when a user wipes out their
 * repository, causing it to repopulate. Instead of repopulating from Maven Central or another remote repository, the
 * artifacts in this cache can be used. Because downloaded artifacts are not mutable in Maven Central (and should not be
 * mutable in any other repository), it should rarely be necessary to remove the download repository.
 * </p>
 *
 * <p><b>Artifact Storage</b></p>
 *
 * <p>
 * Artifacts are stored in this repository in two separate files, <i>artifacts.txt</i> containing metdata about each
 * stored artifact, and <i>attachents.binary</i> containing the concatenated binary content of artifact attachments.
 * Metadata is in text format and separated with <i>========</i> bars. Since the metadata is relatively small, it is
 * loaded into memory and used to locate {@link ResourceSection}s in the attachment file for each content attachment to
 * an artifact.
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
 *     <li>{@link #resolve(ArtifactDescriptor)} - Gets the {@link Artifact} for the given descriptor</li>
 *     <li>{@link #content(Artifact, ArtifactContentMetadata, String)} - Gets the cached resource for the given artifact and content metadata</li>
 * </ul>
 *
 * <p><b>Adding and Removing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #install(Artifact, ArtifactAttachments)} - Adds the given artifact with the given attached resources</li>
 *     <li>{@link #clear()} - Removes all data from this repository</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class FiascoRepository extends BaseRepository
{
    /** The cached artifact entries */
    private final ObjectMap<ArtifactDescriptor, Artifact<?>> artifacts = new ObjectMap<>();

    /** Separator to use between artifact entries in the artifacts.txt file */
    private final String ARTIFACT_SEPARATOR = "\n========\n";

    private Folder rootFolder;

    /** The file for storing artifact metadata in JSON format */
    private final File metadataFile = cacheFile("artifacts.txt");

    /** The binary file containing artifacts, laid out end-to-end */
    private final File attachmentsFile = cacheFile("attachments.binary");

    public FiascoRepository(String name, Folder rootFolder)
    {
        super(name, rootFolder.uri());
        loadMetadata();
    }

    public FiascoRepository(String name)
    {
        this(name, fiascoCacheFolder().folder(name));
    }

    /**
     * Removes all data from this repository
     */
    @Override
    public synchronized void clear()
    {
        metadataFile.delete();
        attachmentsFile.delete();
    }

    /**
     * Returns the section of the binary resources file containing the given artifact
     *
     * @param ignored1 The download repository does not require the artifact
     * @param content The artifact content, including its offset and size
     * @param ignored2 The download repository does not require an attachment suffix
     * @return The resource section for the artifact's content in content.bin
     */
    @Override
    public synchronized Resource content(Artifact<?> ignored1,
                                         ArtifactContentMetadata content,
                                         String ignored2)
    {
        var start = content.offset();
        var end = start + content.size().asBytes();
        return new ResourceSection(attachmentsFile, start, end);
    }

    /**
     * Adds the given content {@link Resource}s to content.bin, and the {@link Artifact} artifact to artifacts.txt in
     * JSON format.
     *
     * @param artifact The artifact to append to artifacts.txt in JSON format
     * @param resources The resources to add to content.bin
     */
    @Override
    public synchronized boolean install(Artifact<?> artifact, ArtifactAttachments resources)
    {
        try
        {
            // If the artifact is a library
            if (artifact instanceof Library library)
            {
                // append the binary, source, and Javadoc JARs to the attachments file, and attach
                // the metadata for each to the library.
                artifact = library
                        .withJar(appendArtifactContent(library.jar(), resources.attachment(JAR_SUFFIX)))
                        .withJavadoc(appendArtifactContent(library.javadoc(), resources.attachment(JAVADOC_JAR_SUFFIX)))
                        .withSource(appendArtifactContent(library.source(), resources.attachment(SOURCES_JAR_SUFFIX)));
            }

            // If the artifact is an asset
            if (artifact instanceof Asset asset)
            {
                // append the asset JAR to the attachments file and attach the metadata to the asset.
                artifact = asset.withJar(appendArtifactContent(asset.jar(), resources.attachment(JAR_SUFFIX)));
            }

            // Store the artifact in the artifacts map by its descriptor.
            artifacts.put(artifact.descriptor(), artifact);

            // If the artifact file is missing or empty,
            var path = metadataFile.asJavaPath();
            if (!Files.exists(path) || Files.size(path) > 0)
            {
                // write out a separator to make the file easy to read,
                writeString(path, ARTIFACT_SEPARATOR);
            }

            // then append the cache entry in JSON to the end of the file.
            writeString(path, artifact.toJson(), APPEND);
            return true;
        }
        catch (Exception e)
        {
            problem(e, "Unable to add cache entry: $", artifact);
            return false;
        }
    }

    /**
     * Gets the cache entry for the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The cache entry for the descriptor
     */
    @Override
    public synchronized Artifact<?> resolve(ArtifactDescriptor descriptor)
    {
        return artifacts.get(descriptor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI uri()
    {
        return rootFolder.uri();
    }

    /**
     * Saves the given content into the content file, returning the given {@link ArtifactContentMetadata} with the
     * offset, size, and last modified time populated.
     *
     * @param artifact The artifact to save
     * @param content The artifact content to write to the resources file
     * @return The updated artifact content
     */
    private ArtifactContentMetadata appendArtifactContent(ArtifactContentMetadata artifact, Resource content)
    {
        try
        {
            // Get path to resources file,
            var path = attachmentsFile.asJavaPath();

            // make a note of where we are going to start writing,
            var start = Files.size(path);

            // get the content last modified time,
            var lastModified = content.lastModified();

            // append the content to the resources file,
            Files.write(path, content.reader().readBytes(), APPEND);

            // get the end of the content,
            var end = Files.size(path);

            // update the artifact with content details
            return artifact
                    .withOffset(start)
                    .withLastModified(lastModified)
                    .withSize(bytes(end - start));
        }
        catch (Exception e)
        {
            problem(e, "Unable to add content to $: $", artifact, content);
            return null;
        }
    }

    /**
     * Returns a cache file for the given name
     *
     * @param name The name of the file
     * @return The file
     */
    private File cacheFile(String name)
    {
        return rootFolder.file(name);
    }

    /**
     * Loads the metadata in <i>artifacts.txt</i> into the artifacts map
     */
    private synchronized void loadMetadata()
    {
        // Read the file,
        var text = metadataFile.reader().readText();

        // split it into chunks,
        for (var at : text.split(ARTIFACT_SEPARATOR))
        {
            // convert the chunk to a cache entry,
            var entry = Artifact.fromJson(at);

            // and put the entry into the entries map.
            artifacts.put(entry.descriptor(), entry);
        }
    }
}
