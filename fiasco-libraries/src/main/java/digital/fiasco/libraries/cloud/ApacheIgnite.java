package digital.fiasco.libraries.cloud;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheIgnite extends LibraryGroups
{
    Library apache_ignite_api           = apache_ignite_group.library("ignite-api");
    Library apache_ignite_configuration = apache_ignite_group.library("ignite-configuration");
    Library apache_ignite_core          = apache_ignite_group.library("ignite-core");
    Library apache_ignite_indexing      = apache_ignite_group.library("ignite-indexing");
    Library apache_ignite_log4j         = apache_ignite_group.library("ignite-log4j");
    Library apache_ignite_log4j2        = apache_ignite_group.library("ignite-log4j2");
    Library apache_ignite_network       = apache_ignite_group.library("ignite-network");
    Library apache_ignite_network_api   = apache_ignite_group.library("ignite-network-api");
    Library apache_ignite_schema        = apache_ignite_group.library("ignite-schema");
    Library apache_ignite_slf4j         = apache_ignite_group.library("ignite-slf4j");
    Library apache_ignite_spring        = apache_ignite_group.library("ignite-spring");
}
