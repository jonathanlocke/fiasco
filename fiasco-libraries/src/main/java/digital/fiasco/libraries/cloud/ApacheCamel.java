package digital.fiasco.libraries.cloud;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface ApacheCamel
{
    Library apache_camel_core = library("org.apache.camel:camel-core");
    Library apache_camel_couch_db = library("org.apache.camel:camel-couchdb");
}
