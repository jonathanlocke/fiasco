package digital.fiasco.runtime.build.structure;

import com.telenav.kivakit.filesystem.Folder;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds information about the folder structure of the build.
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class BaseBuildStructure implements BuildStructure
{
    /** Map from logical name to folder */
    private final Map<String, Folder> folders = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Folder folder(String key)
    {
        return folders.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void folder(String key, Folder folder)
    {
        folders.put(key, folder);
    }
}
