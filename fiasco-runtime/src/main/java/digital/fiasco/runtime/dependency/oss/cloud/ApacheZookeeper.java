package digital.fiasco.runtime.dependency.oss.cloud;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface ApacheZookeeper
{
    Library apache_zookeeper = library("org.apache.zookeeper:zookeeper");
}
