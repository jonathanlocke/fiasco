package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

@SuppressWarnings("unused")
public interface Search
{
    Library apache_lucene_core = Library.library("org.apache.lucene:lucene-core");
}
