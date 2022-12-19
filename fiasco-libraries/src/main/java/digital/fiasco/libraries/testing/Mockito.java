package digital.fiasco.libraries.testing;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface Mockito
{
    Library mockito_all = library("org.mockito:mockito-all");
    Library mockito_core = library("org.mockito:mockito-core");
}
