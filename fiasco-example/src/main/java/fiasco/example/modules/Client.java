package fiasco.example.modules;

import fiasco.Module;
import fiasco.Project;
import fiasco.example.Libraries;

/**
 * @author jonathanl (shibo)
 */
public class Client extends Module implements Libraries
{
    public Client(final Project project)
    {
        super(project, "client");

        artifact("com.telenav.fiasco:fiasco-example-client:1.0");

        requires(kryo());
        requires(wicket_core());
        requires(commons_logging());
    }
}
