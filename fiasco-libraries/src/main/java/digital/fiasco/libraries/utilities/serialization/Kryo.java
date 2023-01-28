package digital.fiasco.libraries.utilities.serialization;

import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

import static digital.fiasco.runtime.dependency.artifact.artifacts.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Kryo
{
    Library kryo = library("com.esotericsoftware:kryo");
}
