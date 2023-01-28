package digital.fiasco.libraries.utilities.serialization;

import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

import static digital.fiasco.runtime.dependency.artifact.artifacts.Library.library;

@SuppressWarnings("unused")
public interface Json
{
    Library json = library("org.json:json");
}
