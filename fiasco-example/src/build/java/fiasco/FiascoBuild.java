package fiasco;

import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.metadata.Contributor;
import digital.fiasco.runtime.build.metadata.Copyright;
import digital.fiasco.runtime.build.metadata.Metadata;
import digital.fiasco.runtime.build.metadata.Organization;

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
        run(FiascoBuild.class, arguments);
    }

    @Override
    public void onInitialize()
    {
        artifact("digital.fiasco:fiasco-example:1.0");

        metadata(new Metadata()
                .withOrganization(new Organization("Fiasco")
                        .withUrl("https://www.fiasco.digital"))
                .withCopyright(new Copyright("Copyright 2020-2022, Jonathan Locke. All Rights Reserved."))
                .withContributor(new Contributor("Jonathan Locke")
                        .withRole("Project Lead")
                        .withEmail("jon@thanlocke.com")));

        addLibrary("org.apache.commons:logging:1.0");
        addLibrary("com.esotericsoftware:kryo:4.3.1");
    }
}
