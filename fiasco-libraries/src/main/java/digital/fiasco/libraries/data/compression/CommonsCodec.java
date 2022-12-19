package digital.fiasco.libraries.data.compression;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface CommonsCodec
{
    Library commons_codec = library("commons-codec:commons-codec");
}
