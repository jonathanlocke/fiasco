package digital.fiasco.libraries.data.database;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface ApacheCouchDb
{
    Library apache_couch_db_ = library("org.apache.ignite:ignite-network-api");
}
