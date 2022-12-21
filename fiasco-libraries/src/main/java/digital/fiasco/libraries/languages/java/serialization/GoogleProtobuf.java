package digital.fiasco.libraries.languages.java.serialization;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface GoogleProtobuf
{
    Library protobuf = library("com.google.protobuf:protobuf-java");
}
