package digital.fiasco.libraries.frameworks;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface KivaKit extends LibraryGroups
{
    Library kivakit                           = kivakit_group.library("kivakit");
    Library kivakit_annotations               = kivakit_group.library("kivakit-annotations");
    Library kivakit_application               = kivakit_group.library("kivakit-application");
    Library kivakit_collections               = kivakit_group.library("kivakit-collections");
    Library kivakit_commandline               = kivakit_group.library("kivakit-commandline");
    Library kivakit_component                 = kivakit_group.library("kivakit-component");
    Library kivakit_conversion                = kivakit_group.library("kivakit-conversion");
    Library kivakit_core                      = kivakit_group.library("kivakit-core");
    Library kivakit_extraction                = kivakit_group.library("kivakit-extraction");
    Library kivakit_filesystems_github        = kivakit_group.library("kivakit-filesystems-github");
    Library kivakit_filesystems_java          = kivakit_group.library("kivakit-filesystems-java");
    Library kivakit_filesystems_s3fs          = kivakit_group.library("kivakit-filesystems-s3fs");
    Library kivakit_interfaces                = kivakit_group.library("kivakit-interfaces");
    Library kivakit_logs_email                = kivakit_group.library("kivakit-logs-email");
    Library kivakit_logs_file                 = kivakit_group.library("kivakit-logs-file");
    Library kivakit_microservice              = kivakit_group.library("kivakit-microservice");
    Library kivakit_mixins                    = kivakit_group.library("kivakit-mixins");
    Library kivakit_network_core              = kivakit_group.library("kivakit-network-core");
    Library kivakit_network_email             = kivakit_group.library("kivakit-network-email");
    Library kivakit_network_ftp               = kivakit_group.library("kivakit-network-ftp");
    Library kivakit_network_http              = kivakit_group.library("kivakit-network-http");
    Library kivakit_network_socket            = kivakit_group.library("kivakit-network-socket");
    Library kivakit_resource                  = kivakit_group.library("kivakit-resource");
    Library kivakit_serialization_core        = kivakit_group.library("kivakit-serialization-core");
    Library kivakit_serialization_gson        = kivakit_group.library("kivakit-serialization-gson");
    Library kivakit_serialization_kryo        = kivakit_group.library("kivakit-serialization-kryo");
    Library kivakit_serialization_properties  = kivakit_group.library("kivakit-serialization-properties");
    Library kivakit_serialization_yaml        = kivakit_group.library("kivakit-serialization-yaml");
    Library kivakit_settings                  = kivakit_group.library("kivakit-settings");
    Library kivakit_settings_stores_zookeeper = kivakit_group.library("kivakit-settings-stores-zookeeper");
    Library kivakit_testing                   = kivakit_group.library("kivakit-testing");
    Library kivakit_validation                = kivakit_group.library("kivakit-validation");
    Library kivakit_web_jersey                = kivakit_group.library("kivakit-web-jersey");
    Library kivakit_web_jetty                 = kivakit_group.library("kivakit-web-jetty");
    Library kivakit_web_swagger               = kivakit_group.library("kivakit-web-swagger");
}
