package digital.fiasco.libraries.ui.web;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface JavaServlet extends LibraryGroups
{
    Library javax_servlet_api = javax_servlet_group.library("javax.servlet-api").asLibrary();
    Library servlet_api       = javax_servlet_group.library("servlet-api").asLibrary();
}
