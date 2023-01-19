package digital.fiasco.libraries.data.search;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheLucene extends LibraryGroups
{
    Library apache_lucene_analyzers         = apache_lucene_group.library("lucene-analyzers").asLibrary();
    Library apache_lucene_analyzers_common  = apache_lucene_group.library("lucene-analyzers-common").asLibrary();
    Library apache_lucene_core              = apache_lucene_group.library("lucene-core").asLibrary();
    Library apache_lucene_core_query_parser = apache_lucene_group.library("lucene-query-parser").asLibrary();
    Library apache_lucene_highlighter       = apache_lucene_group.library("lucene-highlighter").asLibrary();
    Library apache_lucene_memory            = apache_lucene_group.library("lucene-memory").asLibrary();
    Library apache_lucene_misc              = apache_lucene_group.library("lucene-misc").asLibrary();
    Library apache_lucene_queries           = apache_lucene_group.library("lucene-queries").asLibrary();
    Library apache_lucene_spatial           = apache_lucene_group.library("lucene-spatial").asLibrary();
}
