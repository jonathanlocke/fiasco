package digital.fiasco.libraries.cloud;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface ApacheKafka
{
    Library apache_kafka = library("org.apache.kafka:kafka");
    Library apache_kafka_connect_api = library("org.apache.kafka:kafka-connect-api");
}
