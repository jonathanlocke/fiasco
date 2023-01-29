package digital.fiasco.libraries.ui.web;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface ApacheWicket extends LibraryGroups
{
    Library apache_wicket            = apache_wicket_group.library("wicket").asLibrary();
    Library apache_wicket_auth_roles = apache_wicket_group.library("wicket-auth-roles").asLibrary();
    Library apache_wicket_core       = apache_wicket_group.library("wicket-core").asLibrary();
    Library apache_wicket_extensions = apache_wicket_group.library("wicket-extensions").asLibrary();
    Library apache_wicket_request    = apache_wicket_group.library("wicket-request").asLibrary();
    Library apache_wicket_spring     = apache_wicket_group.library("wicket-spring").asLibrary();
    Library apache_wicket_stuff      = apache_wicket_group.library("wicket-stuff").asLibrary();
    Library apache_wicket_util       = apache_wicket_group.library("wicket-util").asLibrary();
}
