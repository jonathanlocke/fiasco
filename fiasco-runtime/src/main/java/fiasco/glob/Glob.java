package fiasco.glob;

import com.telenav.kivakit.resource.ResourceGlob;

@SuppressWarnings({ "unused" })
public interface Glob
{
    default ResourceGlob glob(String pattern)
    {
        return ResourceGlob.glob(pattern);
    }
}
