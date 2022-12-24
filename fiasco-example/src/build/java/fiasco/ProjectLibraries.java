package fiasco;

import digital.fiasco.runtime.build.Build;

import static digital.fiasco.libraries.build.ApacheAnt.apache_ant;
import static digital.fiasco.libraries.languages.java.serialization.Kryo.kryo;
import static digital.fiasco.libraries.logging.ApacheCommonsLogging.apache_commons_logging;

public interface ProjectLibraries extends Build
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
