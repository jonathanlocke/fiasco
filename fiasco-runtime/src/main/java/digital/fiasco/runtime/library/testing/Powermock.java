package digital.fiasco.runtime.library.testing;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface Powermock
{
    Library powermock_api_mockito = library("org.powermock:powermock-api-mockito");
    Library powermock_module_junit4 = library("org.powermock:powermock-module-junit4");
}
