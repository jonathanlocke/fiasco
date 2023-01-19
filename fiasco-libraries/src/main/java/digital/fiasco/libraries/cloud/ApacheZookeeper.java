package digital.fiasco.libraries.cloud;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheZookeeper extends LibraryGroups
{
    Library apache_zookeeper = apache_zookeeper_group.library("zookeeper").asLibrary();
}
