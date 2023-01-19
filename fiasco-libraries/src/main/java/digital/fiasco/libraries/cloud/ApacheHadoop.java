package digital.fiasco.libraries.cloud;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheHadoop extends LibraryGroups
{
    Library apache_hadoop_client                = apache_hadoop_group.library("hadoop-client").asLibrary();
    Library apache_hadoop_common                = apache_hadoop_group.library("hadoop-common").asLibrary();
    Library apache_hadoop_hdfs                  = apache_hadoop_group.library("hadoop-hdfs").asLibrary();
    Library apache_hadoop_mapreduce_client_core = apache_hadoop_group.library("hadoop-mapreduce-client-core").asLibrary();
}
