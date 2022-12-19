package digital.fiasco.libraries.languages.java.serialization;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Kryo
{
    Library kryo = library("com.esotericsoftware:kryo");
}
