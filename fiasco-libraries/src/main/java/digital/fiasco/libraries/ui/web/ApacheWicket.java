package digital.fiasco.libraries.ui.web;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheWicket extends LibraryGroups
{
    Library apache_wicket            = apache_wicket_group.library("wicket");
    Library apache_wicket_auth_roles = apache_wicket_group.library("wicket-auth-roles");
    Library apache_wicket_core       = apache_wicket_group.library("wicket-core");
    Library apache_wicket_extensions = apache_wicket_group.library("wicket-extensions");
    Library apache_wicket_request    = apache_wicket_group.library("wicket-request");
    Library apache_wicket_spring     = apache_wicket_group.library("wicket-spring");
    Library apache_wicket_stuff      = apache_wicket_group.library("wicket-stuff");
    Library apache_wicket_util       = apache_wicket_group.library("wicket-util");
}
