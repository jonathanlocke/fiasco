package digital.fiasco.runtime.build;

import digital.fiasco.runtime.repository.RemoteMavenRepository;
import digital.fiasco.runtime.repository.Repository;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface BuildRepositories
{
    Repository mavenCentral = new RemoteMavenRepository("Maven Central", "https://repo1.maven.org/maven2");
    Repository mavenCentralStaging = new RemoteMavenRepository("Maven Central Staging", "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2");
}
