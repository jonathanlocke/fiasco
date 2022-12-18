package digital.fiasco.runtime.dependency.oss.web;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface ApacheTomcat
{
    Library apache_tomcat_embed_core = library("org.apache.tomcat.embed:tomcat-embed-core");
}
