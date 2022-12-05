import fiasco.FiascoBuild;

import static fiasco.BuildMetadata.metadata;
import static fiasco.metadata.Contributor.contributor;
import static fiasco.metadata.Organization.organization;
import static fiasco.repository.RemoteMavenRepository.mavenCentral;

/**
 * Example Fiasco build.
 *
 * @author jonathanl (shibo)
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
        super(metadata("fiasco:fiasco-example:1.0")
                .withOrganization(organization("State of the Art")
                        .withUrl("https://www.state-of-the-art.org"))
                .withCopyright("Copyright 2020-2022, Jonathan Locke. All Rights Reserved.")
                .withContributor(contributor("Jonathan Locke")
                        .withRole("FiascoBuild Lead")
                        .withEmail("jon@thanlocke.com")));

        librarian().lookIn(mavenCentral).lookIn(nexus);
    }
}
