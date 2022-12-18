package digital.fiasco.runtime.dependency.oss.java.serialization;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface GoogleProtobuf
{
    Library protobuf = library("com.google.protobuf:protobuf-java");
}
