package digital.fiasco.runtime.repository.maven;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourcePath;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactResources;
import digital.fiasco.runtime.dependency.artifact.ArtifactSignatures;
import digital.fiasco.runtime.repository.BaseRepository;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.network.http.HttpNetworkLocation.parseHttpNetworkLocation;
import static com.telenav.kivakit.resource.CopyMode.OVERWRITE;
import static com.telenav.kivakit.resource.FileName.parseFileName;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactResources.JAR_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactResources.JAVADOC_JAR_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactResources.SOURCES_JAR_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactType.LIBRARY;

/**
 * A basic Maven repository, either on the local machine or some remote resource folder. If the given root folder is a
 * local folder the repository is writable, otherwise it is read-only.
 *
 * <p><b>Retrieving Artifacts and Content</b></p>
 *
 * <ul>
 *     <li>{@link #resolve(ArtifactDescriptor)} - Gets the {@link Artifact} for the given descriptor</li>
 *     <li>{@link #content(Artifact, ArtifactContent, String)} - Gets the cached resource for the given artifact and content metadata</li>
 * </ul>
 *
 * <p><b>Adding and Removing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #add(Artifact, ArtifactResources)} - Adds the given artifact with the given attached resources</li>
 *     <li>{@link #clear()} - Removes all data from this repository</li>
 * </ul>
 *
 * @author jonathan
 */
@SuppressWarnings({ "JavadocLinkAsPlainText", "unused" })
public class MavenRepository extends BaseRepository
{
    /** The root folder for this repository */
    private final ResourceFolder<?> root;

    /** The name of this repository */
    private final String name;

