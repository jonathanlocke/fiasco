package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface Cloud
{
    Library apache_hadoop_client = library("org.apache.hadoop:hadoop-client");
    Library apache_hadoop_common = library("org.apache.hadoop:hadoop-common");
    Library apache_hadoop_hdfs = library("org.apache.hadoop:hadoop-hdfs");
    Library apache_hadoop_mapreduce_client_core = library("org.apache.hadoop:hadoop-mapreduce-client-core");
    Library apache_kafka = library("org.apache.kafka:kafka");
    Library apache_kafka_connect_api = library("org.apache.kafka:kafka-connect-api");
    Library apache_spark_avro = library("org.apache.spark:spark-avro");
    Library apache_spark_catalyst = library("org.apache.spark:spark-catalyst");
    Library apache_spark_core = library("org.apache.spark:spark-core");
    Library apache_spark_graphx = library("org.apache.spark:spark-graphx");
    Library apache_spark_hive = library("org.apache.spark:spark-hive");
    Library apache_spark_ml = library("org.apache.spark:spark-mllib");
    Library apache_spark_repl = library("org.apache.spark:spark-repl");
    Library apache_spark_sql = library("org.apache.spark:spark-sql");
    Library apache_spark_streaming = library("org.apache.spark:spark-streaming");
    Library apache_spark_tags = library("org.apache.spark:spark-tags");
    Library apache_spark_yarn = library("org.apache.spark:spark-yarn");
    Library apache_thrift = library("org.apache.thrift:libthrift");
    Library apache_zookeeper = library("org.apache.zookeeper:zookeeper");
    Library aws_jmespath = library("com.amazonaws:jmespath-java");
    Library aws_sdk_core = library("com.amazonaws:aws-java-sdk-core");
    Library aws_sdk_test_utils = library("com.amazonaws:aws-java-sdk-test-utils");
    Library software_aws_sdk_annotations = library("software.amazon.awssdk:annotations");
    Library software_aws_sdk_apache_client = library("software.amazon.awssdk:apache-client");
    Library software_aws_sdk_auth = library("software.amazon.awssdk:auth");
    Library software_aws_sdk_core = library("software.amazon.awssdk:core");
    Library software_aws_sdk_http_client_spi = library("software.amazon.awssdk:http-client-spi");
    Library software_aws_sdk_json_protocol = library("software.amazon.awssdk:aws-json-protocol");
    Library software_aws_sdk_netty_nio_client = library("software.amazon.awssdk:netty-nio-client");
    Library software_aws_sdk_protocol_core = library("software.amazon.awssdk:protocol-core");
    Library software_aws_sdk_regions = library("software.amazon.awssdk:regions");
    Library software_aws_sdk_service_test_utils = library("software.amazon.awssdk:service-test-utils");
    Library software_aws_sdk_utils = library("software.amazon.awssdk:utils");
}
