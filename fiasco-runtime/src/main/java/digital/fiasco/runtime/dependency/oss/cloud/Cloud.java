package digital.fiasco.runtime.dependency.oss.cloud;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "unused" })
public interface Cloud extends
        Aws,
        ApacheHadoop,
        ApacheKafka,
        ApacheSpark,
        ApacheZookeeper
{
}
