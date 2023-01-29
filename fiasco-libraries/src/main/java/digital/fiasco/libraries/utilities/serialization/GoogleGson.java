package digital.fiasco.libraries.utilities.serialization;

import digital.fiasco.runtime.dependency.artifact.types.Library;

import static digital.fiasco.runtime.dependency.artifact.types.Library.library;

@SuppressWarnings("unused")
public interface GoogleGson
{
    Library gson = library("com.google.code.gson:gson");
}
