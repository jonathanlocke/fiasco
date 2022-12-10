import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.RemoteMavenRepository;

/**
 * @author jonathan
 */
public interface Repositories
{
    Repository nexus = new RemoteMavenRepository("Nexus", "https://state-of-the-art.org/nexus/content/groups/public/");
}
