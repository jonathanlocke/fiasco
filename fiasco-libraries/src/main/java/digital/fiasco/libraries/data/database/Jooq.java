package digital.fiasco.libraries.data.database;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Jooq extends LibraryGroups
{
    Library jool                     = jooq_group.library("jool");
    Library jool_java8               = jooq_group.library("jool-java8");
    Library jooq                     = jooq_group.library("jooq");
    Library jooq_checker             = jooq_group.library("jooq-checker");
    Library jooq_codegen             = jooq_group.library("jooq-codegen");
    Library jooq_codegen_maven       = jooq_group.library("jooq-codegen-maven");
    Library jooq_joor                = jooq_group.library("joor");
    Library jooq_joor_java8          = jooq_group.library("joor-java8");
    Library jooq_joou                = jooq_group.library("joou");
    Library jooq_joox                = jooq_group.library("joox");
    Library jooq_kotlin              = jooq_group.library("jooq-kotlin");
    Library jooq_meta                = jooq_group.library("jooq-meta");
    Library jooq_meta_extensions     = jooq_group.library("jooq-meta-extensions");
    Library jooq_postgres_extensions = jooq_group.library("jooq-postgres-extensions");
    Library jooq_scala               = jooq_group.library("jooq-scala");
}
