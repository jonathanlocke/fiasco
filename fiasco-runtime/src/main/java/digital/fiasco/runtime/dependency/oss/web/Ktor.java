package digital.fiasco.runtime.dependency.oss.web;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Ktor
{
    Library ktor_client_core = library("io.ktor:ktor-client-core");
}
