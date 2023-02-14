package digital.fiasco.runtime.dependency.artifact.content.jar;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.data.formats.yaml.model.YamlArray;

import static com.telenav.kivakit.data.formats.yaml.model.YamlArray.array;

/**
 * An index of {@link JarEntry}s in a JAR archive.
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

    @Expose
    private ObjectList<JarEntry> entries;

    /**
     * Returns the {@link JarEntry}s in this index
     *
     * @return The entries
     */
    public ObjectList<JarEntry> entries()
    {
        return entries;
    }

    /**
     * Converts this index to YAML
     *
     * @return The YAML
     */
    public YamlArray toYaml()
    {
        var yaml = array("index");
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
