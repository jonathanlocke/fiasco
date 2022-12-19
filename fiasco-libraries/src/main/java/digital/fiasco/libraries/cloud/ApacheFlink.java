package digital.fiasco.libraries.cloud;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheFlink
{
    Library apache_flink_core = library("org.apache.flink:flink-core");
    Library apache_flink_streaming_java_2_12 = library("org.apache.flink:streaming-java_2.12");
    Library apache_flink_connector_kafka_2_12 = library("org.apache.flink:connector-kafka_2.12");
}
