package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.resource.Resource;

/**
 * The resources attached to an artifact
 *
 * <p><b>Attachments</b></p>
 *
 * <ul>
 *     <li>{@link #attach(String, Resource)} - Attaches the given resource</li>
 *     <li>{@link #attachment(String)} - Returns the given attachment</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class ArtifactAttachments
{
    /** Suffix for javadoc attachment */
    public static final String JAVADOC_JAR_SUFFIX = "-javadoc.jar";

    /** Suffix for jar attachment */
    public static final String JAR_SUFFIX = ".jar";

    /** Suffix for sources attachment */
    public static final String SOURCES_JAR_SUFFIX = "-sources.jar";

    /** The resources by suffix */
    private final ObjectMap<String, Resource> resources = new ObjectMap<>();

    /**
     * Attaches the resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param suffix The artifact suffix
     * @param resource The resource to attach
     */
    public void attach(String suffix, Resource resource)
    {
        resources.put(suffix, resource);
    }

    /**
     * Returns the attached resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param suffix The artifact suffix
     * @return The attached resource
     */
    public Resource attachment(String suffix)
    {
        return resources.get(suffix);
    }
}
