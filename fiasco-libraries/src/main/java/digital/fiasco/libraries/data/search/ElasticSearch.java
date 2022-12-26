package digital.fiasco.libraries.data.search;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ElasticSearch extends LibraryGroups
{
    Library elastic_search                     = elastic_search_group.library("elasticsearch");
    Library elastic_search_client_netty_plugin = elastic_search_plugin_group.library("transport-netty4-client");
    Library elastic_search_client_transport    = elastic_search_client_group.library("transport");
    Library elastic_search_rest                = elastic_search_client_group.library("rest");
    Library elastic_search_rest_client         = elastic_search_client_group.library("elasticsearch-rest-client");
    Library elastic_search_simple_rest_client  = elastic_search_client_group.library("elasticsearch-rest-high-level-client");
}
