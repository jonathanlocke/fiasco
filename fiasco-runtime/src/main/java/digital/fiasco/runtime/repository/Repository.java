package digital.fiasco.runtime.repository;

import com.telenav.kivakit.core.function.Functions;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.registry.Register;
import com.telenav.kivakit.interfaces.naming.Named;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.collections.ArtifactList;
import digital.fiasco.runtime.repository.local.LocalRepository;
import digital.fiasco.runtime.repository.local.cache.CacheRepository;
import digital.fiasco.runtime.repository.maven.MavenRepository;
import digital.fiasco.runtime.repository.remote.RemoteRepository;
import digital.fiasco.runtime.repository.remote.server.FiascoClient;
import digital.fiasco.runtime.repository.remote.server.FiascoServer;

import java.net.URI;
import java.util.List;

import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;

/**
 * Interface to a repository that stores and resolves artifacts and their content attachments.
 * <p>
 * Interface for repositories of artifacts and their metadata. Implementations include:
 *
 * <ul>
 *     <li>{@link CacheRepository} - High performance artifact store</li>
 *     <li>{@link LocalRepository} - Stores artifacts on the local filesystem</li>
 *     <li>{@link MavenRepository} - A remote or local maven repository</li>
 *     <li>{@link RemoteRepository} - A Fiasco repository at a remote URI</li>
 * </ul>
 *
 * <p><b>Local Repositories</b></p>
 *
 * <p>
 * {@link LocalRepository} is used to store artifacts and metadata on the local filesystem. Artifact
 * metadata is stored in JSON format in an append-only text file called <i>artifacts.txt</i>, which allows
 * it to be searched with grep or viewed in a text editor. The artifact content attachments are stored
 * on the filesystem in a hierarchical format similar to a Maven repository.
 * </p>
 *
 * <p><b>Maven Repositories</b></p>
 *
 * <p>
 * {@link MavenRepository} is used to access local or remote Apache Maven repositories. Support for
 * Maven POM files is limited since only basic metadata and dependencies are required by Fiasco.
 * </p>
 *
 * <p><b>Cache Repositories</b></p>
 *
 * <p>
 * {@link CacheRepository} is used to store artifacts and their metadata in a single file to allow
 * high-performance, random access. As with a {@link LocalRepository}, metadata is stored in a single append-only
 * text file, but artifact content attachments are stored end-to-end in a single file, <i>attachments.binary</i>.
 * </p>
 *
 * <p>
 * An instance of {@link CacheRepository} is used as an artifact cache to avoid unnecessary downloads when a user wipes
 * out their {@link LocalRepository}, causing it to repopulate. Instead of repopulating from Maven Central or another
 * remote repository, the artifacts in this repository can be used since artifacts and their metadata are never altered,
 * only appended to their respective <i>artifacts.txt</i> and <i>artifact-content.binary</i>files. Because remote
 * artifacts are guaranteed by Maven Central (and other remote repositories) to be immutable, it should rarely be
 * necessary to remove a download cache repository.
 * </p>
 *
 * <p>
 * Another instance of {@link CacheRepository} is used by {@link FiascoServer} to respond quickly to requests to
 * resolve one or more artifact descriptors.
 * </p>
 *
 *
 * <p><b>Remote Repositories</b></p>
 *
 * <p>
 * {@link RemoteRepository} is used to access a remote {@link FiascoServer} (launched by the <i>fiasco-server</i> project).
 * Internally, this repository uses {@link FiascoClient} to communicate with the server.
 * </p>
 *
 * <p><b>Retrieving Artifacts and Content</b></p>
 *
 * <ul>
 *     <li>{@link #resolveArtifacts(List)} - Resolves the given descriptors to a collection of {@link Artifact}s, complete with {@link ArtifactContent} attachments</li>
 * </ul>
 *
 * <p><b>Installing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #installArtifact(Artifact)} - Installs the given artifact in this repository, along with its attached resources</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@Register
public interface Repository extends
    Repeater,
    Named
{
    enum InstallationResult
    {
        /** The artifact was successfully installed */
        INSTALLED,

        /** The artifact was already installed */
        ALREADY_INSTALLED,

        /** The artifact could not be installed */
        INSTALLATION_FAILED
    }

    /**
     * Removes all artifacts from this repository
     */
    Repository clear();

    /**
     * Installs the given artifact in this repository
     *
     * @param artifact The artifact to install
     * @throws IllegalStateException Thrown if the artifact cannot be installed in this repository
     */
    InstallationResult installArtifact(Artifact<?> artifact);

    /**
     * Returns true if this repository is remote
     *
     * @return True if this is remote
     */
    default boolean isRemote()
    {
        return false;
    }

    /**
     * Resolves each artifact descriptor to an {@link Artifact} but does not resolve the content
     *
     * @param descriptors The artifact descriptors
     * @return The resolved artifacts
     * @throws IllegalArgumentException Thrown if any descriptor cannot be resolved
     */
    default ArtifactList resolveArtifacts(List<ArtifactDescriptor> descriptors)
    {
        return resolveArtifacts(descriptors, nullProgressReporter(), Functions::doNothing);
    }

    /**
     * Resolves each artifact descriptor to an {@link Artifact} complete with content attachments
     *
     * @param descriptors The artifact descriptors
     * @param reporter The progress reporter to call as content input is read
     * @param reader Callback for reading trailing data after the initial JSON element
     * @return The resolved artifacts
     * @throws IllegalArgumentException Thrown if any descriptor cannot be resolved
     */
    ArtifactList resolveArtifacts(List<ArtifactDescriptor> descriptors,
                                  ProgressReporter reporter,
                                  RepositoryContentReader reader);

    /**
     * Returns the URI of this repository
     *
     * @return The repository URI
     */
    URI uri();
}
