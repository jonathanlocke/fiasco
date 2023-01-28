package digital.fiasco.libraries.utilities.logging;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings("unused")
public interface ApacheLog4j extends LibraryGroups
{
    Library apache_log4_slf4j_impl    = apache_log4j_group.library("log4j-slf4j-impl").asLibrary();
    Library apache_log4j_1_2_api      = apache_log4j_group.library("log4j-1.2-api").asLibrary();
    Library apache_log4j_api          = apache_log4j_group.library("log4j-api").asLibrary();
    Library apache_log4j_core         = apache_log4j_group.library("log4j-core").asLibrary();
    Library apache_log4j_jcl          = apache_log4j_group.library("log4j-jcl").asLibrary();
    Library apache_log4j_web          = apache_log4j_group.library("log4j-web").asLibrary();
    Library apache_logging_log4j_core = apache_log4j_group.library("log4j-core").asLibrary();
}
