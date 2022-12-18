package digital.fiasco.runtime.dependency.oss.java.networking.http;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface ApacheHttpComponents
{
    Library apache_httpcomponents_http_core = library("org.apache.httpcomponents:httpcore");
    Library apache_httpcomponents_http_mime = library("org.apache.httpcomponents:httpmime");
    Library apache_httpcomponents_http_client = library("org.apache.httpcomponents:httpclient");
}
