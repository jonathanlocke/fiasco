package digital.fiasco.runtime.dependency.artifact.content.jar;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.compression.archive.ZipArchive;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContentSignatures;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.language.Hash.hashMany;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.resource.WriteMode.APPEND;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.AccessMode.READ;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.zipArchive;
import static digital.fiasco.runtime.dependency.artifact.content.jar.JarEntry.jarEntry;
import static digital.fiasco.runtime.dependency.artifact.content.jar.JarIndex.jarIndex;

/**
 * JAR artifact content exploded into a {@link JarIndex} of {@link JarEntry}s.
 *
 * <p><b>YAML</b></p>
 *
 * <ul>
 *     <li>{@link #toYaml()}</li>
 *     <li>{@link #jarContent(YamlBlock)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
public class JarContent extends ArtifactContent
{
    public static JarContent jarContent(Resource resource)
    {
        ensure(resource instanceof File);
        return new JarContent(zipArchive(throwingListener(), (File) resource, READ));
    }

    public static JarContent jarContent(YamlBlock block)
    {
        return new JarContent(block);
    }

    /** The JAR index */
    @Expose
    private JarIndex index;

    /** The JAR zip archive */
    @Expose
    private ZipArchive archive;

    protected JarContent(JarContent that)
    {
        super(that);
        this.index = that.index;
        this.archive = that.archive;
    }

    /**
     * Holds the content for a single JAR artifact. The JAR is exploded into an index which references the series of
     * decompressed files from the JAR.
     *
     * @param archive The JAR archive
     */
    protected JarContent(ZipArchive archive)
    {
        super(archive.file().fileName().name(), null, archive.file().identifier(), -1,
            archive.resource().lastModified().asLocalTime(), archive.resource().sizeInBytes());

        this.archive = archive;

        this.index = jarIndex();
        var offset = 0L;
        for (var at : archive)
        {
            var entry = jarEntry(at)
                .withOffset(offset);
            index = index.withEntry(entry);
            offset += entry.size().asBytes();
        }
    }

    protected JarContent(YamlBlock block)
    {
        super(block);

        index = jarIndex(block.array("index"));
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
    public JarContent copy()
    {
        return new JarContent(this);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof JarContent that)
        {
            return super.equals(that) && this.index.equals(that.index);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hashMany(super.hashCode(), index.hashCode());
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
            .with(index.toYaml());
    }

    @Override
    public JarContent withLastModified(LocalTime lastModified)
    {
        return (JarContent) super.withLastModified(lastModified);
    }

    @Override
    public JarContent withName(String name)
    {
        return (JarContent) super.withName(name);
    }

    @Override
    public JarContent withOffset(long offset)
    {
        return (JarContent) super.withOffset(offset);
    }

    @Override
    public JarContent withResource(Resource resource)
    {
        return (JarContent) super.withResource(resource);
    }

    @Override
    public JarContent withSignatures(ArtifactContentSignatures signatures)
    {
        return (JarContent) super.withSignatures(signatures);
    }

    @Override
    public JarContent withSize(Bytes size)
    {
        return (JarContent) super.withSize(size);
    }
}
