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
import static digital.fiasco.runtime.build.metadata.Role.ARCHITECT;
import static digital.fiasco.runtime.build.metadata.Role.LEAD_DEVELOPER;
import static digital.fiasco.runtime.build.metadata.Role.ORIGINATOR;

/**
 * Example Fiasco build.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused" })
public class BaseProjectBuild extends Build
{
    public static void main(String[] arguments)
    {
        run(BaseProjectBuild.class, arguments);
    }

    @Override
    public BuildMetadata metadata()
    {
        var jonathanLocke = new Organization("Jonathan Locke")
                .withUrl(url("https://state-of-the-art.org"));

        return new BuildMetadata()
                .withArtifactDescriptor("digital.fiasco:fiasco-example:1.0")
                .withLicense(APACHE_LICENSE)
                .withOrganization(jonathanLocke)
                .withCopyright(new Copyright("Copyright 2022-2023, Jonathan Locke. All Rights Reserved.", year(2022), year(2023)))
                .withResources(new Resources()
                        .withHome(url("https://github.com/jonathanlocke/fiasco"))
                        .withIssues(url("https://github.com/jonathanlocke/fiasco/issues"))
                        .withSources(url("https://github.com/jonathanlocke/fiasco.git")))
                .withContributors(list(
                        new Contributor("Jonathan Locke")
                                .withNickname("Shibo")
                                .withOrganization(jonathanLocke)
                                .withTimeZone("America/Denver")
                                .withRoles(ORIGINATOR, ARCHITECT, LEAD_DEVELOPER)
                                .withEmail("jon@thanlocke.com")));
    }

    @Override
    public void onInitialize()
    {
        librarian().lookIn(MAVEN_CENTRAL);
    }
}
