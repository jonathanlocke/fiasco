package digital.fiasco.libraries.data.database;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheCouchDb
{
    Library apache_couch_db_ = library("org.apache.ignite:ignite-network-api");
}
