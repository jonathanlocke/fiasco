package digital.fiasco.libraries.data.database;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface MyBatis
{
    Library mybatis = library("org.mybatis:mybatis");
    Library mybatis_dynamic_sql = library("org.mybatis:mybatis-dynamic-sql");
    Library mybatis_generator = library("org.mybatis.generator:mybatis-generator");
    Library mybatis_generator_core = library("org.mybatis.generator:mybatis-generator-core");
    Library mybatis_generator_maven_plugin = library("org.mybatis.generator:mybatis-generator-maven-plugin");
    Library mybatis_guice = library("org.mybatis:mybatis-guice");
    Library mybatis_scripting_freemarker = library("org.mybatis.scripting:mybatis-freemarker");
    Library mybatis_scripting_thymeleaf = library("org.mybatis.scripting:mybatis-thymeleaf");
    Library mybatis_scripting_velocity = library("org.mybatis.scripting:mybatis-velocity");
    Library mybatis_spring = library("org.mybatis:mybatis-spring");
    Library mybatis_spring_boot_autoconfigure = library("org.mybatis.spring.boot:mybatis-spring-boot-autoconfigure");
    Library mybatis_spring_boot_starter = library("org.mybatis.spring.boot:mybatis-spring-boot-starter");
}
