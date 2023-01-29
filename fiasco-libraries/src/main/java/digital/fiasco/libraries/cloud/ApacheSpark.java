package digital.fiasco.libraries.cloud;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface ApacheSpark extends LibraryGroups
{
    Library apache_spark_avro      = apache_spark_group.library("spark-avro").asLibrary();
    Library apache_spark_catalyst  = apache_spark_group.library("spark-catalyst").asLibrary();
    Library apache_spark_core      = apache_spark_group.library("spark-core").asLibrary();
    Library apache_spark_graphx    = apache_spark_group.library("spark-graphx").asLibrary();
    Library apache_spark_hive      = apache_spark_group.library("spark-hive").asLibrary();
    Library apache_spark_ml        = apache_spark_group.library("spark-mllib").asLibrary();
    Library apache_spark_repl      = apache_spark_group.library("spark-repl").asLibrary();
    Library apache_spark_sql       = apache_spark_group.library("spark-sql").asLibrary();
    Library apache_spark_streaming = apache_spark_group.library("spark-streaming").asLibrary();
    Library apache_spark_tags      = apache_spark_group.library("spark-tags").asLibrary();
    Library apache_spark_yarn      = apache_spark_group.library("spark-yarn").asLibrary();
}
