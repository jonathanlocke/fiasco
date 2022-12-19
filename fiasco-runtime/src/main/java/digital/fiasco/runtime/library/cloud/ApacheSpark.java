package digital.fiasco.runtime.library.cloud;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheSpark
{
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
}
