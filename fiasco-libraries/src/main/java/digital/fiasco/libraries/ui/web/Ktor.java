package digital.fiasco.libraries.ui.web;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface Ktor
{
    Library ktor_client_core = library("io.ktor:ktor-client-core");
}
