package digital.fiasco.libraries.cloud;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheSpark extends LibraryGroups
{
    Library apache_spark_avro      = apache_spark_group.library("spark-avro");
    Library apache_spark_catalyst  = apache_spark_group.library("spark-catalyst");
    Library apache_spark_core      = apache_spark_group.library("spark-core");
    Library apache_spark_graphx    = apache_spark_group.library("spark-graphx");
    Library apache_spark_hive      = apache_spark_group.library("spark-hive");
    Library apache_spark_ml        = apache_spark_group.library("spark-mllib");
    Library apache_spark_repl      = apache_spark_group.library("spark-repl");
    Library apache_spark_sql       = apache_spark_group.library("spark-sql");
    Library apache_spark_streaming = apache_spark_group.library("spark-streaming");
    Library apache_spark_tags      = apache_spark_group.library("spark-tags");
    Library apache_spark_yarn      = apache_spark_group.library("spark-yarn");
}
