package digital.fiasco.libraries.build;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheMaven extends LibraryGroups
{
    Library maven_core              = apache_maven_group.library("maven-core").asLibrary();
    Library maven_artifact          = apache_maven_group.library("maven-artifact").asLibrary();
    Library maven_project           = apache_maven_group.library("maven-project").asLibrary();
    Library maven_antrun_plugin     = apache_maven_plugins_group.library("maven-antrun-plugin").asLibrary();
    Library maven_assembly_plugin   = apache_maven_plugins_group.library("maven-assembly-plugin").asLibrary();
    Library maven_checkstyle_plugin = apache_maven_plugins_group.library("maven-checkstyle-plugin").asLibrary();
    Library maven_clean_plugin      = apache_maven_plugins_group.library("maven-clean-plugin").asLibrary();
    Library maven_compiler_plugin   = apache_maven_plugins_group.library("maven-compiler-plugin").asLibrary();
    Library maven_dependency_plugin = apache_maven_plugins_group.library("maven-dependency-plugin").asLibrary();
    Library maven_deploy_plugin     = apache_maven_plugins_group.library("maven-deploy-plugin").asLibrary();
    Library maven_enforcer_plugin   = apache_maven_plugins_group.library("maven-enforcer-plugin").asLibrary();
    Library maven_failsafe_plugin   = apache_maven_plugins_group.library("maven-failsafe-plugin").asLibrary();
    Library maven_gpg_plugin        = apache_maven_plugins_group.library("maven-gpg-plugin").asLibrary();
    Library maven_jar_plugin        = apache_maven_plugins_group.library("maven-jar-plugin").asLibrary();
    Library maven_javadoc_plugin    = apache_maven_plugins_group.library("maven-javadoc-plugin").asLibrary();
    Library maven_release_plugin    = apache_maven_plugins_group.library("maven-release-plugin").asLibrary();
    Library maven_resources_plugin  = apache_maven_plugins_group.library("maven-resources-plugin").asLibrary();
    Library maven_shade_plugin      = apache_maven_plugins_group.library("maven-shade-plugin").asLibrary();
    Library maven_site_plugin       = apache_maven_plugins_group.library("maven-site-plugin").asLibrary();
    Library maven_source_plugin     = apache_maven_plugins_group.library("maven-source-plugin").asLibrary();
    Library maven_surefire_plugin   = apache_maven_plugins_group.library("maven-surefire-plugin").asLibrary();
    Library maven_war_plugin        = apache_maven_plugins_group.library("maven-war-plugin").asLibrary();
}
