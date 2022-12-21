package digital.fiasco.libraries.languages.java.serialization;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Kryo
{
    Library kryo = library("com.esotericsoftware:kryo");
}