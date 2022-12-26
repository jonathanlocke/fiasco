package digital.fiasco.libraries.data.database;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface MyBatis extends LibraryGroups
{
    Library mybatis                           = mybatis_group.library("mybatis");
    Library mybatis_dynamic_sql               = mybatis_group.library("mybatis-dynamic-sql");
    Library mybatis_generator                 = mybatis_generator_group.library("mybatis-generator");
    Library mybatis_generator_core            = mybatis_generator_group.library("mybatis-generator-core");
    Library mybatis_generator_maven_plugin    = mybatis_generator_group.library("mybatis-generator-maven-plugin");
    Library mybatis_guice                     = mybatis_group.library("mybatis-guice");
    Library mybatis_scripting_freemarker      = mybatis_scripting_group.library("mybatis-freemarker");
    Library mybatis_scripting_thymeleaf       = mybatis_scripting_group.library("mybatis-thymeleaf");
    Library mybatis_scripting_velocity        = mybatis_scripting_group.library("mybatis-velocity");
    Library mybatis_spring                    = mybatis_group.library("mybatis-spring");
    Library mybatis_spring_boot_autoconfigure = mybatis_spring_boot_group.library("mybatis-spring-boot-autoconfigure");
    Library mybatis_spring_boot_starter       = mybatis_spring_boot_group.library("mybatis-spring-boot-starter");
}
