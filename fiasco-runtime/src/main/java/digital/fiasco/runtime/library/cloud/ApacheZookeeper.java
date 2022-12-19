package digital.fiasco.runtime.library.cloud;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheZookeeper
{
    Library apache_zookeeper = library("org.apache.zookeeper:zookeeper");
}
