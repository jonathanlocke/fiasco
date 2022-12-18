package digital.fiasco.runtime.dependency.oss.testing;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Hamcrest
{
    Library hamcrest = library("org.hamcrest:hamcrest-all");
    Library hamcrest_library = library("org.hamcrest:hamcrest-library");
}
