package digital.fiasco.libraries.data.database;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Jooq extends LibraryGroups
{
    Library jool                     = jooq_group.library("jool").asLibrary();
    Library jool_java8               = jooq_group.library("jool-java8").asLibrary();
    Library jooq                     = jooq_group.library("jooq").asLibrary();
    Library jooq_checker             = jooq_group.library("jooq-checker").asLibrary();
    Library jooq_codegen             = jooq_group.library("jooq-codegen").asLibrary();
    Library jooq_codegen_maven       = jooq_group.library("jooq-codegen-maven").asLibrary();
    Library jooq_joor                = jooq_group.library("joor").asLibrary();
    Library jooq_joor_java8          = jooq_group.library("joor-java8").asLibrary();
    Library jooq_joou                = jooq_group.library("joou").asLibrary();
    Library jooq_joox                = jooq_group.library("joox").asLibrary();
    Library jooq_kotlin              = jooq_group.library("jooq-kotlin").asLibrary();
    Library jooq_meta                = jooq_group.library("jooq-meta").asLibrary();
    Library jooq_meta_extensions     = jooq_group.library("jooq-meta-extensions").asLibrary();
    Library jooq_postgres_extensions = jooq_group.library("jooq-postgres-extensions").asLibrary();
    Library jooq_scala               = jooq_group.library("jooq-scala").asLibrary();
}
