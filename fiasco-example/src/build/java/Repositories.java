import fiasco.Repository;
import fiasco.repository.RemoteMavenRepository;

/**
 * @author jonathan
 */
public interface Repositories
{
    Repository nexus = new RemoteMavenRepository("Nexus", "https://state-of-the-art.org/nexus/content/groups/public/");
}
