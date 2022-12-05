import fiasco.FiascoBuild;
import fiasco.metadata.Contributor;
import fiasco.metadata.Copyright;
import fiasco.metadata.Metadata;
import fiasco.metadata.Organization;

import static fiasco.repository.RemoteMavenRepository.mavenCentral;

/**
 * Example Fiasco build.
 *
 * @author jonathan
 */
@SuppressWarnings({ "InnerClassMayBeStatic", "unused" })
public class Build extends FiascoBuild implements
        Libraries,
        Repositories
{
    public static void main(String[] args)
    {
        new Build().build();
    }

    public Build()
    {
        metadata(new Metadata("fiasco:fiasco-example:1.0")
                .withOrganization(new Organization("State of the Art")
                        .withUrl("https://www.state-of-the-art.org"))
                .withCopyright(new Copyright("Copyright 2020-2022, Jonathan Locke. All Rights Reserved."))
                .withContributor(new Contributor("Jonathan Locke")
                        .withRole("FiascoBuild Lead")
                        .withEmail("jon@thanlocke.com")));

        librarian().lookIn(mavenCentral).lookIn(nexus);
    }
}
