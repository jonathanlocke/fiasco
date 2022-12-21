package digital.fiasco.libraries.ui.web;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface JavaServlet
{
    Library javax_servlet_api = library("javax.servlet:javax.servlet-api");
    Library servlet_api = library("javax.servlet:servlet-api");
}
