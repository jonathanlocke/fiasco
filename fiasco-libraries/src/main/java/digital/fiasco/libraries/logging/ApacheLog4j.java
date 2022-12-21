package digital.fiasco.libraries.logging;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface ApacheLog4j
{
    Library apache_log4_slf4j_impl = library("org.apache.logging.log4j:log4j-slf4j-impl");
    Library apache_log4j_1_2_api = library("org.apache.logging.log4j:log4j-1.2-api");
    Library apache_log4j_api = library("org.apache.logging.log4j:log4j-api");
    Library apache_log4j_core = library("org.apache.logging.log4j:log4j-core");
    Library apache_log4j_jcl = library("org.apache.logging.log4j:log4j-jcl");
    Library apache_log4j_web = library("org.apache.logging.log4j:log4j-web");
    Library apache_logging_log4j_core = library("org.apache.logging.log4j:log4j-core");
}
