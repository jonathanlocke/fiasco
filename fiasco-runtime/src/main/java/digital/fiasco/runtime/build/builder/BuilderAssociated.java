package digital.fiasco.runtime.build.builder;

/**
 * Interface to get the builder for an object that is build-associated
 *
 * @author Jonathan Locke
 */
public interface BuilderAssociated
{
    /**
     * Returns the builder associated with this object
     *
     * @return The builder
     */
    Builder associatedBuilder();
}
