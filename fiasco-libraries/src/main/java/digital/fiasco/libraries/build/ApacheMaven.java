package digital.fiasco.libraries.build;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheMaven extends LibraryGroups
{
    Library maven_core              = apache_maven_group.library("maven-core");
    Library maven_artifact          = apache_maven_group.library("maven-artifact");
    Library maven_project           = apache_maven_group.library("maven-project");
    Library maven_antrun_plugin     = apache_maven_plugins_group.library("maven-antrun-plugin");
    Library maven_assembly_plugin   = apache_maven_plugins_group.library("maven-assembly-plugin");
    Library maven_checkstyle_plugin = apache_maven_plugins_group.library("maven-checkstyle-plugin");
    Library maven_clean_plugin      = apache_maven_plugins_group.library("maven-clean-plugin");
    Library maven_compiler_plugin   = apache_maven_plugins_group.library("maven-compiler-plugin");
    Library maven_dependency_plugin = apache_maven_plugins_group.library("maven-dependency-plugin");
    Library maven_deploy_plugin     = apache_maven_plugins_group.library("maven-deploy-plugin");
    Library maven_enforcer_plugin   = apache_maven_plugins_group.library("maven-enforcer-plugin");
    Library maven_failsafe_plugin   = apache_maven_plugins_group.library("maven-failsafe-plugin");
    Library maven_gpg_plugin        = apache_maven_plugins_group.library("maven-gpg-plugin");
    Library maven_jar_plugin        = apache_maven_plugins_group.library("maven-jar-plugin");
    Library maven_javadoc_plugin    = apache_maven_plugins_group.library("maven-javadoc-plugin");
    Library maven_release_plugin    = apache_maven_plugins_group.library("maven-release-plugin");
    Library maven_resources_plugin  = apache_maven_plugins_group.library("maven-resources-plugin");
    Library maven_shade_plugin      = apache_maven_plugins_group.library("maven-shade-plugin");
    Library maven_site_plugin       = apache_maven_plugins_group.library("maven-site-plugin");
    Library maven_source_plugin     = apache_maven_plugins_group.library("maven-source-plugin");
    Library maven_surefire_plugin   = apache_maven_plugins_group.library("maven-surefire-plugin");
    Library maven_war_plugin        = apache_maven_plugins_group.library("maven-war-plugin");
}
