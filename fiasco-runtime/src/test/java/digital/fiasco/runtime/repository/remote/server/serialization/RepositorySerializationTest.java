package digital.fiasco.runtime.repository.remote.server.serialization;

import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.repository.local.user.FiascoUserRepository;
import digital.fiasco.runtime.repository.local.cache.FiascoCacheRepository;
import org.junit.Test;

import static digital.fiasco.runtime.build.environment.BuildRepositoriesTrait.MAVEN_CENTRAL;
import static digital.fiasco.runtime.build.environment.BuildRepositoriesTrait.MAVEN_CENTRAL_STAGING;

public class RepositorySerializationTest extends FiascoTest
{
    @Test
    public void test()
    {
        testSerialization(MAVEN_CENTRAL);
        testSerialization(MAVEN_CENTRAL_STAGING);
        testSerialization(new FiascoCacheRepository("cache"));
        testSerialization(new FiascoUserRepository("local"));
    }
}
