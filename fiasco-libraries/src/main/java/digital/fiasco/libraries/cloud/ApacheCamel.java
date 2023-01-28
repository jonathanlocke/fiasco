package digital.fiasco.libraries.cloud;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings("unused")
public interface ApacheCamel extends LibraryGroups
{
    Library apache_camel_core     = apache_camel_group.library("camel-core").asLibrary();
    Library apache_camel_couch_db = apache_camel_group.library("camel-couchdb").asLibrary();
}
