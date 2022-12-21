package digital.fiasco.libraries.frameworks;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface GoogleGuice
{
    Library guice = library("com.google.inject:guice");
}
