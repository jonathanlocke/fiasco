package digital.fiasco.libraries.data.database;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings("unused")
public interface Hikari extends LibraryGroups
{
    Library hikari = zaxxer_group.library("HikariCP").asLibrary();
}
