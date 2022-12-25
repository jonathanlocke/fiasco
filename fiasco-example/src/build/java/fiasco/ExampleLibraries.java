package fiasco;

import digital.fiasco.libraries.Libraries;
import digital.fiasco.runtime.build.Build;

public interface ExampleLibraries extends
        Build,
        Libraries
{
    /**
     * Pins versions to the associated build for this interface
     */
    default void pinVersions()
    {
        pinVersion(apache_ant, "1.0.3");
        pinVersion(apache_commons_logging, "1.9.0");
        pinVersion(kryo, "4.3.1");
    }
}
