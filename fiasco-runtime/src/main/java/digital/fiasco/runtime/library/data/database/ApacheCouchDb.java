package digital.fiasco.runtime.library.data.database;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheCouchDb
{
    Library apache_couch_db_ = library("org.apache.ignite:ignite-network-api");
}
