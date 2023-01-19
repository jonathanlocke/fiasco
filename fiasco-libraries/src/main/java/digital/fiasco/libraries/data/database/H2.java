package digital.fiasco.libraries.data.database;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface H2 extends LibraryGroups
{
    Library h2_database = h2_database_group.library("h2").asLibrary();
}
