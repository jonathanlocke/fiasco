package fiasco;

/**
 * Example Fiasco build.
 *
 * @author Jonathan Locke
 */
public class ExampleMultiBuild extends MultiProjectBuild
{
    public static void main(String[] arguments)
    {
        new ExampleBuild().run(arguments);
    }

    @Override
    protected void onInitialize()
    {
        pinVersions();

        var build = new ExampleBuild();

        addBuild(build.childBuild("example1"));

        addBuild(build.childBuild("example2")
                .withDependencies(
                        kivakit_application,
                        kivakit_network_core
                ));
    }
}
