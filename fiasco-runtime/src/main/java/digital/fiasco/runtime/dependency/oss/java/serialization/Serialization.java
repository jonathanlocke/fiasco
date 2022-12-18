package digital.fiasco.runtime.dependency.oss.java.serialization;

import static digital.fiasco.runtime.repository.Library.library;

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
