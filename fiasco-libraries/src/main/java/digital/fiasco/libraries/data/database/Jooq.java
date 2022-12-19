package digital.fiasco.libraries.data.database;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Jooq
{
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
}
