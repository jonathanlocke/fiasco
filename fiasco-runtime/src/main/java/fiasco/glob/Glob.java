package fiasco.glob;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.ResourceGlob;

@SuppressWarnings({ "unused" })
public interface Glob
{
    default ResourceGlob glob(String pattern)
    {
        return ResourceGlob.glob(pattern);
    }

    default ResourceGlob glob(Folder folder, String pattern)
    {
        return glob(folder + "/" + pattern);
    }
}
