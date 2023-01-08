package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.interfaces.naming.Named;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.Repository;

/**
 * A dependency has a list of {@link #dependencies()}, which must be resolved for it to function.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface Dependency extends Named
{
    /**
     * @return The objects that this depends on
     */
    DependencyList dependencies();

    /**
     * The artifact descriptor for this dependency
     */
    ArtifactDescriptor artifactDescriptor();

    /**
     * Returns the repository where this dependency was found
     *
     * @return The hosting repository
     */
    Repository repository();
}
