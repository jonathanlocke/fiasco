package digital.fiasco.libraries.cloud;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheIgnite
{
    Library apache_ignite_api = library("org.apache.ignite:ignite-api");
    Library apache_ignite_configuration = library("org.apache.ignite:ignite-configuration");
    Library apache_ignite_core = library("org.apache.ignite:ignite-core");
    Library apache_ignite_indexing = library("org.apache.ignite:ignite-indexing");
    Library apache_ignite_log4j = library("org.apache.ignite:ignite-log4j");
    Library apache_ignite_log4j2 = library("org.apache.ignite:ignite-log4j2");
    Library apache_ignite_network = library("org.apache.ignite:ignite-network");
    Library apache_ignite_network_api = library("org.apache.ignite:ignite-network-api");
    Library apache_ignite_schema = library("org.apache.ignite:ignite-schema");
    Library apache_ignite_slf4j = library("org.apache.ignite:ignite-slf4j");
    Library apache_ignite_spring = library("org.apache.ignite:ignite-spring");
}
