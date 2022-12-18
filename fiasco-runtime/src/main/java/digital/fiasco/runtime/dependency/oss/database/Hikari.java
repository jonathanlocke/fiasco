package digital.fiasco.runtime.dependency.oss.database;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Hikari
{
    Library hikari = library("com.zaxxer:HikariCP");
}
