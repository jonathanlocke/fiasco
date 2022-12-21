package digital.fiasco.runtime.repository.maven;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.network.http.HttpResourceFolder;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourcePath;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachments;
import digital.fiasco.runtime.dependency.artifact.ArtifactContentMetadata;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactSignatures;
import digital.fiasco.runtime.dependency.artifact.Library;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.maven.resolver.MavenDependency;
import digital.fiasco.runtime.repository.maven.resolver.MavenResolver;

import java.net.URI;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.resource.CopyMode.OVERWRITE;
import static com.telenav.kivakit.resource.FileName.parseFileName;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachments.JAR_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachments.JAVADOC_JAR_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachments.SOURCES_JAR_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.Asset.asset;
import static digital.fiasco.runtime.dependency.artifact.Library.library;

/**
 * A basic Maven repository, either on the local machine or some remote resource folder. For remote URIs, repositories
 * are generally not writable.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #name()} - The human readable name of this repository</li>
 *     <li>{@link #uri()} - The location of this repository</li>
 * </ul>
 *
 * <p><b>Retrieving Artifacts and Content</b></p>
 *
 * <ul>
 *     <li>{@link #resolve(ObjectList)} - Gets the {@link Artifact}s for the given descriptors</li>
 *     <li>{@link #content(Artifact, ArtifactContentMetadata, String)} - Gets the cached resource for the given artifact and content metadata</li>
 * </ul>
 *
 * <p><b>Adding and Removing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #install(Artifact, ArtifactAttachments)} - Adds the given artifact with the given attached resources to
 *     this repository (if allowed)</li>
 *     <li>{@link #clear()} - Removes all data from this repository</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "JavadocLinkAsPlainText", "unused" })
public class MavenRepository extends BaseRepository
{
    /** Resolves artifacts from maven repositories */
    private static MavenResolver mavenResolver = new MavenResolver();

    /** The root folder for this repository */
    private final ResourceFolder<?> root;

    /**
     * Creates a maven repository
     */
    public MavenRepository(String name, URI uri)
    {
        super(name, uri);
        this.root = new HttpResourceFolder(uri);

        mavenResolver = mavenResolver.withRepository(this);
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
    public Resource content(Artifact<?> artifact, ArtifactContentMetadata content, String suffix)
    {
        var descriptor = artifact.descriptor();
        return mavenFolder(root, descriptor)
                .resource(mavenFileName(descriptor, suffix));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean install(Artifact<?> artifact, ArtifactAttachments resources)
    {
        var descriptor = artifact.descriptor();
        if (mavenWriteContent(descriptor, artifact.jar(), resources.attachment(JAR_SUFFIX), JAR_SUFFIX))
        {
            if (artifact instanceof Library library)
            {
                return mavenWriteContent(descriptor, library.javadoc(), resources.attachment(JAVADOC_JAR_SUFFIX), JAVADOC_JAR_SUFFIX)
                        && mavenWriteContent(descriptor, library.source(), resources.attachment(SOURCES_JAR_SUFFIX), SOURCES_JAR_SUFFIX)
                        && mavenWritePom(artifact);
            }
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRemote()
    {
        return !(root instanceof Folder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Artifact<?>> resolve(ObjectList<ArtifactDescriptor> descriptors)
    {
        ObjectList<Artifact<?>> resolved = list();

        // Go through each descriptor,
        for (var descriptor : descriptors)
        {
            // read any artifact jar attachment (if the repository does not contain the artifact,
            // the return value assigned to jar will be null),
            var jar = mavenReadContent(descriptor, JAR_SUFFIX);
            if (jar != null)
            {
                // then read any Javadoc or source code attachments,
                var javadoc = mavenReadContent(descriptor, JAVADOC_JAR_SUFFIX);
                var source = mavenReadContent(descriptor, SOURCES_JAR_SUFFIX);

                // and resolve dependencies for the artifact from remote repositories.
                var dependencyDescriptors = mavenResolver
                        .resolveDependencies(descriptor)
                        .map(MavenDependency::descriptor);

                var artifacts = resolve(dependencyDescriptors);
                var dependencies = dependencyList((Dependency<?>) artifacts);

                // If the artifact has source code,
                if (source != null)
                {
                    // return it as a library,
                    resolved.add(library(descriptor)
                            .withDependencies(dependencies)
                            .withJar(jar)
                            .withJavadoc(javadoc)
                            .withSource(source));
                }
                else
                {
                    // otherwise, return it as an asset.
                    resolved.add(asset(descriptor)
                            .withDependencies(dependencies)
                            .withJar(jar));
                }
            }
        }
        return resolved;
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
     * Reads the signature files that go with an artifact, returning an {@link ArtifactContentMetadata}.
     *
     * @param descriptor The artifact descriptor
     * @param suffix The file suffix
     * @return The artifact content
     */
    private ArtifactContentMetadata mavenReadContent(ArtifactDescriptor descriptor, String suffix)
    {
        var artifact = mavenResource(root, descriptor, suffix);
        if (artifact.exists())
        {
            var pgp = mavenResource(root, descriptor, suffix + "pgp").reader().readText();
            var md5 = mavenResource(root, descriptor, suffix + "md5").reader().readText();
            var sha1 = mavenResource(root, descriptor, suffix + "sha1").reader().readText();
            var signatures = new ArtifactSignatures(pgp, md5, sha1);
            return new ArtifactContentMetadata(signatures, -1, artifact.lastModified(), artifact.sizeInBytes());
        }
        return null;
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
    private boolean mavenWriteContent(ArtifactDescriptor descriptor,
                                      ArtifactContentMetadata artifactContent,
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
    private boolean mavenWritePom(Artifact<?> artifact)
    {
        // If we can write to the folder,
        if (root instanceof Folder target)
        {
            // get the file to write to
            var file = (File) mavenResource(target, artifact.descriptor(), ".pom");
            return file.saveText(artifact.mavenPom());
        }
        else
        {
            problem("Cannot write pom to: $", root);
            return false;
        }
    }
}
