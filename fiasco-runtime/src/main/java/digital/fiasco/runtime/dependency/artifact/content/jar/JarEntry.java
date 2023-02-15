package digital.fiasco.runtime.dependency.artifact.content.jar;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.interfaces.object.Copyable;
import com.telenav.kivakit.resource.compression.archive.ZipEntry;

import static com.telenav.kivakit.core.language.Hash.hashMany;
import static com.telenav.kivakit.core.language.Objects.areEqualPairs;
import static com.telenav.kivakit.core.time.Duration.ONE_SECOND;
import static com.telenav.kivakit.core.time.KivaKitTimeFormats.KIVAKIT_DATE_TIME_SECONDS;
import static com.telenav.kivakit.core.time.LocalTime.localTime;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.yamlBlock;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.yamlScalar;

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
 *     <li>{@link #jarEntry(YamlBlock)}</li>
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
            .withPath(entry.jarPath().asString())
            .withSize(entry.sizeInBytes())
            .withLastModified(entry.lastModified());
    }

    public static JarEntry jarEntry(YamlBlock block)
    {
        return new JarEntry(block);
    }

    /** The full path to the jar entry, including filename */
    @FormatProperty
    @Expose
    private String path;

    /** The size of the entry */
    @Expose
    @FormatProperty
    private Bytes size;

    /** The time of last modification of the entry */
    @Expose
    @FormatProperty
    private Time lastModified;

    /** The offset of this JAR entry */
    @Expose
    @FormatProperty
    private long offset;

    public JarEntry(YamlBlock block)
    {
        path = block.scalar("path").string();
        size = bytes(block.scalar("size").number().intValue());
        lastModified = localTime(KIVAKIT_DATE_TIME_SECONDS,
            block.scalar("lastModified").string()).roundDown(ONE_SECOND);
        offset = block.scalar("offset").number().longValue();
    }

    protected JarEntry(JarEntry that)
    {
        this.path = that.path;
        this.size = that.size;
        this.lastModified = that.lastModified;
        this.offset = that.offset;
    }

    protected JarEntry()
    {
    }

    @Override
    public JarEntry copy()
    {
        return new JarEntry(this);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof JarEntry that)
        {
            return areEqualPairs(
                this.size, that.size,
                this.path, that.path,
                this.offset, that.offset,
                this.lastModified, that.lastModified);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hashMany(lastModified, size, path, offset);
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

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * Converts this entry to YAML
     *
     * @return The YAML
     */
    public YamlBlock toYaml()
    {
        return yamlBlock()
            .with(yamlScalar("path", path))
            .with(yamlScalar("size", size.asBytes()))
            .with(yamlScalar("lastModified", lastModified.asLocalTime().asDateTimeSecondsString()))
            .with(yamlScalar("offset", offset));
    }

    public JarEntry withLastModified(Time time)
    {
        return mutated(it -> it.lastModified = time);
    }

    public JarEntry withOffset(long offset)
    {
        return mutated(it -> it.offset = offset);
    }

    public JarEntry withPath(String path)
    {
        return mutated(it -> it.path = path);
    }

    public JarEntry withSize(Bytes size)
    {
        return mutated(it -> it.size = size);
    }
}
