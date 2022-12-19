package digital.fiasco.runtime.library.cloud;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheKafka
{
    Library apache_kafka = library("org.apache.kafka:kafka");
    Library apache_kafka_connect_api = library("org.apache.kafka:kafka-connect-api");
}
