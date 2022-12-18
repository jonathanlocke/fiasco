package digital.fiasco.runtime.dependency.oss.frameworks;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface GoogleGuice
{
    Library guice = library("com.google.inject:guice");
}
