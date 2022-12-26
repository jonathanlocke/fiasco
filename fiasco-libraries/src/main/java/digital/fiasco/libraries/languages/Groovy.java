package digital.fiasco.libraries.languages;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Groovy extends LibraryGroups
{
    Library apache_groovy           = apache_groovy_group.library("groovy");
    Library apache_groovy_datetime  = apache_groovy_group.library("groovy-datetime");
    Library apache_groovy_json      = apache_groovy_group.library("groovy-json");
    Library apache_groovy_jsr_223   = apache_groovy_group.library("groovy-jsr223");
    Library apache_groovy_sh        = apache_groovy_group.library("groovy-groovysh");
    Library apache_groovy_sql       = apache_groovy_group.library("groovy-sql");
    Library apache_groovy_templates = apache_groovy_group.library("groovy-templates");
    Library apache_groovy_xml       = apache_groovy_group.library("groovy-xml");
    Library apache_groovy_yaml      = apache_groovy_group.library("groovy-yaml");
}
