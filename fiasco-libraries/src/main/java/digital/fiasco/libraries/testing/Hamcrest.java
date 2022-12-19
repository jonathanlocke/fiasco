package digital.fiasco.libraries.testing;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface Hamcrest
{
    Library hamcrest = library("org.hamcrest:hamcrest-all");
    Library hamcrest_library = library("org.hamcrest:hamcrest-library");
}
