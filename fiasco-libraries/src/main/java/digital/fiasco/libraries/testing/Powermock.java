package digital.fiasco.libraries.testing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings("unused")
public interface Powermock extends LibraryGroups
{
    Library powermock_api_mockito   = powermock_group.library("powermock-api-mockito").asLibrary();
    Library powermock_module_junit4 = powermock_group.library("powermock-module-junit4").asLibrary();
}
