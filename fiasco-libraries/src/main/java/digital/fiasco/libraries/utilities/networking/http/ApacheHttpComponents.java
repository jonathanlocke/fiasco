package digital.fiasco.libraries.utilities.networking.http;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface ApacheHttpComponents extends LibraryGroups
{
    Library apache_httpcomponents_http_core   = apache_http_components_group.library("httpcore");
    Library apache_httpcomponents_http_mime   = apache_http_components_group.library("httpmime");
    Library apache_httpcomponents_http_client = apache_http_components_group.library("httpclient");
}
