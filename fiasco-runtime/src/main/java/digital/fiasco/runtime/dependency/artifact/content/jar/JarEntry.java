package digital.fiasco.runtime.dependency.artifact.content.jar;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.interfaces.object.Copyable;
import com.telenav.kivakit.resource.compression.archive.ZipEntry;

import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.scalar;

/**
 * An entry in a JAR archive.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #path()}</li>
 *     <li>{@link #size()}</li>
 *     <li>{@link #lastModified()}</li>
 *     <li>{@link #offset()}</li>
 * </ul>
 *
 * <p><b>YAML</b></p>
 *
 * <ul>
 *     <li>{@link #toYaml()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
public class JarEntry implements Copyable<JarEntry>
{
    public static JarEntry jarEntry()
    {
        return new JarEntry();
    }

    public static JarEntry jarEntry(ZipEntry entry)
    {
        return jarEntry()
            .withPath(entry.path().asString())
            .withSize(entry.sizeInBytes())
            .withLastModified(entry.lastModified());
    }

    /** The full path to the jar entry, including filename */
    @Expose
    private String path;

    /** The size of the entry */
    @Expose
    private Bytes size;

    /** The time of last modification of the entry */
    @Expose
    private Time lastModified;

    /** The offset of this JAR entry */
    @Expose
    private long offset;

    private JarEntry(JarEntry that)
    {
        this.path = that.path;
        this.size = that.size;
        this.lastModified = that.lastModified;
        this.offset = that.offset;
    }

    private JarEntry()
    {
    }

    @Override
    public JarEntry copy()
    {
        return new JarEntry(this);
    }

    public Time lastModified()
    {
        return lastModified;
    }

    public long offset()
    {
        return offset;
    }

    public String path()
    {
        return path;
    }

    public Bytes size()
    {
        return size;
    }

    /**
     * Converts this entry to YAML
     *
     * @return The YAML
     */
    public YamlBlock toYaml()
    {
        return block()
            .with(scalar("path", path))
            .with(scalar("size", size.asBytes() + " bytes"))
            .with(scalar("lastModified", lastModified.asLocalTime().asDateTimeString()))
            .with(scalar("offset", offset));
    }

    public JarEntry withLastModified(Time time)
    {
        return mutatedCopy(it -> it.lastModified = time);
    }

    public JarEntry withOffset(long offset)
    {
        return mutatedCopy(it -> it.offset = offset);
    }

    public JarEntry withPath(String path)
    {
        return mutatedCopy(it -> it.path = path);
    }

    public JarEntry withSize(Bytes size)
    {
        return mutatedCopy(it -> it.size = size);
    }
}
