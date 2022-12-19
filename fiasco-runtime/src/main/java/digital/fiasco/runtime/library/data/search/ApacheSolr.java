package digital.fiasco.runtime.library.data.search;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheSolr
{
    Library apache_solr = library("org.apache.solr:solr");
    Library apache_solr_core = library("org.apache.solr:solr-core");
    Library apache_solr_solrj = library("org.apache.solr:solr-solrj");
    Library apache_solr_test = library("org.apache.solr:solr-test-framework");
}
