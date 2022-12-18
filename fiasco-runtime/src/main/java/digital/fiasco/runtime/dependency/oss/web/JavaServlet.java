package digital.fiasco.runtime.dependency.oss.web;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface JavaServlet
{
    Library javax_servlet_api = library("javax.servlet:javax.servlet-api");
    Library servlet_api = library("javax.servlet:servlet-api");
}
