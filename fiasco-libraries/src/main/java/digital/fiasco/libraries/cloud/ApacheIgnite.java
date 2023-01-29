package digital.fiasco.libraries.cloud;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface ApacheIgnite extends LibraryGroups
{
    Library apache_ignite_api           = apache_ignite_group.library("ignite-api").asLibrary();
    Library apache_ignite_configuration = apache_ignite_group.library("ignite-configuration").asLibrary();
    Library apache_ignite_core          = apache_ignite_group.library("ignite-core").asLibrary();
    Library apache_ignite_indexing      = apache_ignite_group.library("ignite-indexing").asLibrary();
    Library apache_ignite_log4j         = apache_ignite_group.library("ignite-log4j").asLibrary();
    Library apache_ignite_log4j2        = apache_ignite_group.library("ignite-log4j2").asLibrary();
    Library apache_ignite_network       = apache_ignite_group.library("ignite-network").asLibrary();
    Library apache_ignite_network_api   = apache_ignite_group.library("ignite-network-api").asLibrary();
    Library apache_ignite_schema        = apache_ignite_group.library("ignite-schema").asLibrary();
    Library apache_ignite_slf4j         = apache_ignite_group.library("ignite-slf4j").asLibrary();
    Library apache_ignite_spring        = apache_ignite_group.library("ignite-spring").asLibrary();
}
