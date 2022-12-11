package digital.fiasco.runtime.repository.maven;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourcePath;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.artifact.ArtifactContentMetadata;
import digital.fiasco.runtime.repository.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.artifact.ArtifactMetadata;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.network.http.HttpNetworkLocation.parseHttpNetworkLocation;
import static com.telenav.kivakit.resource.CopyMode.OVERWRITE;
import static com.telenav.kivakit.resource.FileName.parseFileName;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;

/**
 * A basic Maven repository, either on the local machine or some remote resource folder. If the given root folder is a
 * local folder the repository is writable, otherwise it is read-only.
 *
 * <p><b>Retrieving</b></p>
 *
 * <ul>
 *     <li>{@link #metadata(ArtifactDescriptor)} - Gets the {@link ArtifactMetadata} for the given descriptor</li>
 *     <li>{@link digital.fiasco.runtime.repository.Repository#content(ArtifactMetadata, ArtifactContentMetadata, String)} - Gets the cached resource for the given artifact and content metadata</li>
 * </ul>
 *
 * <p><b>Adding and Removing</b></p>
 *
 * <ul>
 *     <li>{@link #clear()} - Removes all data from this repository</li>
 *     <li>{@link #metadata(ArtifactDescriptor)} - Gets the {@link ArtifactMetadata} for the given descriptor</li>
 *     <li>{@link digital.fiasco.runtime.repository.Repository#content(ArtifactMetadata, ArtifactContentMetadata, String)} - Gets the cached resource for the given content metadata</li>
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
    public boolean add(ArtifactMetadata metadata, Resource jar, Resource javadoc, Resource source)
    {
        var descriptor = metadata.descriptor();
        return writeResource(descriptor, metadata.jar(), jar, ".jar")
                && writeResource(descriptor, metadata.javadoc(), javadoc, "-javadoc.jar")
                && writeResource(descriptor, metadata.source(), source, "-sources.jar")
                && writePom(metadata);
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
    public Resource content(ArtifactMetadata metadata, ArtifactContentMetadata content, String suffix)
    {
        var descriptor = metadata.descriptor();
        return mavenFolder(root, descriptor)
                .resource(mavenFileName(descriptor, suffix));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArtifactMetadata metadata(ArtifactDescriptor descriptor)
    {
        return null;
    }

    public String name()
    {
        return name;
    }

    /**
     * Returns a file under the root of this repository
     *
     * @param descriptor The artifact descriptor
     * @param suffix The file suffix
     * @return The file
     */
    private File mavenFile(Folder target, ArtifactDescriptor descriptor, String suffix)
    {
        var folder = (Folder) mavenFolder(target, descriptor);
        return folder.file(mavenFileName(descriptor, suffix));
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
     * Returns the subfolder within the given folder for the given descriptor
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
     * Writes a basic pom for the given artifact metadata:
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
     * @param metadata The artifact metadata
     */
    private boolean writePom(ArtifactMetadata metadata)
    {
        // If we can write to the folder,
        if (root instanceof Folder target)
        {
            // get the file to write to
            var file = mavenFile(target, metadata.descriptor(), ".pom");
            file.saveText(metadata.asMavenPom());
            // TODO return saveText boolean
            return true;
        }
        else
        {
            problem("Cannot write pom to: $", root);
            return false;
        }
    }

    /**
     * Writes the given metadata and resource in this format:
     * <pre>
     * kivakit-application-1.9.0-javadoc.jar
     * kivakit-application-1.9.0-javadoc.jar.asc
     * kivakit-application-1.9.0-javadoc.jar.md5
     * kivakit-application-1.9.0-javadoc.jar.sha1
     * </pre>
     *
     * @param descriptor The descriptor for the artifact
     * @param contentMetadata The artifact content metadata
     * @param content The artifact content to save
     * @param suffix The resource suffix to use
     */
    private boolean writeResource(ArtifactDescriptor descriptor,
                                  ArtifactContentMetadata contentMetadata,
                                  Resource content,
                                  String suffix)
    {
        // If we can write to the folder,
        if (root instanceof Folder targetFolder)
        {
            // get the target sub-folder,
            targetFolder = (Folder) mavenFolder(targetFolder, descriptor);

            // and the target file in that folder,
            var targetFile = mavenFile(targetFolder, descriptor, ".jar");

            // and copy the content to that file.
            content.safeCopyTo(targetFile, OVERWRITE);

            // Get the signatures for the content,
            var signature = contentMetadata.signatures();

            // and write them to the folder.
            mavenFile(targetFolder, descriptor, suffix + ".asc").saveText(signature.pgp());
            mavenFile(targetFolder, descriptor, suffix + ".md5").saveText(signature.md5());
            mavenFile(targetFolder, descriptor, suffix + ".sha1").saveText(signature.sha1());

            // TODO return saveText stati using KivaKit 1.9.1
            return true;
        }
        return false;
    }
}
