package digital.fiasco.libraries.data.database;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface H2
{
    Library h2 = library("com.h2database:h2");
}
