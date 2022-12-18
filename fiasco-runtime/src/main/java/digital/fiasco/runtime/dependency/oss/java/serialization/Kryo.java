package digital.fiasco.runtime.dependency.oss.java.serialization;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Kryo
{
    Library kryo = library("com.esotericsoftware:kryo");
}
