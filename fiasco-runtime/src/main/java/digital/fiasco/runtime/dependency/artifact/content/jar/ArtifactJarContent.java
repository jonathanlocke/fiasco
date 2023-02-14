package digital.fiasco.runtime.dependency.artifact.content.jar;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.compression.archive.ZipArchive;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContentSignatures;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.block;
import static com.telenav.kivakit.resource.WriteMode.APPEND;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.AccessMode.READ;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.zipArchive;
import static digital.fiasco.runtime.dependency.artifact.content.jar.JarEntry.jarEntry;
import static digital.fiasco.runtime.dependency.artifact.content.jar.JarIndex.jarIndex;

/**
 * JAR artifact content exploded into a {@link JarIndex} of {@link JarEntry}s.
 */
public class ArtifactJarContent extends ArtifactContent
{
    public static ArtifactJarContent jarContent(Resource resource)
    {
        ensure(resource instanceof File);
        return new ArtifactJarContent(null, null, null, -1,
            zipArchive(throwingListener(), (File) resource, READ));
    }

    /** The JAR index */
    @Expose
    private JarIndex index;

    /** The JAR zip archive */
    @Expose
    private final ZipArchive archive;

    /**
     * Holds the content for a single JAR artifact. The JAR is exploded into an index which references the series of
     * decompressed files in the JAR.
     *
     * @param name The name of the artifact resource
     * @param signatures The signatures for this content
     * @param resourceIdentifier The resource containing the content
     * @param offset The offset of this content in a cache file (if any)
     * @param archive The JAR archive
     */
    public ArtifactJarContent(String name,
                              ArtifactContentSignatures signatures,
                              ResourceIdentifier resourceIdentifier,
                              long offset,
                              ZipArchive archive)
    {
        super(name, signatures, resourceIdentifier, offset, archive.resource().lastModified(), archive.resource().sizeInBytes());

        this.archive = archive;

        this.index = jarIndex();
        for (var entry : archive)
        {
            index = index.withEntry(jarEntry(entry));
        }
    }

    /**
     * Appends all JAR entries to the given file. The entries can be found later using the {@link JarIndex}.
     *
     * @param file The file
     */
    public void appendEntriesTo(File file)
    {
        archive.forEach(it -> it.safeCopyTo(file, APPEND));
    }

    @Override
    public boolean equals(Object object)
    {
        return super.equals(object);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    /**
     * Returns the index for this JAR content
     *
     * @return The index
     */
    public JarIndex index()
    {
        return index;
    }

    @Override
    public YamlBlock toYaml()
    {
        return super.toYaml()
            .with(block("index")
                .with(index.toYaml()));
    }
}
