package digital.fiasco.libraries.build;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheAnt extends LibraryGroups
{
    Library apache_ant = apache_ant_group.library("ant").asLibrary();
}
