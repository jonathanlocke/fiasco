package digital.fiasco.libraries.utilities.logging;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheLog4j extends LibraryGroups
{
    Library apache_log4_slf4j_impl    = apache_log4j_group.library("log4j-slf4j-impl");
    Library apache_log4j_1_2_api      = apache_log4j_group.library("log4j-1.2-api");
    Library apache_log4j_api          = apache_log4j_group.library("log4j-api");
    Library apache_log4j_core         = apache_log4j_group.library("log4j-core");
    Library apache_log4j_jcl          = apache_log4j_group.library("log4j-jcl");
    Library apache_log4j_web          = apache_log4j_group.library("log4j-web");
    Library apache_logging_log4j_core = apache_log4j_group.library("log4j-core");
}
