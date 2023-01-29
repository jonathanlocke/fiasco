package digital.fiasco.libraries.frameworks;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface KivaKit extends LibraryGroups
{
    Library kivakit                           = kivakit_group.library("kivakit").asLibrary();
    Library kivakit_annotations               = kivakit_group.library("kivakit-annotations").asLibrary();
    Library kivakit_application               = kivakit_group.library("kivakit-application").asLibrary();
    Library kivakit_collections               = kivakit_group.library("kivakit-collections").asLibrary();
    Library kivakit_commandline               = kivakit_group.library("kivakit-commandline").asLibrary();
    Library kivakit_component                 = kivakit_group.library("kivakit-component").asLibrary();
    Library kivakit_conversion                = kivakit_group.library("kivakit-conversion").asLibrary();
    Library kivakit_core                      = kivakit_group.library("kivakit-core").asLibrary();
    Library kivakit_extraction                = kivakit_group.library("kivakit-extraction").asLibrary();
    Library kivakit_filesystems_github        = kivakit_group.library("kivakit-filesystems-github").asLibrary();
    Library kivakit_filesystems_java          = kivakit_group.library("kivakit-filesystems-java").asLibrary();
    Library kivakit_filesystems_s3fs          = kivakit_group.library("kivakit-filesystems-s3fs").asLibrary();
    Library kivakit_interfaces                = kivakit_group.library("kivakit-interfaces").asLibrary();
    Library kivakit_logs_email                = kivakit_group.library("kivakit-logs-email").asLibrary();
    Library kivakit_logs_file                 = kivakit_group.library("kivakit-logs-file").asLibrary();
    Library kivakit_microservice              = kivakit_group.library("kivakit-microservice").asLibrary();
    Library kivakit_mixins                    = kivakit_group.library("kivakit-mixins").asLibrary();
    Library kivakit_network_core              = kivakit_group.library("kivakit-network-core").asLibrary();
    Library kivakit_network_email             = kivakit_group.library("kivakit-network-email").asLibrary();
    Library kivakit_network_ftp               = kivakit_group.library("kivakit-network-ftp").asLibrary();
    Library kivakit_network_http              = kivakit_group.library("kivakit-network-http").asLibrary();
    Library kivakit_network_socket            = kivakit_group.library("kivakit-network-socket").asLibrary();
    Library kivakit_resource                  = kivakit_group.library("kivakit-resource").asLibrary();
    Library kivakit_serialization_core        = kivakit_group.library("kivakit-serialization-core").asLibrary();
    Library kivakit_serialization_gson        = kivakit_group.library("kivakit-serialization-gson").asLibrary();
    Library kivakit_serialization_kryo        = kivakit_group.library("kivakit-serialization-kryo").asLibrary();
    Library kivakit_serialization_properties  = kivakit_group.library("kivakit-serialization-properties").asLibrary();
    Library kivakit_serialization_yaml        = kivakit_group.library("kivakit-serialization-yaml").asLibrary();
    Library kivakit_settings                  = kivakit_group.library("kivakit-settings").asLibrary();
    Library kivakit_settings_stores_zookeeper = kivakit_group.library("kivakit-settings-stores-zookeeper").asLibrary();
    Library kivakit_testing                   = kivakit_group.library("kivakit-testing").asLibrary();
    Library kivakit_validation                = kivakit_group.library("kivakit-validation").asLibrary();
    Library kivakit_web_jersey                = kivakit_group.library("kivakit-web-jersey").asLibrary();
    Library kivakit_web_jetty                 = kivakit_group.library("kivakit-web-jetty").asLibrary();
    Library kivakit_web_swagger               = kivakit_group.library("kivakit-web-swagger").asLibrary();
}
