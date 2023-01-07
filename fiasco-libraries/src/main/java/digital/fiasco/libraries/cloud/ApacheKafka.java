package digital.fiasco.libraries.cloud;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheKafka extends LibraryGroups
{
    Library apache_kafka             = apache_kafka_group.library("kafka");
    Library apache_kafka_connect_api = apache_kafka_group.library("kafka-connect-api");
}
