package digital.fiasco.libraries.utilities.serialization;

import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

import static digital.fiasco.runtime.dependency.artifact.artifacts.Library.library;

@SuppressWarnings("unused")
public interface GoogleGson
{
    Library gson = library("com.google.code.gson:gson");
}
