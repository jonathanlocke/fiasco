package digital.fiasco.libraries.utilities.processes;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings("unused")
public interface ApacheCommonsExec extends LibraryGroups
{
    Library apache_commons_exec = apache_commons_group.library("commons-exec").asLibrary();
}
