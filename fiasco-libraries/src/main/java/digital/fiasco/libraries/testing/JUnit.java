package digital.fiasco.libraries.testing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface JUnit extends LibraryGroups
{
    Library junit         = junit_group.library("junit").asLibrary();
    Library junit5_api    = junit_jupiter_group.library("junit-jupiter-api").asLibrary();
    Library junit5_engine = junit_jupiter_group.library("junit-jupiter-engine").asLibrary();
}
