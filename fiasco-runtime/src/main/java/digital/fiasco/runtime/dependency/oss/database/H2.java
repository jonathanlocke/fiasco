package digital.fiasco.runtime.dependency.oss.database;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface H2
{
    Library h2 = library("com.h2database:h2");
}
