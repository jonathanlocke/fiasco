package digital.fiasco.libraries.data.database;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface H2
{
    Library h2 = library("com.h2database:h2");
}
