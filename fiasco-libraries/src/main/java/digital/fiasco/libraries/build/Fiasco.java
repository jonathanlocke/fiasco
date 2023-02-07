package digital.fiasco.libraries.build;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface Fiasco extends LibraryGroups
{
    Library fiasco_runtime   = fiasco_group.library("fiasco-runtime").asLibrary();
    Library fiasco_libraries = fiasco_group.library("fiasco-libraries").asLibrary();
}
