package digital.fiasco.libraries.data.database;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings("unused")
public interface MyBatis extends LibraryGroups
{
    Library mybatis                           = mybatis_group.library("mybatis").asLibrary();
    Library mybatis_dynamic_sql               = mybatis_group.library("mybatis-dynamic-sql").asLibrary();
    Library mybatis_generator                 = mybatis_generator_group.library("mybatis-generator").asLibrary();
    Library mybatis_generator_core            = mybatis_generator_group.library("mybatis-generator-core").asLibrary();
    Library mybatis_generator_maven_plugin    = mybatis_generator_group.library("mybatis-generator-maven-plugin").asLibrary();
    Library mybatis_guice                     = mybatis_group.library("mybatis-guice").asLibrary();
    Library mybatis_scripting_freemarker      = mybatis_scripting_group.library("mybatis-freemarker").asLibrary();
    Library mybatis_scripting_thymeleaf       = mybatis_scripting_group.library("mybatis-thymeleaf").asLibrary();
    Library mybatis_scripting_velocity        = mybatis_scripting_group.library("mybatis-velocity").asLibrary();
    Library mybatis_spring                    = mybatis_group.library("mybatis-spring").asLibrary();
    Library mybatis_spring_boot_autoconfigure = mybatis_spring_boot_group.library("mybatis-spring-boot-autoconfigure").asLibrary();
    Library mybatis_spring_boot_starter       = mybatis_spring_boot_group.library("mybatis-spring-boot-starter").asLibrary();
}
