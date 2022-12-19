package digital.fiasco.runtime.library.testing;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface Hamcrest
{
    Library hamcrest = library("org.hamcrest:hamcrest-all");
    Library hamcrest_library = library("org.hamcrest:hamcrest-library");
}
