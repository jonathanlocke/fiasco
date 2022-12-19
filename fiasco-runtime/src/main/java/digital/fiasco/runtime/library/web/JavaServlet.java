package digital.fiasco.runtime.library.web;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface JavaServlet
{
    Library javax_servlet_api = library("javax.servlet:javax.servlet-api");
    Library servlet_api = library("javax.servlet:servlet-api");
}
