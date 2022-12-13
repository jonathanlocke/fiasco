package fiasco;

import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.BuildMetadata;
import digital.fiasco.runtime.build.metadata.Contributor;
import digital.fiasco.runtime.build.metadata.Copyright;
import digital.fiasco.runtime.build.metadata.Organization;

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

        artifactDescriptor("digital.fiasco:fiasco-example:1.0");

        metadata(new BuildMetadata()
                .withOrganization(new Organization("Fiasco")
                        .withUrl("https://www.fiasco.digital"))
                .withCopyright(new Copyright("Copyright 2020-2022, Jonathan Locke. All Rights Reserved."))
                .withContributor(new Contributor("Jonathan Locke")
                        .withRole("Project Lead")
                        .withEmail("jon@thanlocke.com")));
    }
}
