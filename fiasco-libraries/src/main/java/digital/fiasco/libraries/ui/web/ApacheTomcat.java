package digital.fiasco.libraries.ui.web;

import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

import static digital.fiasco.runtime.dependency.artifact.artifacts.Library.library;

@SuppressWarnings("unused")
public interface ApacheTomcat
{
    Library apache_tomcat_embed_core = library("org.apache.tomcat.embed:tomcat-embed-core");
}
