package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface Web
{
    Library apache_click = library("org.apache.click:click");
    Library apache_tomcat_embed_core = library("org.apache.tomcat.embed:tomcat-embed-core");
    Library apache_velocity = library("org.apache.velocity:velocity");
    Library apache_wicket = library("org.apache.wicket:wicket");
    Library apache_wicket_auth_roles = library("org.apache.wicket:wicket-auth-roles");
    Library apache_wicket_core = library("org.apache.wicket:wicket-core");
    Library apache_wicket_extensions = library("org.apache.wicket:wicket-extensions");
    Library apache_wicket_request = library("org.apache.wicket:wicket-request");
    Library apache_wicket_spring = library("org.apache.wicket:wicket-spring");
    Library apache_wicket_stuff = library("org.apache.wicket:wicket-stuff");
    Library apache_wicket_util = library("org.apache.wicket:wicket-util");
    Library gwt_user = library("com.google.gwt:gwt-user");
    Library javax_servlet_api = library("javax.servlet:javax.servlet-api");
    Library ktor_client_core = library("io.ktor:ktor-client-core");
    Library lift_webkit = library("net.liftweb:lift-webkit");
    Library play = library("com.typesafe.play:play");
    Library servlet_api = library("javax.servlet:servlet-api");
    Library struts2_core = library("org.apache.struts:struts2-core");
    Library tapestry_core = library("org.apache.tapestry:tapestry-core");
    Library vaadin = library("com.vaadin:vaadin");
}
