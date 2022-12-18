package digital.fiasco.runtime.dependency.oss.testing;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Powermock
{
    Library powermock_api_mockito = library("org.powermock:powermock-api-mockito");
    Library powermock_module_junit4 = library("org.powermock:powermock-module-junit4");
}
