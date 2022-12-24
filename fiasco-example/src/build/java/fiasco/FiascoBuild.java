package fiasco;

import digital.fiasco.libraries.Libraries;
import digital.fiasco.runtime.build.BuildListener;

/**
 * Example Fiasco build.
 *
 * @author Jonathan Locke
 */
public class FiascoBuild extends BaseProjectBuild implements
        Libraries,
        BuildListener
{
    public static void main(String[] arguments)
    {
        new FiascoBuild().run(arguments);
    }

    @Override
    public void onInitialize()
    {
        requires(apache_ant,
                apache_commons_logging.version("1.9.0"),
                kryo.version("4.3.1"));

        librarian().pinVersion(apache_ant, "1.0.3");
    }
}
