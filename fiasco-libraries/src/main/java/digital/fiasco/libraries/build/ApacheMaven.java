package digital.fiasco.libraries.build;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheMaven
{
    Library maven_antrun_plugin = library("org.apache.maven.plugins:maven-antrun-plugin");
    Library maven_artifact = library("org.apache.maven:maven-artifact");
    Library maven_assembly_plugin = library("org.apache.maven.plugins:maven-assembly-plugin");
    Library maven_checkstyle_plugin = library("org.apache.maven.plugins:maven-checkstyle-plugin");
    Library maven_clean_plugin = library("org.apache.maven.plugins:maven-clean-plugin");
    Library maven_compiler_plugin = library("org.apache.maven.plugins:maven-compiler-plugin");
    Library maven_core = library("org.apache.maven:maven-core");
    Library maven_dependency_plugin = library("org.apache.maven.plugins:maven-dependency-plugin");
    Library maven_deploy_plugin = library("org.apache.maven.plugins:maven-deploy-plugin");
    Library maven_enforcer_plugin = library("org.apache.maven.plugins:maven-enforcer-plugin");
    Library maven_failsafe_plugin = library("org.apache.maven.plugins:maven-failsafe-plugin");
    Library maven_gpg_plugin = library("org.apache.maven.plugins:maven-gpg-plugin");
    Library maven_jar_plugin = library("org.apache.maven.plugins:maven-jar-plugin");
    Library maven_javadoc_plugin = library("org.apache.maven.plugins:maven-javadoc-plugin");
    Library maven_project = library("org.apache.maven:maven-project");
    Library maven_release_plugin = library("org.apache.maven.plugins:maven-release-plugin");
    Library maven_resources_plugin = library("org.apache.maven.plugins:maven-resources-plugin");
    Library maven_shade_plugin = library("org.apache.maven.plugins:maven-shade-plugin");
    Library maven_site_plugin = library("org.apache.maven.plugins:maven-site-plugin");
    Library maven_source_plugin = library("org.apache.maven.plugins:maven-source-plugin");
    Library maven_surefire_plugin = library("org.apache.maven.plugins:maven-surefire-plugin");
    Library maven_war_plugin = library("org.apache.maven.plugins:maven-war-plugin");
}
