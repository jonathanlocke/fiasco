package digital.fiasco.runtime.dependency.oss.cloud;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface ApacheKafka
{
    Library apache_kafka = library("org.apache.kafka:kafka");
    Library apache_kafka_connect_api = library("org.apache.kafka:kafka-connect-api");
}
