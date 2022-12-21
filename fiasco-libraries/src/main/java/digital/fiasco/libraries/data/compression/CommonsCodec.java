package digital.fiasco.libraries.data.compression;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface CommonsCodec
{
    Library commons_codec = library("commons-codec:commons-codec");
}
