package digital.fiasco.runtime.library.cloud;

@SuppressWarnings({ "unused" })
public interface Cloud extends
        Aws,
        ApacheCamel,
        ApacheFlink,
        ApacheHadoop,
        ApacheIgnite,
        ApacheKafka,
        ApacheSpark,
        ApacheZookeeper
{
}
