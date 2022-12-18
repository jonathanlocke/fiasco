package digital.fiasco.runtime.dependency.oss.testing;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Mockito
{
    Library mockito_all = library("org.mockito:mockito-all");
    Library mockito_core = library("org.mockito:mockito-core");
}
