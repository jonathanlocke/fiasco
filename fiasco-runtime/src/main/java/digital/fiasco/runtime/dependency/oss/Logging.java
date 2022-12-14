package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "unused" })
public interface Logging
{
    Library apache_commons_logging = library("org.apache.commons:logging");
    Library apache_log4_slf4j_impl = library("org.apache.logging.log4j:log4j-slf4j-impl");
    Library apache_log4j_1_2_api = library("org.apache.logging.log4j:log4j-1.2-api");
    Library apache_log4j_api = library("org.apache.logging.log4j:log4j-api");
    Library apache_log4j_core = library("org.apache.logging.log4j:log4j-core");
    Library apache_log4j_jcl = library("org.apache.logging.log4j:log4j-jcl");
    Library apache_log4j_web = library("org.apache.logging.log4j:log4j-web");
    Library apache_logging_log4j_core = library("org.apache.logging.log4j:log4j-core");
    Library commons_cli = library("commons-cli:commons-cli");
    Library commons_logging = library("commons-logging:commons-logging");
    Library jcl_over_slf4j = library("org.slf4j:jcl-over-slf4j");
    Library log4j = library("log4j:log4j");
    Library logback_classic = library("ch.qos.logback:logback-classic");
    Library logback_core = library("ch.qos.logback:logback-core");
    Library slf4j_api = library("org.slf4j:slf4j-api");
    Library slf4j_log4j12 = library("org.slf4j:slf4j-log4j12");
    Library slf4j_simple = library("org.slf4j:slf4j-simple");
}
