package digital.fiasco.runtime.dependency.oss.search;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface ApacheLucene
{
    Library apache_lucene_core = library("org.apache.lucene:lucene-core");
}
