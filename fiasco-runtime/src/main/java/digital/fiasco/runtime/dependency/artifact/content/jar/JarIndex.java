package digital.fiasco.runtime.dependency.artifact.content.jar;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.data.formats.yaml.model.YamlArray;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.data.formats.yaml.model.YamlArray.yamlArray;
import static digital.fiasco.runtime.dependency.artifact.content.jar.JarEntry.jarEntry;

/**
 * An index of {@link JarEntry}s in a JAR archive.
 *
 * <p><b>YAML</b></p>
 *
 * <ul>
 *     <li>{@link #toYaml()}</li>
 *     <li>{@link #jarIndex(YamlArray)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see JarEntry
 */
public class JarIndex
{
    public static JarIndex jarIndex()
    {
        return new JarIndex();
    }

    public static JarIndex jarIndex(YamlArray array)
    {
        return new JarIndex(array);
    }

    @Expose
    @FormatProperty
    private ObjectList<JarEntry> entries = list();

    protected JarIndex(YamlArray array)
    {
        entries = new ObjectList<>();
        for (var at : array.elements())
        {
            entries.add(jarEntry((YamlBlock) at));
        }
    }

    protected JarIndex()
    {
    }

    /**
     * Returns the {@link JarEntry}s in this index
     *
     * @return The entries
     */
    public ObjectList<JarEntry> entries()
    {
        return entries;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof JarIndex that)
        {
            return this.entries.equals(that.entries);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return entries.hashCode();
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * Converts this index to YAML
     *
     * @return The YAML
     */
    public YamlArray toYaml()
    {
        var yaml = yamlArray("index");
        for (var at : entries)
        {
            yaml = yaml.with(at.toYaml());
        }
        return yaml;
    }

    public JarIndex withEntry(JarEntry entry)
    {
        var copy = new JarIndex();
        copy.entries = entries.copy();
        copy.entries.add(entry);
        return copy;
    }
}
