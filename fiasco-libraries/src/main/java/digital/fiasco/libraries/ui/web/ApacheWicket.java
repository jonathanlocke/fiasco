package digital.fiasco.libraries.ui.web;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface ApacheWicket
{
    Library apache_wicket = library("org.apache.wicket:wicket");
    Library apache_wicket_auth_roles = library("org.apache.wicket:wicket-auth-roles");
    Library apache_wicket_core = library("org.apache.wicket:wicket-core");
    Library apache_wicket_extensions = library("org.apache.wicket:wicket-extensions");
    Library apache_wicket_request = library("org.apache.wicket:wicket-request");
    Library apache_wicket_spring = library("org.apache.wicket:wicket-spring");
    Library apache_wicket_stuff = library("org.apache.wicket:wicket-stuff");
    Library apache_wicket_util = library("org.apache.wicket:wicket-util");
}
