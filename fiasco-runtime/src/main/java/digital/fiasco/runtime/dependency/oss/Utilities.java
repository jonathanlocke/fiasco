package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface Utilities
{
    Library apache_commons_collection4 = library("org.apache.commons:commons-collections4");
    Library apache_commons_csv = library("org.apache.commons:commons-csv");
    Library apache_commons_exec = library("org.apache.commons:commons-exec");
    Library apache_commons_math3 = library("org.apache.commons:commons-math3");
    Library apache_commons_text = library("org.apache.commons:commons-text");
    Library commons_beanutils = library("commons-beanutils:commons-beanutils");
    Library commons_codec = library("commons-codec:commons-codec");
    Library commons_collections = library("commons-collections:commons-collections");
    Library commons_cli = library("commons-cli:commons-cli");
    Library commons_io = library("commons-io:commons-io");
    Library commons_lang = library("commons-lang:commons-lang");
    Library commons_lang3 = library("org.apache.commons:commons-lang3");
    Library commons_logging = library("commons-logging:commons-logging");
    Library find_bugs = library("com.google.code.findbugs:jsr305");
    Library guava = library("com.google.guava:guava");
    Library javax_inject = library("javax.inject:javax.inject");
    Library javax_validation_api = library("javax.validation:validation-api");
    Library joda_time = library("joda-time:joda-time");
}
