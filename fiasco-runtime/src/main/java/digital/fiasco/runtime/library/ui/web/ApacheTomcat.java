package digital.fiasco.runtime.library.ui.web;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheTomcat
{
    Library apache_tomcat_embed_core = library("org.apache.tomcat.embed:tomcat-embed-core");
}
