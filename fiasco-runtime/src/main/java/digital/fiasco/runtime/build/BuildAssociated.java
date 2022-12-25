package digital.fiasco.runtime.build;

/**
 * Interface to get the build for an object that is build-associated
 *
 * @author Jonathan Locke
 */
public interface BuildAssociated
{
    /**
     * Returns the build associated with this object
     *
     * @return The build
     */
    Build associatedBuild();
}
