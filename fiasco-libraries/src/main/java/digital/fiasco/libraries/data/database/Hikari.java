package digital.fiasco.libraries.data.database;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.ArtifactGroup;
import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.ArtifactGroup.group;
import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface Hikari extends LibraryGroups
{
    Library hikari = zaxxer_group.library("HikariCP");
}
