package digital.fiasco.runtime.library.database;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface MySql
{
    Library mysql_connector = library("mysql:mysql-connector-java");
}
