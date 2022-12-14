package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface Networking
{
    Library apache_httpcomponents_http_core = library("org.apache.httpcomponents:httpcore");
    Library apache_httpcomponents_http_mime = library("org.apache.httpcomponents:httpmime");
    Library http_client = library("org.apache.httpcomponents:httpclient");
    Library kivakit_network_core = library("com.telenav.kivakit:kivakit-network-core");
    Library ok_http = library("com.squareup.okhttp3:okhttp");
}
