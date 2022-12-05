import fiasco.Repository;
import fiasco.repository.RemoteMavenRepository;

/**
 * @author jonathanl (shibo)
 */
public interface Repositories
{
    Repository nexus = new RemoteMavenRepository("Nexus", "https://state-of-the-art.org/nexus/content/groups/public/");
}
