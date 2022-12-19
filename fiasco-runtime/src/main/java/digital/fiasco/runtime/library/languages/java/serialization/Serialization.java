package digital.fiasco.runtime.library.languages.java.serialization;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings({ "unused" })
public interface Serialization extends
        GoogleGson,
        Jackson,
        Json,
        Kryo,
        GoogleProtobuf,
        ApacheThrift
{
}
