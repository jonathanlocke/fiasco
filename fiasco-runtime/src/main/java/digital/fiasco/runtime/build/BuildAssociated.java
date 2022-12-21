package digital.fiasco.runtime.build;

/**
 * An object that has an associated {@link Build}
 *
 * @author Jonathan Locke
 */
public interface BuildAssociated
{
    /**
     * Returns the build associated with this object
     */
    Build associatedBuild();
}
