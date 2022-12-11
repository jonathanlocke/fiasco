package digital.fiasco.runtime.repository.maven;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.artifact.ArtifactContentMetadata;
import digital.fiasco.runtime.repository.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.artifact.ArtifactMetadata;

import static com.telenav.kivakit.resource.CopyMode.OVERWRITE;

/**
 * A basic Maven repository, either on the local machine or some remote resource folder. If the given root folder is a
 * local folder the repository is writable, otherwise it is read-only.
 *
 * @author jonathan
 */
@SuppressWarnings({ "JavadocLinkAsPlainText", "unused" })
public class MavenRepository extends BaseRepository
{
    /** The root folder for this repository */
    private final ResourceFolder<?> root;

    /**
     * @param root The root folder of this repository
     */
    public MavenRepository(ResourceFolder<?> root)
    {
        this.root = listenTo(root);
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
    public Resource content(ArtifactContentMetadata artifact)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArtifactMetadata metadata(ArtifactDescriptor descriptor)
    {
        return null;
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
        var folder = target.folder(descriptor.asMavenPath().join("/"));
        var fileName = descriptor.asFileName(suffix);
        return folder.file(fileName);
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
            targetFolder = targetFolder.folder(descriptor.asMavenPath().join("/"));

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
