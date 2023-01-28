package digital.fiasco.runtime.build;

import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.maven.MavenRepository;

import static com.telenav.kivakit.resource.Uris.uri;
import static digital.fiasco.runtime.repository.maven.MavenRepository.LOCAL_MAVEN_REPOSITORY_FOLDER;

/**
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface BuildRepositoriesTrait
{
    Repository MAVEN_CENTRAL = new MavenRepository("maven-central",
        uri("https://repo1.maven.org/maven2"),
        LOCAL_MAVEN_REPOSITORY_FOLDER
    );

    Repository MAVEN_CENTRAL_STAGING = new MavenRepository("maven-central-staging",
        uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2"),
        LOCAL_MAVEN_REPOSITORY_FOLDER
    );
}
