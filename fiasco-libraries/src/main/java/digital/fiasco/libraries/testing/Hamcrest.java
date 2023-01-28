package digital.fiasco.libraries.testing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings("unused")
public interface Hamcrest extends LibraryGroups
{
    Library hamcrest         = hamcrest_group.library("hamcrest-all").asLibrary();
    Library hamcrest_library = hamcrest_group.library("hamcrest-library").asLibrary();
}
