package fiasco;

import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.BuildMetadata;
import digital.fiasco.runtime.build.metadata.Contributor;
import digital.fiasco.runtime.build.metadata.Copyright;
import digital.fiasco.runtime.build.metadata.Organization;
import digital.fiasco.runtime.build.metadata.Resources;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.time.Year.year;
import static com.telenav.kivakit.resource.Urls.url;
import static digital.fiasco.runtime.build.metadata.License.APACHE_LICENSE;

/**
 * Example Fiasco build.
 *
 * @author jonathan
 */
@SuppressWarnings({ "InnerClassMayBeStatic", "unused" })
public class ProjectBuild extends Build
{
    public static void main(String[] arguments)
    {
        run(ProjectBuild.class, arguments);
    }

    @Override
    public void onInitialize()
    {
        librarian().lookIn(mavenCentral);

        var jonathanLocke = new Organization("Jonathan Locke");

        metadata(new BuildMetadata()
                .withArtifactDescriptor("digital.fiasco:fiasco-example:1.0")
                .withLicense(APACHE_LICENSE)
                .withOrganization(jonathanLocke)
                .withCopyright(new Copyright("Copyright 2022-2023, Jonathan Locke. All Rights Reserved.", year(2022), year(2023)))
                .withResources(new Resources()
                        .withHome(url("https://github.com/jonathanlocke/fiasco"))
                        .withIssues(url("https://github.com/jonathanlocke/fiasco/issues"))
                        .withCode(url("https://github.com/jonathanlocke/fiasco.git")))
                .withContributors(list(
                        new Contributor("Jonathan Locke")
                                .withOrganization(jonathanLocke)
                                .withTimeZone("America/Denver")
                                .withRole("Originator")
                                .withRole("Project Lead")
                                .withEmail("jon@thanlocke.com"))));
    }
}
