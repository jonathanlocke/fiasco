package digital.fiasco.libraries.cloud;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheFlink extends LibraryGroups
{
    Library apache_flink_core                 = apache_flink_group.library("flink-core").asLibrary();
    Library apache_flink_streaming_java_2_12  = apache_flink_group.library("streaming-java_2.12").asLibrary();
    Library apache_flink_connector_kafka_2_12 = apache_flink_group.library("connector-kafka_2.12").asLibrary();
}
