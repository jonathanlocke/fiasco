package fiasco;

import digital.fiasco.libraries.Libraries;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.metadata.BuildMetadata;
import digital.fiasco.runtime.build.metadata.Contributor;
import digital.fiasco.runtime.build.metadata.Copyright;
import digital.fiasco.runtime.build.metadata.Organization;
import digital.fiasco.runtime.build.metadata.Resources;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.time.Year.year;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static com.telenav.kivakit.resource.Urls.url;
import static digital.fiasco.libraries.utilities.serialization.Kryo.kryo;
import static digital.fiasco.runtime.build.metadata.License.APACHE_LICENSE;
import static digital.fiasco.runtime.build.metadata.Role.ARCHITECT;
import static digital.fiasco.runtime.build.metadata.Role.LEAD_DEVELOPER;
import static digital.fiasco.runtime.build.metadata.Role.ORIGINATOR;

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

        return new BuildMetadata()
            .withArtifactDescriptor("digital.fiasco:fiasco-example:1.0")
            .withLicense(APACHE_LICENSE)
            .withOrganization(fiasco)
            .withCopyright(new Copyright("Copyright 2022-2023, Jonathan Locke. All Rights Reserved.", year(2022), year(2023)))
            .withResources(new Resources()
                .withHome(url("https://github.com/jonathanlocke/fiasco"))
                .withIssues(url("https://github.com/jonathanlocke/fiasco/issues"))
                .withSources(url("https://github.com/jonathanlocke/fiasco.git")))
            .withContributors(list(
                new Contributor("Jonathan Locke")
                    .withNickname("Shibo")
                    .withOrganization(fiasco)
                    .withTimeZone("America/Denver")
                    .withRoles(ORIGINATOR, ARCHITECT, LEAD_DEVELOPER)
                    .withEmail("jon@thanlocke.com")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBuild()
    {
        var builder = newBuilder()
            .withTargetArtifactDescriptor("digital.fiasco:example:1.0")
            .requires(apache_ant, apache_commons_logging, kryo)
            .pinVersion(apache_ant, "1.0.3")
            .pinVersion(apache_commons_logging, "1.9.0")
            .pinVersion(kryo, "4.3.1")
            .withRootFolder(currentFolder())
            .beforePhase("compile", () ->
            {
            });

        builder.build().showResult();

        // Multi-project build
        var child = builder.childBuilder("example-child")
            .withDependencies(hamcrest_library.version("5.0"));
        child.build();
    }
}

