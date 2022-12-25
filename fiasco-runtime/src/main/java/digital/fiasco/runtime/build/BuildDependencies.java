package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.version.Version;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;

import static com.telenav.kivakit.core.version.Version.version;

/**
 * Defines build dependencies.
 *
 * <p><b>Build Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies()}</li>
 *     <li>{@link #requires(DependencyList)}</li>
 *     <li>{@link #requires(Artifact, Artifact[])}</li>
 * </ul>
 *
 * <p><b>Dependency Version Pinning</b></p>
 *
 * <ul>
 *     <li>{@link #pinVersion(Artifact, String)}</li>
 *     <li>{@link #pinVersion(Artifact, Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("UnusedReturnValue")
public interface BuildDependencies
{
    /**
     * The dependencies for this build
     *
     * @return The list of dependencies
     */
    DependencyList dependencies();

    /**
     * Globally pins all versions of the given artifact to the specified version
     *
     * @param artifact The artifact
     * @param version The version to use
     * @return The build for method chaining
     */
    Build pinVersion(Artifact<?> artifact, Version version);

    /**
     * Globally pins all versions of the given artifact to the specified version
     *
     * @param artifact The artifact
     * @param version The version to use
     * @return The build for method chaining
     */
    default Build pinVersion(Artifact<?> artifact, String version)
    {
        return pinVersion(artifact, version(version));
    }

    /**
     * Adds one or more build dependencies
     *
     * @param first The first dependency
     * @param rest Any further dependencies
     * @return The build for method chaining
     */
    Build requires(Artifact<?> first, Artifact<?>... rest);

    /**
     * Adds a list of build dependencies
     *
     * @param dependencies THe dependencies to add
     * @return The build for method chaining
     */
    Build requires(DependencyList dependencies);
}
