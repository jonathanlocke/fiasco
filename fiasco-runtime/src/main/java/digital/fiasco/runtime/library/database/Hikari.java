package digital.fiasco.runtime.library.database;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface Hikari
{
    Library hikari = library("com.zaxxer:HikariCP");
}
