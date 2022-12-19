package digital.fiasco.runtime.library.cloud;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheCamel
{
    Library apache_camel_core = library("org.apache.camel:camel-core");
    Library apache_camel_couch_db = library("org.apache.camel:camel-couchdb");
}
