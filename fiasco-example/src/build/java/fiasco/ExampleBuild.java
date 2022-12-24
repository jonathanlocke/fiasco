package fiasco;

import digital.fiasco.runtime.build.BuildMetadata;

/**
 * Example Fiasco build.
 *
 * @author Jonathan Locke
 */
public class ExampleBuild extends ProjectBuild
{
    public static void main(String[] arguments)
    {
        run(ExampleBuild.class, arguments);
    }

    @Override
    public BuildMetadata metadata()
    {
        return super.metadata();
    }

    @Override
    public void onInitialize()
    {
        requires(apache_ant, apache_commons_logging, kryo);
    }
}
