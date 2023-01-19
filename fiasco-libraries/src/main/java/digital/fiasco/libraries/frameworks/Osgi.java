package digital.fiasco.libraries.frameworks;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface Osgi extends LibraryGroups
{
    Library osgi_core = osgi_group.library("org.osgi.core").asLibrary();
}
