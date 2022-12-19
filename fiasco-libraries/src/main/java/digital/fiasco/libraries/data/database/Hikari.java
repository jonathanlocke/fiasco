package digital.fiasco.libraries.data.database;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface Hikari
{
    Library hikari = library("com.zaxxer:HikariCP");
}
