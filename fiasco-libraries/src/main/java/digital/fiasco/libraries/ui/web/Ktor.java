package digital.fiasco.libraries.ui.web;

import digital.fiasco.runtime.dependency.artifact.types.Library;

import static digital.fiasco.runtime.dependency.artifact.types.Library.library;

@SuppressWarnings("unused")
public interface Ktor
{
    Library ktor_client_core = library("io.ktor:ktor-client-core");
}
