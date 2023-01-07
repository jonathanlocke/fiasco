package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;

import java.net.URL;

import static com.telenav.kivakit.resource.Urls.url;

/**
 * Locations of project resources
 *
 * @param name The name of the project resource
 * @param location The location of the resource
 */
@SuppressWarnings("unused")
public record ProjectResource(@FormatProperty String name,
                              @FormatProperty URL location)
{
    public static ProjectResource projectDocumentation(String location)
    {
        return projectResources()
            .withName("Project Documentation")
            .withLocation(location);
    }

    public static ProjectResource projectHome(String location)
    {
        return projectResources()
            .withName("Project Home")
            .withLocation(location);
    }

    public static ProjectResource projectIssues(String location)
    {
        return projectResources()
            .withName("Project Issues")
            .withLocation(location);
    }

    public static ProjectResource projectResources()
    {
        return new ProjectResource(null, null);
    }

    public static ProjectResource projectSources(String location)
    {
        return projectResources()
            .withName("Project Sources")
            .withLocation(location);
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public ProjectResource withLocation(String location)
    {
        return withLocation(url(location));
    }

    public ProjectResource withLocation(URL location)
    {
        return new ProjectResource(name, location);
    }

    public ProjectResource withName(String name)
    {
        return new ProjectResource(name, location);
    }
}
