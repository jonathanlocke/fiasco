package digital.fiasco.libraries.utilities.logging;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface Logback extends LibraryGroups
{
    Library logback_classic = logback_group.library("logback-classic").asLibrary();
    Library logback_core    = logback_group.library("logback-core").asLibrary();
}
