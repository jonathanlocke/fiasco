package digital.fiasco.runtime.build;

import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.maven.MavenRepository;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface BuildRepositories
{
    Repository mavenCentral = new MavenRepository(
            "Maven Central",
            "https://repo1.maven.org/maven2");

    Repository mavenCentralStaging = new MavenRepository(
            "Maven Central Staging",
            "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2");
}
