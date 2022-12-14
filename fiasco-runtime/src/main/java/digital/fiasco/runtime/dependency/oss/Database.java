package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface Database
{
    Library h2 = library("com.h2database:h2");
    Library hibernate_core = library("org.hibernate:hibernate-core");
    Library hikari = library("com.zaxxer:HikariCP");
    Library jool = library("org.jooq:jool");
    Library jool_java8 = library("org.jooq:jool-java8");
    Library jooq = library("org.jooq:jooq");
    Library jooq_checker = library("org.jooq:jooq-checker");
    Library jooq_codegen = library("org.jooq:jooq-codegen");
    Library jooq_codegen_maven = library("org.jooq:jooq-codegen-maven");
    Library jooq_joor = library("org.jooq:joor");
    Library jooq_joor_java8 = library("org.jooq:joor-java8");
    Library jooq_joou = library("org.jooq:joou");
    Library jooq_joox = library("org.jooq:joox");
    Library jooq_kotlin = library("org.jooq:jooq-kotlin");
    Library jooq_meta = library("org.jooq:jooq-meta");
    Library jooq_meta_extensions = library("org.jooq:jooq-meta-extensions");
    Library jooq_postgres_extensions = library("org.jooq:jooq-postgres-extensions");
    Library jooq_scala = library("org.jooq:jooq-scala");
    Library mysql_connector = library("mysql:mysql-connector-java");
}
