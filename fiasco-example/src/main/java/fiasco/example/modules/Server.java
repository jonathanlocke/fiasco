package fiasco.example.modules;

import fiasco.Module;
import fiasco.Project;
import fiasco.example.Libraries;

import static com.telenav.kivakit.core.os.Console.console;

/**
 * @author jonathanl (shibo)
 */
public class Server extends Module implements Libraries
{
    public Server(Project project)
    {
        super(project, "server");

        artifact("com.telenav.fiasco:fiasco-example-server:1.0");

        requires(kryo());
        requires(wicket_core());
        requires(commons_logging());

        builder().onCompiled(() -> console().println("done"));
    }
}
