package digital.fiasco.runtime.library.cloud;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings({ "unused" })
public interface Cloud extends
        Aws,
        ApacheHadoop,
        ApacheKafka,
        ApacheSpark,
        ApacheZookeeper
{
}
