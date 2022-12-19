package fiasco;

import digital.fiasco.libraries.Libraries;
import digital.fiasco.runtime.build.MultiBuild;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;

import static com.telenav.kivakit.core.version.Version.parseVersion;

/**
 * Example Fiasco build.
 *
 * @author jonathan
 */
public class FiascoBuild extends MultiBuild implements Libraries
{
    public static void main(String[] arguments)
    {
        new FiascoBuild().arguments(arguments).run(arguments);
    }

    final String version_apache_commons_logging = "1.0";

    final String version_kivakit = "1.9.0";

    final String version_kryo = "4.3.1";

    @Override
    protected void onInitialize()
    {
        var build = new ProjectBuild();

        build.librarian().pinArtifactVersion(
                ArtifactDescriptor.artifactDescriptor("org.apache.commons:logging"),
                parseVersion("1.0.3"));

        addBuild(build
                .project("example1")
                .withDependencies(
                        apache_commons_logging.version(version_apache_commons_logging),
                        kryo.version(version_kryo)
                ));

        addBuild(build
                .project("example2")
                .withDependencies(
                        kivakit_application.version(version_kivakit),
                        kivakit_network_core.version(version_kivakit)
                ));
    }
}
