package digital.fiasco.runtime.dependency.oss.data.compression;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface CommonsCodec
{
    Library commons_codec = library("commons-codec:commons-codec");
}
