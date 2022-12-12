package digital.fiasco.runtime.repository.artifact;

import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.resource.Resource;

@SuppressWarnings("unused")
public class ArtifactResources
{
    /** Suffix for javadoc attachment */
    public static final String JAVADOC_JAR_SUFFIX = "-javadoc.jar";

    /** Suffix for jar attachment */
    public static final String JAR_SUFFIX = ".jar";

    /** Suffix for sources attachment */
    public static final String SOURCES_JAR_SUFFIX = "-sources.jar";

    private final ObjectMap<String, Resource> resources = new ObjectMap<>();

    public void add(String suffix, Resource resource)
    {
        resources.put(suffix, resource);
    }

    public Resource get(String suffix)
    {
        return resources.get(suffix);
    }
}
