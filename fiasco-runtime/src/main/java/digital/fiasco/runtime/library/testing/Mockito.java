package digital.fiasco.runtime.library.testing;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface Mockito
{
    Library mockito_all = library("org.mockito:mockito-all");
    Library mockito_core = library("org.mockito:mockito-core");
}
