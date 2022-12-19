package digital.fiasco.libraries.languages.java.serialization;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface ApacheThrift
{
    Library apache_thrift = library("org.apache.thrift:libthrift");
}
