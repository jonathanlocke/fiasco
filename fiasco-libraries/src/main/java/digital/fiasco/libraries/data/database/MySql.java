package digital.fiasco.libraries.data.database;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface MySql
{
    Library mysql_connector = library("mysql:mysql-connector-java");
}
