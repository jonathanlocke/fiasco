package digital.fiasco.libraries.utilities.serialization;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface Json
{
    Library json = library("org.json:json");
}
