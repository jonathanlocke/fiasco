package fiasco;

import digital.fiasco.libraries.Libraries;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.metadata.BuildMetadata;
import digital.fiasco.runtime.build.metadata.Organization;

import static com.telenav.kivakit.resource.Urls.url;
import static digital.fiasco.libraries.utilities.serialization.Kryo.kryo;
import static digital.fiasco.runtime.build.metadata.BuildMetadata.buildMetadata;
import static digital.fiasco.runtime.build.metadata.Contributor.contributor;
import static digital.fiasco.runtime.build.metadata.Copyright.copyright;
import static digital.fiasco.runtime.build.metadata.License.APACHE_LICENSE_2;
import static digital.fiasco.runtime.build.metadata.License.MIT_LICENSE;
import static digital.fiasco.runtime.build.metadata.MailingList.developerMailingList;
import static digital.fiasco.runtime.build.metadata.MailingList.userMailingList;
import static digital.fiasco.runtime.build.metadata.ProjectResource.projectDocumentation;
import static digital.fiasco.runtime.build.metadata.ProjectResource.projectHome;
import static digital.fiasco.runtime.build.metadata.ProjectResource.projectIssues;
import static digital.fiasco.runtime.build.metadata.ProjectResource.projectSources;
import static digital.fiasco.runtime.build.metadata.ProjectRole.ARCHITECT;
import static digital.fiasco.runtime.build.metadata.ProjectRole.LEAD_DEVELOPER;
import static digital.fiasco.runtime.build.metadata.ProjectRole.ORIGINATOR;

/**
 * Example Fiasco build.
 *
 * @author Jonathan Locke
 */
public class ExampleBuild extends BaseBuild implements Libraries
{
    public static void main(String[] arguments)
    {
        run(ExampleBuild.class, arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BuildMetadata metadata()
    {
        var fiasco = new Organization("Fiasco")
            .withUrl(url("https://fiasco.digital"));

        return buildMetadata()
            .withArtifactDescriptor("digital.fiasco:fiasco-example:1.0")
            .withDescription("Example use of Fiasco build system")
            .withLicenses(APACHE_LICENSE_2, MIT_LICENSE)
            .withOrganization(fiasco)
            .withCopyright(copyright()
                .withFrom(2022)
                .withTo(2023)
                .withText("Copyright 2022-2023, Jonathan Locke. All Rights Reserved."))
            .withProjectResources(
                projectHome("https://github.com/jonathanlocke/fiasco"),
                projectDocumentation("https://github.com/jonathanlocke/fiasco"),
                projectSources("https://github.com/jonathanlocke/fiasco.git"),
                projectIssues("https://github.com/jonathanlocke/fiasco/issues"))
            .withMailingLists(
                userMailingList()
                    .withSubscribe("user-subscribe@fiasco.digital")
                    .withUnsubscribe("user-unsubscribe@fiasco.digital")
                    .withPost("user@fiasco.digital"),
                developerMailingList()
                    .withSubscribe("developer-subscribe@fiasco.digital")
                    .withUnsubscribe("developer-unsubscribe@fiasco.digital")
                    .withPost("developer@fiasco.digital"))
            .withContributor(
                contributor("Jonathan Locke")
                    .withNickname("Shibo")
                    .withOrganization(fiasco)
                    .withTimeZone("America/Denver")
                    .withRoles(ORIGINATOR, ARCHITECT, LEAD_DEVELOPER)
                    .withEmail("jon@thanlocke.com"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Builder onConfigureBuild(Builder root)
    {
        var utilities = root
            .deriveBuilder("utilities")
            .requires(hamcrest_library.version("5.0"));

        var model = root
            .deriveBuilder("child2")
            .requires(hamcrest_library.version("5.0"))
            .dependsOn(utilities);

        return root
            .requires(apache_ant, apache_commons_logging, kryo)
            .pinVersion(apache_ant, "1.0.3")
            .pinVersion(apache_commons_logging, "1.9.0")
            .pinVersion(kryo, "4.3.1")
            .beforePhase("compile", it ->
            {
                var cleaner = it.newCleaner();
                cleaner.run();
            })
            .dependsOn(model);
    }
}
