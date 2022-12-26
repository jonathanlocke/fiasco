package digital.fiasco.runtime.build;

import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.maven.MavenRepository;

import static com.telenav.kivakit.resource.Uris.uri;

/**
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface BuildRepositories
{
    Repository MAVEN_CENTRAL = new MavenRepository(
        "Maven Central",
        uri("https://repo1.maven.org/maven2"));

    Repository MAVEN_CENTRAL_STAGING = new MavenRepository(
        "Maven Central Staging",
        uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2"));
}
