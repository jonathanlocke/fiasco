package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Apache
{
    Library apache_ant = library("org.apache.ant:ant");
    Library apache_axis2 = Library.library("org.apache.axis2.wso2:axis2");
    Library apache_camel = library("org.apache.camel:camel_core");
    Library apache_click = library("org.apache.click:click");
    Library apache_commons_collection4 = library("org.apache.commons:commons-collections4");
    Library apache_commons_compress = library("org.apache.commons:commons-compress");
    Library apache_commons_configuration2 = library("org.apache.commons:commons-configuration2");
    Library apache_commons_dbcp2 = library("org.apache.commons:commons-dbcp2");
    Library apache_commons_email = library("org.apache.commons:commons-email");
    Library apache_commons_logging = library("org.apache.commons:logging");
    Library apache_commons_math = library("org.apache.commons:commons-math");
    Library apache_commons_math3 = library("org.apache.commons:commons-math3");
    Library apache_commons_text = library("org.apache.commons:commons-text");
    Library apache_derby = library("org.apache.derby:derby");
    Library apache_hadoop_common = library("org.apache.hadoop:hadoop-common");
    Library apache_hadoop_hdfs = library("org.apache.hadoop:hadoop-hdfs");
    Library apache_hive = library("org.apache.hive:hive-metastore");
    Library apache_jdbc = library("org.apache.hive:hive-jdbc");
    Library apache_kafka = library("org.apache.kafka:kafka");
    Library apache_log4_slf4j_impl = library("org.apache.logging.log4j:log4j-slf4j-impl");
    Library apache_log4j_1_2_api = library("org.apache.logging.log4j:log4j-1.2-api");
    Library apache_log4j_api = library("org.apache.logging.log4j:log4j-api");
    Library apache_log4j_core = library("org.apache.logging.log4j:log4j-core");
    Library apache_log4j_jcl = library("org.apache.logging.log4j:log4j-jcl");
    Library apache_log4j_web = library("org.apache.logging.log4j:log4j-web");
    Library apache_logging_log4j_core = library("org.apache.logging.log4j:log4j-core");
    Library apache_lucene_core = Library.library("org.apache.lucene:lucene-core");
    Library apache_spark_avro = library("org.apache.spark:spark-avro");
    Library apache_spark_catalyst = library("org.apache.spark:spark-catalyst");
    Library apache_spark_core = library("org.apache.spark:spark-core");
    Library apache_spark_graphx = library("org.apache.spark:spark-graphx");
    Library apache_spark_hive = library("org.apache.spark:spark-hive");
    Library apache_spark_ml = library("org.apache.spark:spark-mllib");
    Library apache_spark_repl = library("org.apache.spark:spark-repl");
    Library apache_spark_sql = library("org.apache.spark:spark-sql");
    Library apache_spark_streaming = library("org.apache.spark:spark-streaming");
    Library apache_spark_tags = library("org.apache.spark:spark-tags");
    Library apache_spark_yarn = library("org.apache.spark:spark-yarn");
    Library apache_thrift = library("org.apache.thrift:libthrift");
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
    Library apache_zookeeper = library("org.apache.zookeeper:zookeeper");
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
