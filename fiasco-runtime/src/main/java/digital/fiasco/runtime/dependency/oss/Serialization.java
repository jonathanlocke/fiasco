package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface Serialization
{
    Library gson = library("com.google.code.gson:gson");
    Library jackson_annotations = library("com.fasterxml.jackson.core:jackson-annotations");
    Library jackson_core = library("com.fasterxml.jackson.core:jackson-core");
    Library jackson_databind = library("com.fasterxml.jackson.core:jackson-databind");
    Library json = library("org.json:json");
    Library kryo = library("com.esotericsoftware:kryo");
    Library protobuf = library("com.google.protobuf:protobuf-java");
}
