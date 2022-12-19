package digital.fiasco.libraries.languages.java.serialization;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface GoogleProtobuf
{
    Library protobuf = library("com.google.protobuf:protobuf-java");
}
