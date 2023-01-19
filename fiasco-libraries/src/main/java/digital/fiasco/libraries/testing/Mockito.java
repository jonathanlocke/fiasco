package digital.fiasco.libraries.testing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface Mockito extends LibraryGroups
{
    Library mockito_all  = mockito_group.library("mockito-all").asLibrary();
    Library mockito_core = mockito_group.library("mockito-core").asLibrary();
}
