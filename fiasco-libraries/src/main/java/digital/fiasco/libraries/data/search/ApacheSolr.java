package digital.fiasco.libraries.data.search;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface ApacheSolr
{
    Library apache_solr = library("org.apache.solr:solr");
    Library apache_solr_core = library("org.apache.solr:solr-core");
    Library apache_solr_solrj = library("org.apache.solr:solr-solrj");
    Library apache_solr_test = library("org.apache.solr:solr-test-framework");
}
