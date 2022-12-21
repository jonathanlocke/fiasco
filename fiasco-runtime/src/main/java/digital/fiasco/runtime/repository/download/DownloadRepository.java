package digital.fiasco.runtime.repository.download;

import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.ResourceSection;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactResources;
import digital.fiasco.runtime.dependency.artifact.Asset;
import digital.fiasco.runtime.dependency.artifact.Library;
import digital.fiasco.runtime.repository.BaseRepository;

import java.net.URI;
import java.nio.file.Files;

import static digital.fiasco.runtime.FiascoRuntime.fiascoCacheFolder;
import static digital.fiasco.runtime.dependency.artifact.ArtifactResources.JAR_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactResources.JAVADOC_JAR_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactResources.SOURCES_JAR_SUFFIX;
import static java.nio.file.StandardOpenOption.APPEND;

/**
 * A fast cache of downloaded artifacts and their metadata. This cache helps to avoid unnecessary downloads when a user
 * wipes out their repository, causing it to repopulate. Instead of repopulating from Maven Central or another remote
 * repository, the artifacts in this cache can be used.
 *
 * <p><b>Retrieving Artifacts and Content</b></p>
 *
 * <ul>
 *     <li>{@link #resolve(ArtifactDescriptor)} - Gets the {@link Artifact} for the given descriptor</li>
 *     <li>{@link #content(Artifact, ArtifactContent, String)} - Gets the cached resource for the given artifact and content metadata</li>
 * </ul>
 *
 * <p><b>Adding and Removing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #install(Artifact, ArtifactResources)} - Adds the given artifact with the given attached resources</li>
 *     <li>{@link #clear()} - Removes all data from this repository</li>
 * </ul>
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class DownloadRepository extends BaseRepository
{
    /** True if this cache has been loaded */
    private boolean loaded;

    /** The file for storing artifact metadata in JSON format */
    private final File artifactsFile = cacheFile("artifacts.txt");

    /** The binary file containing artifacts, laid out end-to-end */
    private final File contentFile = cacheFile("content.bin");

    /** The cached artifact entries */
    private final ObjectMap<ArtifactDescriptor, Artifact<?>> artifacts = new ObjectMap<>();

    /** Separator to use between artifact entries in the artifacts.txt file */
    private final String ARTIFACT_SEPARATOR = "\n========\n";

    /**
     * Removes all data from this repository
     */
    @Override
    public synchronized void clear()
    {
        artifactsFile.delete();
        contentFile.delete();
    }

    /**
     * Returns the section of the binary resources file containing the given artifact
     *
     * @param artifact The artifact, including its offset and size
     * @param content The artifact content to retrieve
     * @return The resource section for the artifact's content in content.bin
     */
    @Override
    public synchronized Resource content(Artifact<?> artifact,
                                         ArtifactContent content,
                                         String suffix)
    {
        var start = content.offset();
        var end = start + content.size().asBytes();
        return new ResourceSection(contentFile, start, end);
    }

    /**
     * Adds the given content {@link Resource}s to content.bin, and the {@link Artifact} artifact to artifacts.txt in
     * JSON format.
     *
     * @param artifact The artifact to append to artifacts.txt in JSON format
     * @param resources The resources to add to content.bin
     */
    @Override
    public synchronized boolean install(Artifact<?> artifact, ArtifactResources resources)
    {
        try
        {
            if (artifact instanceof Library library)
            {
                // Save resources to artifactsFile and attach cached artifacts to the cache entry.
                artifact = library
                        .withJar(appendArtifactContent(library.jar(), resources.get(JAR_SUFFIX)))
                        .withJavadoc(appendArtifactContent(library.javadoc(), resources.get(JAVADOC_JAR_SUFFIX)))
                        .withSource(appendArtifactContent(library.source(), resources.get(SOURCES_JAR_SUFFIX)));
            }

            if (artifact instanceof Asset asset)
            {
                artifact = asset.withJar(appendArtifactContent(asset.jar(), resources.get(JAR_SUFFIX)));
            }

            // If the artifact file is missing or empty,
            var path = artifactsFile.asJavaPath();
            if (!Files.exists(path) || Files.size(path) > 0)
            {
                // write out a separator to make the file easy to read,
                Files.writeString(path, ARTIFACT_SEPARATOR);
            }

            // then append the cache entry in JSON to the end of the file.
            Files.writeString(path, artifact.toJson(), APPEND);
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
        load();
        return artifacts.get(descriptor);
    }

    @Override
    public URI uri()
    {
        return downloadCacheFolder().uri();
    }

    /**
     * Returns the download cache folder
     *
     * @return The folder
     */
    private static Folder downloadCacheFolder()
    {
        return fiascoCacheFolder().folder("download-cache");
    }

    /**
     * Saves the given content into the content file, returning the given {@link ArtifactContent} with the offset, size,
     * and last modified time populated.
     *
     * @param artifact The artifact to save
     * @param content The artifact content to write to the resources file
     * @return The updated artifact content
     */
    private ArtifactContent appendArtifactContent(ArtifactContent artifact, Resource content)
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
        return downloadCacheFolder()
                .file(name);
    }

    /**
     * Loads artifacts.txt into the artifacts map
     */
    private synchronized void load()
    {
        if (!loaded)
        {
            // Read the file,
            var text = artifactsFile.reader().readText();

            // split it into chunks,
            for (var at : text.split(ARTIFACT_SEPARATOR))
            {
                // convert the chunk to a cache entry,
                var entry = Artifact.fromJson(at);

                // and put the entry into the entries map.
                artifacts.put(entry.descriptor(), entry);
            }
            loaded = true;
        }
    }
}
