package digital.fiasco.runtime.repository;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface CommonLibraries
{
    interface framework
    {
        Library kivakit = library("com.telenav.kivakit:kivakit");
        Library kivakit_application = library("com.telenav.kivakit:kivakit-application");
        Library lombok = library("org.projectlombok:lombok");
        Library spring_beans = library("org.springframework:spring-beans");
        Library spring_boot_autoconfigure = library("org.springframework.boot:spring-boot-autoconfigure");
        Library spring_boot_configuration_processor = library("org.springframework.boot:spring-boot-configuration-processor");
        Library spring_context = library("org.springframework:spring-context");
        Library spring_core = library("org.springframework:spring-core");
        Library spring_web = library("org.springframework:spring-web");
    }

    interface languages
    {
        Library clojure = library("org.clojure:clojure");
        Library kotlin_stdlib = library("org.jetbrains.kotlin:kotlin-stdlib");
        Library kotlin_stdlib_common = library("org.jetbrains.kotlin:kotlin-stdlib-common");
        Library scala_library = library("org.scala-lang:scala-library");
    }

    interface logging
    {
        Library apache_commons_logging = library("org.apache.commons:logging");
        Library commons_logging = library("commons-logging:commons-logging");
        Library log4j = library("log4j:log4j");
        Library slf4j_api = library("org.slf4j:slf4j-api");
        Library slf4j_log4j12 = library("org.slf4j:slf4j-log4j12");
        Library slf4j_simple = library("org.slf4j:slf4j-simple");
    }

    interface network
    {
        Library http_client = library("org.apache.httpcomponents:httpclient");
        Library kivakit_network_core = library("com.telenav.kivakit:kivakit-network-core");
        Library ok_http = library("com.squareup.okhttp3:okhttp");
    }

    interface serialization
    {
        Library gson = library("com.google.code.gson:gson");
        Library jackson_core = library("com.fasterxml.jackson.core:jackson-core");
        Library jackson_databind = library("com.fasterxml.jackson.core:jackson-databind");
        Library jackson_annotations = library("com.fasterxml.jackson.core:jackson-annotations");
        Library kryo = library("com.esotericsoftware:kryo");
    }

    interface testing
    {
        Library assertj_core = library("org.assertj:assertj-core");
        Library junit = library("junit:junit");
        Library junit5_api = library("org.junit.jupiter:junit-jupiter-api");
        Library junit5_engine = library("org.junit.jupiter:junit-jupiter-engine");
        Library mockito_all = library("org.mockito:mockito-all");
        Library mockito_core = library("org.mockito:mockito-core");
        Library test_ng = library("org.testng:testng");
    }

    interface utilities
    {
        Library commons_codec = library("commons-codec:commons-codec");
        Library commons_io = library("commons-io:commons-io");
        Library commons_lang = library("commons-lang:commons-lang");
        Library commons_lang3 = library("org.apache.commons:commons-lang3");
        Library find_bugs = library("com.google.code.findbugs:jsr305");
        Library guava = library("com.google.guava:guava");
    }

    interface web
    {
        Library javax_servlet_api = library("javax.servlet:javax.servlet-api");
    }
}
