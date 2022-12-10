import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.metadata.Contributor;
import digital.fiasco.runtime.build.metadata.Copyright;
import digital.fiasco.runtime.build.metadata.Metadata;
import digital.fiasco.runtime.build.metadata.Organization;

import static digital.fiasco.runtime.repository.RemoteMavenRepository.mavenCentral;

/**
 * Example Fiasco build.
 *
 * @author jonathan
 */
@SuppressWarnings({ "InnerClassMayBeStatic", "unused" })
public class FiascoBuild extends Build implements
        Libraries,
        Repositories
{
    public static void main(String[] arguments)
    {
        new FiascoBuild().run(arguments);
    }

    @Override
    public void onBuildStart()
    {
        metadata(new Metadata("fiasco:fiasco-example:1.0")
                .withOrganization(new Organization("State of the Art")
                        .withUrl("https://www.state-of-the-art.org"))
                .withCopyright(new Copyright("Copyright 2020-2022, Jonathan Locke. All Rights Reserved."))
                .withContributor(new Contributor("Jonathan Locke")
                        .withRole("BuildProject Lead")
                        .withEmail("jon@thanlocke.com")));

        librarian().lookIn(mavenCentral).lookIn(nexus);
    }
}
