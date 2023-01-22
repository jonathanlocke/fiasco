package digital.fiasco.runtime.repository.remote.server.serialization;

import com.telenav.kivakit.filesystem.Folders;
import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.repository.local.cache.CacheRepository;
import digital.fiasco.runtime.repository.local.LocalRepository;
import org.junit.Test;

import static digital.fiasco.runtime.build.BuildRepositories.MAVEN_CENTRAL;
import static digital.fiasco.runtime.build.BuildRepositories.MAVEN_CENTRAL_STAGING;

public class RepositorySerializationTest extends FiascoTest
{
    @Test
    public void test()
    {
        testSerialization(MAVEN_CENTRAL);
        testSerialization(MAVEN_CENTRAL_STAGING);
        testSerialization(new CacheRepository("cache", Folders.currentFolder()));
        testSerialization(new LocalRepository("local", Folders.currentFolder()));
    }
}
