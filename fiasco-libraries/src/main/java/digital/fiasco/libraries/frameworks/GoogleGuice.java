package digital.fiasco.libraries.frameworks;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface GoogleGuice
{
    Library guice = library("com.google.inject:guice");
}
