package digital.fiasco.libraries.utilities.serialization;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Kryo
{
    Library kryo = library("com.esotericsoftware:kryo");
}
