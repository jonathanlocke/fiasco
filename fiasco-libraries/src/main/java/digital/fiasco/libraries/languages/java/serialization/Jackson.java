package digital.fiasco.libraries.languages.java.serialization;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface Jackson
{
    Library jackson_annotations = library("com.fasterxml.jackson.core:jackson-annotations");
    Library jackson_core = library("com.fasterxml.jackson.core:jackson-core");
    Library jackson_databind = library("com.fasterxml.jackson.core:jackson-databind");
}
