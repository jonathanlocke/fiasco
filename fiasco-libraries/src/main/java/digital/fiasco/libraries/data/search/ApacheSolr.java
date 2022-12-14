package digital.fiasco.libraries.data.search;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheSolr extends LibraryGroups
{
    Library apache_solr       = apache_solr_group.library("solr");
    Library apache_solr_core  = apache_solr_group.library("solr-core");
    Library apache_solr_solrj = apache_solr_group.library("solr-solrj");
    Library apache_solr_test  = apache_solr_group.library("solr-test-framework");
}
