package digital.fiasco.libraries.testing;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface Mockito
{
    Library mockito_all = library("org.mockito:mockito-all");
    Library mockito_core = library("org.mockito:mockito-core");
}
