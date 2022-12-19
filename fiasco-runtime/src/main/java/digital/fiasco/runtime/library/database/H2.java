package digital.fiasco.runtime.library.database;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface H2
{
    Library h2 = library("com.h2database:h2");
}
