package digital.fiasco.libraries.utilities.serialization;

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