    /**
     * @param root The root folder of this repository
     */
    public MavenRepository(String name, String root)
    {
        this.name = name;
        this.root = parseHttpNetworkLocation(this, root).resource().parent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(Artifact artifact, ArtifactResources resources)
    {
        var descriptor = artifact.descriptor();
        return writeContent(descriptor, artifact.jar(), resources.get(JAR_SUFFIX), JAR_SUFFIX)
                && writeContent(descriptor, artifact.javadoc(), resources.get(JAVADOC_JAR_SUFFIX), JAVADOC_JAR_SUFFIX)
                && writeContent(descriptor, artifact.source(), resources.get(SOURCES_JAR_SUFFIX), SOURCES_JAR_SUFFIX)
                && writePom(artifact);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
        root.delete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource content(Artifact artifact, ArtifactContent content, String suffix)
    {
        var descriptor = artifact.descriptor();
        return mavenFolder(root, descriptor)
                .resource(mavenFileName(descriptor, suffix));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Artifact resolve(ArtifactDescriptor descriptor)
    {
        // Read content information,
        var jar = readContent(descriptor, JAR_SUFFIX);
        var javadoc = readContent(descriptor, JAVADOC_JAR_SUFFIX);
        var source = readContent(descriptor, SOURCES_JAR_SUFFIX);

        // TODO read metadata with Maven libraries
        DependencyList<Artifact> dependencies = dependencyList();

        // Return the artifact, with descriptor, dependencies, and jar attachments.
        return new Artifact(descriptor, LIBRARY, dependencies, jar, javadoc, source);
    }

    /**
     * Returns this descriptor as a filename with the given suffix.
     *
     * @param suffix The suffix to add to the filename
     * @return The filename
     */
    private FileName mavenFileName(ArtifactDescriptor descriptor, String suffix)
    {
        return parseFileName(throwingListener(), descriptor.group() + "-" + descriptor.version() + suffix);
    }

    /**
     * Returns the sub-folder within the given folder for the given descriptor
     *
     * @param root The root folder
     * @param descriptor The descriptor
     * @return The folder within the root folder for the descriptor
     */
    private ResourceFolder<?> mavenFolder(ResourceFolder<?> root, ArtifactDescriptor descriptor)
    {
        return root.folder(mavenPath(descriptor).join("/"));
    }

    /**
     * Returns this artifact descriptor as a Maven path (relative to some repository root) such as:
     * <pre>
     * com/telenav/kivakit/kivakit-application/1.9.0</pre>
     *
     * @return The resource path
     */
    private ResourcePath mavenPath(ArtifactDescriptor descriptor)
    {
        var groupPath = descriptor.group().name().replaceAll("\\.", "/");
        return parseResourcePath(throwingListener(), groupPath + "/" + descriptor.identifier() + "/" + descriptor.version());
    }

    /**
     * Returns a file under the given target folder for the given descriptor and file suffix
     *
     * @param target The target folder
     * @param descriptor The artifact descriptor
     * @param suffix The file suffix
     * @return The file
     */
    private Resource mavenResource(ResourceFolder<?> target, ArtifactDescriptor descriptor, String suffix)
    {
        var folder = mavenFolder(target, descriptor);
        return folder.resource(mavenFileName(descriptor, suffix));
    }

    /**
     * Reads the signature files that go with an artifact, returning an {@link ArtifactContent}.
     *
     * @param descriptor The artifact descriptor
     * @param suffix The file suffix
     * @return The artifact content
     */
    private ArtifactContent readContent(ArtifactDescriptor descriptor, String suffix)
    {
        var artifact = mavenResource(root, descriptor, suffix);
        var pgp = mavenResource(root, descriptor, suffix + "pgp").reader().readText();
        var md5 = mavenResource(root, descriptor, suffix + "md5").reader().readText();
        var sha1 = mavenResource(root, descriptor, suffix + "sha1").reader().readText();
        var signatures = new ArtifactSignatures(pgp, md5, sha1);
        return new ArtifactContent(signatures, -1, artifact.lastModified(), artifact.sizeInBytes());
    }

    /**
     * Writes the given content in this format:
     * <pre>
     * kivakit-application-1.9.0-javadoc.jar
     * kivakit-application-1.9.0-javadoc.jar.asc
     * kivakit-application-1.9.0-javadoc.jar.md5
     * kivakit-application-1.9.0-javadoc.jar.sha1
     * </pre>
     *
     * @param descriptor The descriptor for the artifact
     * @param artifactContent The artifact content
     * @param artifactContentResource The artifact content resource to save
     * @param suffix The resource suffix to use
     */
    private boolean writeContent(ArtifactDescriptor descriptor,
                                 ArtifactContent artifactContent,
                                 Resource artifactContentResource,
                                 String suffix)
    {
        // If we can write to the folder,
        if (root instanceof Folder targetFolder)
        {
            // get the target sub-folder,
            targetFolder = (Folder) mavenFolder(targetFolder, descriptor);

            // and the target file in that folder,
            var targetFile = (File) mavenResource(targetFolder, descriptor, suffix);

            // and copy the content to that file.
            if (artifactContentResource.safeCopyTo(targetFile, OVERWRITE))
            {
                // Get the signatures for the content,
                var signature = artifactContent.signatures();

                // and write them to the folder.
                return ((File) mavenResource(targetFolder, descriptor, suffix + ".asc")).saveText(signature.pgp())
                        && ((File) mavenResource(targetFolder, descriptor, suffix + ".md5")).saveText(signature.md5())
                        && ((File) mavenResource(targetFolder, descriptor, suffix + ".sha1")).saveText(signature.sha1());
            }
        }
        return false;
    }

    /**
     * Writes a basic pom of this form for the given artifact:
     *
     * <pre>
     * &lt;project
     *   xmlns="http://maven.apache.org/POM/4.0.0"
     *   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     *   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
     *               http://maven.apache.org/xsd/maven-4.0.0.xsd"&gt;
     *  &lt;modelVersion&gt;4.0.0&lt;/modelVersion&gt;
     *  &lt;groupId&gt;com.telenav.kivakit&lt;/groupId&gt;
     *  &lt;artifactId&gt;kivakit-application&lt;/artifactId&gt;
     *  &lt;version&gt;1.9.0&lt;/version&gt;
     *  &lt;dependencies&gt;
     *    &lt;dependency&gt;
     *      &lt;groupId&gt;com.telenav.kivakit&lt;/groupId&gt;
     *      &lt;artifactId&gt;kivakit-component&lt;/artifactId&gt;
     *      &lt;version&gt;1.9.0&lt;/version&gt;
     *    &lt;/dependency&gt;
     *  &lt;/dependencies&gt;
     * &lt;/project&gt;</pre>
     *
     * @param artifact The artifact metadata
     */
    private boolean writePom(Artifact artifact)
    {
        // If we can write to the folder,
        if (root instanceof Folder target)
        {
            // get the file to write to
            var file = (File) mavenResource(target, artifact.descriptor(), ".pom");
            return file.saveText(artifact.asMavenPom());
        }
        else
        {
            problem("Cannot write pom to: $", root);
            return false;
        }
    }
}
