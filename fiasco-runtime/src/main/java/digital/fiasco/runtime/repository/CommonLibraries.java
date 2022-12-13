package digital.fiasco.runtime.repository;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface CommonLibraries
{
    interface build
    {
        Library gradle = library("com.android.tools.build:gradle");
        Library maven_antrun_plugin = library("org.apache.maven.plugins:maven-antrun-plugin");
        Library maven_assembly_plugin = library("org.apache.maven.plugins:maven-assembly-plugin");
        Library maven_checkstyle_plugin = library("org.apache.maven.plugins:maven-checkstyle-plugin");
        Library maven_clean_plugin = library("org.apache.maven.plugins:maven-clean-plugin");
        Library maven_compiler_plugin = library("org.apache.maven.plugins:maven-compiler-plugin");
        Library maven_dependency_plugin = library("org.apache.maven.plugins:maven-dependency-plugin");
        Library maven_deploy_plugin = library("org.apache.maven.plugins:maven-deploy-plugin");
        Library maven_enforcer_plugin = library("org.apache.maven.plugins:maven-enforcer-plugin");
        Library maven_failsafe_plugin = library("org.apache.maven.plugins:maven-failsafe-plugin");
        Library maven_gpg_plugin = library("org.apache.maven.plugins:maven-gpg-plugin");
        Library maven_jar_plugin = library("org.apache.maven.plugins:maven-jar-plugin");
        Library maven_javadoc_plugin = library("org.apache.maven.plugins:maven-javadoc-plugin");
        Library maven_release_plugin = library("org.apache.maven.plugins:maven-release-plugin");
        Library maven_resources_plugin = library("org.apache.maven.plugins:maven-resources-plugin");
        Library maven_shade_plugin = library("org.apache.maven.plugins:maven-shade-plugin");
        Library maven_site_plugin = library("org.apache.maven.plugins:maven-site-plugin");
        Library maven_source_plugin = library("org.apache.maven.plugins:maven-source-plugin");
        Library maven_surefire_plugin = library("org.apache.maven.plugins:maven-surefire-plugin");
        Library maven_war_plugin = library("org.apache.maven.plugins:maven-war-plugin");
    }

    interface cloud
    {
        Library aws_jmespath = library("com.amazonaws:jmespath-java");
        Library aws_sdk_core = library("com.amazonaws:aws-java-sdk-core");
        Library aws_sdk_test_utils = library("com.amazonaws:aws-java-sdk-test-utils");
        Library hadoop_common = library("org.apache.hadoop:hadoop-common");
        Library hadoop_hdfs = library("org.apache.hadoop:hadoop-hdfs");
        Library software_aws_sdk_annotations = library("software.amazon.awssdk:annotations");
        Library software_aws_sdk_core = library("software.amazon.awssdk:core");
        Library software_aws_sdk_regions = library("software.amazon.awssdk:regions");
        Library software_aws_sdk_netty_nio_client = library("software.amazon.awssdk:netty-nio-client");
        Library software_aws_sdk_http_client_spi = library("software.amazon.awssdk:http-client-spi");
        Library software_aws_sdk_apache_client = library("software.amazon.awssdk:apache-client");
        Library software_aws_sdk_utils = library("software.amazon.awssdk:utils");
        Library software_aws_sdk_auth = library("software.amazon.awssdk:auth");
        Library software_aws_sdk_json_protocol = library("software.amazon.awssdk:aws-json-protocol");
        Library software_aws_sdk_service_test_utils = library("software.amazon.awssdk:service-test-utils");
        Library software_aws_sdk_protocol_core = library("software.amazon.awssdk:protocol-core");
    }

    interface concurrency
    {
        Library rxjava = library("io.reactivex.rxjava2:rxjava");
    }

    interface database
    {
        Library h2 = library("com.h2database:h2");
        Library mysql_connector = library("mysql:mysql-connector-java");
        Library hibernate_core = library("org.hibernate:hibernate-core");
    }

    interface framework
    {
        Library google_services = library("com.google.gms:google-services");
        Library guice = library("com.google.inject:guice");
        Library javax_annotation_api = library("Javax.annotation-api");
        Library kivakit = library("com.telenav.kivakit:kivakit");
        Library kivakit_application = library("com.telenav.kivakit:kivakit-application");
        Library kotlin_gradle_plugin = library("org.jetbrains.kotlin:kotlin-gradle-plugin");
        Library lombok = library("org.projectlombok:lombok");
        Library osgi_core = library("org.osgi:org.osgi.core");
        Library spring_beans = library("org.springframework:spring-beans");
        Library spring_boot_autoconfigure = library("org.springframework.boot:spring-boot-autoconfigure");
        Library spring_boot_configuration_processor = library("org.springframework.boot:spring-boot-configuration-processor");
        Library spring_boot_devtools = library("org.springframework.boot:spring-boot-devtools");
        Library spring_boot_starter_actuator = library("org.springframework.boot:spring-boot-starter-actuator");
        Library spring_context = library("org.springframework:spring-context");
        Library spring_core = library("org.springframework:spring-core");
        Library spring_jdbc = library("org.springframework:spring-web");
        Library spring_test = library("org.springframework:spring-test");
        Library spring_tx = library("org.springframework:spring-tx");
        Library spring_web = library("org.springframework:spring-jdbc");
        Library spring_web_mvc = library("org.springframework:spring-webmvc");
        Library string_boot_starter = library("org.springframework.boot:spring-boot-starter");
        Library string_boot_starter_data_jpa = library("org.springframework.boot:spring-boot-starter-data-jpa");
        Library string_boot_starter_test = library("org.springframework.boot:spring-boot-starter-test");
        Library string_boot_starter_web = library("org.springframework.boot:spring-boot-starter-web");
    }

    interface languages
    {
        Library clojure = library("org.clojure:clojure");
        Library clojure_complete = library("clojure-complete_clojure-complete");
        Library groovy_all = library("org.codehaus.groovy:groovy-all");
        Library kotlin_stdlib = library("org.jetbrains.kotlin:kotlin-stdlib");
        Library kotlin_stdlib_common = library("org.jetbrains.kotlin:kotlin-stdlib-common");
        Library scala_library = library("org.scala-lang:scala-library");
        Library scala_reflect = library("org.scala-lang:scala-reflect");
        Library scala_test = library("org.scalatest:scalatest");
    }

    interface logging
    {
        Library apache_commons_logging = library("org.apache.commons:logging");
        Library apache_logging_log4j_core = library("org.apache.logging.log4j:log4j-core");
        Library commons_cli = library("commons-cli:commons-cli");
        Library commons_logging = library("commons-logging:commons-logging");
        Library jcl_over_slf4j = library("org.slf4j:jcl-over-slf4j");
        Library log4_slf4j_impl = library("org.apache.logging.log4j:log4j-slf4j-impl");
        Library log4j = library("log4j:log4j");
        Library logback_classic = library("ch.qos.logback:logback-classic");
        Library logback_core = library("ch.qos.logback:logback-core");
        Library slf4j_api = library("org.slf4j:slf4j-api");
        Library slf4j_log4j12 = library("org.slf4j:slf4j-log4j12");
        Library slf4j_simple = library("org.slf4j:slf4j-simple");
    }

    interface network
    {
        Library apache_httpcomponents_httpcore = library("org.apache.httpcomponents:httpcore");
        Library http_client = library("org.apache.httpcomponents:httpclient");
        Library kivakit_network_core = library("com.telenav.kivakit:kivakit-network-core");
        Library ok_http = library("com.squareup.okhttp3:okhttp");
    }

    interface parsing
    {
        Library antlr = library("org.antlr:antlr");
        Library antlr_complete = library("org.antlr:antlr-complete");
        Library antlr4 = library("org.antlr:antlr4");
        Library antlr4_annotaions = library("org.antlr:antlr4-annotaions");
        Library antlr4_maven_plugin = library("org.antlr:antlr4-maven-plugin");
        Library antlr4_runtime = library("org.antlr:antlr4-runtime");
        Library antlr_runtime = library("org.antlr:antlr-runtime");
    }

    interface serialization
    {
        Library gson = library("com.google.code.gson:gson");
        Library json = library("org.json:json");
        Library jackson_core = library("com.fasterxml.jackson.core:jackson-core");
        Library jackson_databind = library("com.fasterxml.jackson.core:jackson-databind");
        Library jackson_annotations = library("com.fasterxml.jackson.core:jackson-annotations");
        Library kryo = library("com.esotericsoftware:kryo");
        Library protobuf = library("com.google.protobuf:protobuf-java");
    }

    interface testing
    {
        Library assertj_core = library("org.assertj:assertj-core");
        Library easymock = library("org.easymock:easymock");
        Library hamcrest = library("org.hamcrest:hamcrest-all");
        Library hamcrest_library = library("org.hamcrest:hamcrest-library");
        Library junit = library("junit:junit");
        Library junit5_api = library("org.junit.jupiter:junit-jupiter-api");
        Library junit5_engine = library("org.junit.jupiter:junit-jupiter-engine");
        Library mockito_all = library("org.mockito:mockito-all");
        Library mockito_core = library("org.mockito:mockito-core");
        Library test_ng = library("org.testng:testng");
        Library wiremock = library("com.github.tomakehurst:wiremock");
        Library powermock_module_junit4 = library("org.powermock:powermock-module-junit4");
        Library powermock_api_mockito = library("org.powermock:powermock-api-mockito");
    }

    interface utilities
    {
        Library commons_beanutils = library("commons-beanutils:commons-beanutils");
        Library commons_codec = library("commons-codec:commons-codec");
        Library commons_io = library("commons-io:commons-io");
        Library commons_collections = library("commons-collections:commons-collections");
        Library commons_lang = library("commons-lang:commons-lang");
        Library commons_lang3 = library("org.apache.commons:commons-lang3");
        Library find_bugs = library("com.google.code.findbugs:jsr305");
        Library guava = library("com.google.guava:guava");
        Library joda_time = library("joda-time:joda-time");
        Library javax_validation_api = library("javax.validation:validation-api");
        Library javax_inject = library("javax.inject:javax.inject");
    }

    interface web
    {
        Library apache_click = library("org.apache.click:click");
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
}
