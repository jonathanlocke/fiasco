package digital.fiasco.runtime.library.frameworks;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface GoogleGuice
{
    Library guice = library("com.google.inject:guice");
}
