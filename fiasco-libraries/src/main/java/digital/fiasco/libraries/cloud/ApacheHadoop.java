package digital.fiasco.libraries.cloud;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface ApacheHadoop
{
    Library apache_hadoop_client = library("org.apache.hadoop:hadoop-client");
    Library apache_hadoop_common = library("org.apache.hadoop:hadoop-common");
    Library apache_hadoop_hdfs = library("org.apache.hadoop:hadoop-hdfs");
    Library apache_hadoop_mapreduce_client_core = library("org.apache.hadoop:hadoop-mapreduce-client-core");
}
