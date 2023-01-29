package digital.fiasco.libraries.data.database;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface MySql extends LibraryGroups
{
    Library mysql_connector = mysql_group.library("mysql-connector-java").asLibrary();
}
