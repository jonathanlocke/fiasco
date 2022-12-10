import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.metadata.Contributor;
import digital.fiasco.runtime.build.metadata.Copyright;
import digital.fiasco.runtime.build.metadata.Metadata;
import digital.fiasco.runtime.build.metadata.Organization;

import static digital.fiasco.runtime.build.tools.librarian.Library.library;

/**
 * Example Fiasco build.
 *
 * @author jonathan
 */
@SuppressWarnings({ "InnerClassMayBeStatic", "unused", "SpellCheckingInspection" })
public class FiascoBuild extends Build
{
    public static void main(String[] arguments)
    {
        new FiascoBuild().run(arguments);
    }

    @Override
    public void onBuildStart()
    {
        metadata(new Metadata("fiasco:fiasco-example:1.0")
                .withOrganization(new Organization("Digital Fiasco")
                        .withUrl("https://www.fiasco.digital"))
                .withCopyright(new Copyright("Copyright 2020-2022, Jonathan Locke. All Rights Reserved."))
                .withContributor(new Contributor("Jonathan Locke")
                        .withRole("Project Lead")
                        .withEmail("jon@thanlocke.com")));

        libraries().add(library("org.apache.commons:logging:1.0"));
        libraries().add(library("com.esotericsoftware:kryo:4.3.1"));
    }
}
