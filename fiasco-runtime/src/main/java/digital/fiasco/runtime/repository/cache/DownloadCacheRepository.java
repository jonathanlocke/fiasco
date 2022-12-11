package digital.fiasco.runtime.repository.cache;

import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.ResourceSection;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.artifact.ArtifactContentMetadata;
import digital.fiasco.runtime.repository.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.artifact.ArtifactMetadata;

import java.nio.file.Files;

import static com.telenav.kivakit.filesystem.Folders.userHome;
import static java.nio.file.StandardOpenOption.APPEND;

/**
 * A fast cache of downloaded artifacts and their metadata. This cache helps to avoid unnecessary downloads when a user
 * wipes out their repository, causing it to repopulate. Instead of repopulating from Maven Central or another remote
 * repository, the artifacts in this cache can be used.
 *
 * <p><b>Key Methods</b></p>
 *
 * <ul>
 *     <li>{@link #add(ArtifactMetadata, Resource, Resource, Resource)} - Adds an artifact to this cache, including its metadata, and its jar, javadoc and source attachments</li>
 *     <li>{@link #metadata(ArtifactDescriptor)} - Gets the {@link ArtifactMetadata} for the given descriptor</li>
 *     <li>{@link #content(ArtifactContentMetadata)} - Gets the cached resource for the given content metadata</li>
 * </ul>
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class DownloadCacheRepository extends BaseRepository
{
    /** True if this cache has been loaded */
    private boolean loaded;

    /** The file for storing metadata in JSON format */
    private final File metadataFile = cacheFile("artifact-metadata.txt");

    /** The binary file containing artifacts, laid out end-to-end */
    private final File contentFile = cacheFile("content.bin");

    /** The cached artifact entries */
    private final ObjectMap<ArtifactDescriptor, ArtifactMetadata> artifacts = new ObjectMap<>();

    /** Separator to use between metadata entries in the metadata.txt file */
    private final String SEPARATOR = "\n========\n";

    /**
     * Adds the given content {@link Resource}s to content.bin, and the {@link ArtifactMetadata} metadata to
     * metadata.txt in JSON format.
     *
     * @param metadata The cache entry metadata to append to metadata.txt in JSON format
     * @param jar The jar resource to add to content.bin
     * @param javadoc The javadoc resource to add to content.bin
     * @param source The source resource to add to content.bin
     */
    @Override
    public synchronized boolean add(ArtifactMetadata metadata, Resource jar, Resource javadoc, Resource source)
    {
        try
        {
            // Save resources to artifactsFile and attach cached artifacts to the cache entry.
            metadata = metadata
                    .withJar(appendArtifactContent(metadata.jar(), jar))
                    .withJavadoc(appendArtifactContent(metadata.javadoc(), javadoc))
                    .withSource(appendArtifactContent(metadata.source(), source));

            // If the metadata file is missing or empty,
            var path = metadataFile.asJavaPath();
            if (!Files.exists(path) || Files.size(path) > 0)
            {
                // write out a separator to make the file easy to read,
                Files.writeString(path, SEPARATOR);
            }

            // then append the cache entry in JSON to the end of the file.
            Files.writeString(path, metadata.toJson(), APPEND);
            return true;
        }
        catch (Exception e)
        {
            problem(e, "Unable to add cache entry: $", metadata);
            return false;
        }
    }

    /**
     * Removes all data from this repository
     */
    @Override
    public synchronized void clear()
    {
        metadataFile.delete();
        contentFile.delete();
    }

    /**
     * Returns the section of the binary resources file containing the given artifact
     *
     * @param artifact The artifact metadata, including its offset and size
     * @return The resource section for the artifact
     */
    @Override
    public synchronized Resource content(ArtifactContentMetadata artifact)
    {
        var start = artifact.offset();
        var end = start + artifact.size().asBytes();
        return new ResourceSection(contentFile, start, end);
    }

    /**
     * Gets the cache entry for the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The cache entry for the descriptor
     */
    @Override
    public synchronized ArtifactMetadata metadata(ArtifactDescriptor descriptor)
    {
        load();
        return artifacts.get(descriptor);
    }

    /**
     * Saves the given content into the content file, returning the given {@link ArtifactContentMetadata} with the
     * offset, size, and last modified time populated.
     *
     * @param artifact The artifact to save
     * @param content The artifact content to write to the resources file
     * @return The updated artifact metadata
     */
    private ArtifactContentMetadata appendArtifactContent(ArtifactContentMetadata artifact, Resource content)
    {
        try
        {
            // Get path to resources file,
            var path = contentFile.asJavaPath();

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
                    .withSize(end - start);
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
        return userHome()
                .folder(".fiasco/downloaded-artifacts")
                .file(name);
    }

    /**
     * Loads metadata.txt into the cache entries map
     */
    private synchronized void load()
    {
        if (!loaded)
        {
            // Read the file,
            var text = metadataFile.reader().readText();

            // split it into chunks,
            for (var at : text.split(SEPARATOR))
            {
                // convert the chunk to a cache entry,
                var entry = ArtifactMetadata.fromJson(at);

                // and put the entry into the entries map.
                artifacts.put(entry.descriptor(), entry);
            }
            loaded = true;
        }
    }
}
