package digital.fiasco.libraries.data.search;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface ElasticSearch
{
    Library elastic_search = library("org.elasticsearch:elasticsearch");
    Library elastic_search_client_netty = library("org.elasticsearch.plugin:transport-netty4-client");
    Library elastic_search_client_transport = library("org.elasticsearch.client:transport");
    Library elastic_search_rest = library("org.elasticsearch.client:rest");
    Library elastic_search_rest_client = library("org.elasticsearch.client:elasticsearch-rest-client");
    Library elastic_search_simple_rest_client = library("org.elasticsearch.client:elasticsearch-rest-high-level-client");
}
