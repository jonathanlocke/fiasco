package fiasco.example;

import com.telenav.kivakit.filesystem.Folder;
import fiasco.Project;
import fiasco.example.modules.Client;
import fiasco.example.modules.Server;
import fiasco.metadata.Contributor;
import fiasco.metadata.Organization;
import fiasco.repository.RemoteMavenRepository;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "InnerClassMayBeStatic", "unused" })
public class Fiasco extends Project implements Libraries, Repositories
{
    public Fiasco(final Folder root)
    {
        super(root);

        artifact("com.telenav.fiasco:fiasco-example:1.0");
        organization(new Organization("Telenav")
                .withUrl("https://www.telenav.com"));
        copyright("Copyright 2020, Telenav Inc. All Rights Reserved.");
        contributor(new Contributor("shibo")
                .withRole("Project Lead")
                .withEmail("shibo@telenav.com"));

        librarian()
                .lookIn(RemoteMavenRepository.mavenCentral)
                .lookIn(nexus);

        requires(new Client(this));
        requires(new Server(this));
    }
}
