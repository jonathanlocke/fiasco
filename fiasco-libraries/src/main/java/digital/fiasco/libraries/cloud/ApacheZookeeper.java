package digital.fiasco.libraries.cloud;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheZookeeper
{
    Library apache_zookeeper = library("org.apache.zookeeper:zookeeper");
}
