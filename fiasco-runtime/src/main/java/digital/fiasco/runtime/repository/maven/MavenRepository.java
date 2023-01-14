package digital.fiasco.runtime.repository.maven;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.network.http.HttpResourceFolder;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourcePath;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactContentSignatures;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.dependency.artifact.Library;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.maven.resolver.MavenDependency;
import digital.fiasco.runtime.repository.maven.resolver.MavenResolver;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.resource.Extension.MD5;
import static com.telenav.kivakit.resource.Extension.POM;
import static com.telenav.kivakit.resource.Extension.SHA1;
import static com.telenav.kivakit.resource.FileName.parseFileName;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;
import static com.telenav.kivakit.resource.WriteMode.OVERWRITE;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.attachment;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAR_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.POM_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.SOURCES_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactContent.content;
import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifactList;
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
 *     <li>{@link digital.fiasco.runtime.repository.Repository#resolveArtifacts(ObjectList)} - Resolves the given descriptors to a list of {@link Artifact}s, complete with {@link ArtifactContent} attachments</li>
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
@SuppressWarnings({ "JavadocLinkAsPlainText", "unused" })
public class MavenRepository extends BaseRepository
{
    /** Resolves artifacts from maven repositories */
    private static MavenResolver mavenResolver = new MavenResolver();

    /** The root folder for this repository */
    private final ResourceFolder<?> rootFolder;

    /**
     * Creates a maven repository
     */
    public MavenRepository(String name, URI uri)
    {
        super(name, uri);

        this.rootFolder = new HttpResourceFolder(uri);
        mavenResolver = mavenResolver.withRepository(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installArtifact(Artifact<?> artifact)
    {
        lock().write(() ->
        {
            var descriptor = artifact.descriptor();
            mavenWriteContent(artifact.attachmentOfType(JAR_ATTACHMENT));
            if (artifact instanceof Library library)
            {
                mavenWriteContent(artifact.attachmentOfType(JAVADOC_ATTACHMENT));
                mavenWriteContent(artifact.attachmentOfType(SOURCES_ATTACHMENT));
            }
            mavenWritePom(artifact);
        });
    }

    @Override
    public boolean isRemote()
    {
        return !uri().getScheme().equals("file");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArtifactList resolveArtifacts(ObjectList<ArtifactDescriptor> descriptors)
    {
        return lock().read(() ->
        {
            var resolved = artifactList();

            // Go through each descriptor,
            for (var descriptor : descriptors)
            {
                var artifact = artifacts().get(descriptor);

                // read any artifact jar attachment (if the repository does not contain the artifact,
                // the return value assigned to jar will be null),
                var jar = mavenReadContent(attachment(JAR_ATTACHMENT));
                if (jar != null)
                {
                    // then read any Javadoc or source code attachments,
                    var javadoc = mavenReadContent(attachment(JAVADOC_ATTACHMENT));
                    var source = mavenReadContent(attachment(SOURCES_ATTACHMENT));

                    // and resolve dependencies for the artifact from remote repositories.
                    var dependencyDescriptors = mavenResolver
                        .resolveDependencies(descriptor)
                        .map(MavenDependency::descriptor);

                    var resolvedArtifacts = resolveArtifacts(dependencyDescriptors);

                    // If the artifact has source code,
                    if (source != null)
                    {
                        // return it as a library,
                        resolved.add(library(descriptor)
                            .withDependencies(resolvedArtifacts)
                            .withContent(jar)
                            .withJavadoc(javadoc)
                            .withSources(source));
                    }
                    else
                    {
                        // otherwise, return it as an asset.
                        resolved.add(asset(descriptor)
                            .withDependencies(resolvedArtifacts)
                            .withContent(jar));
                    }
                }
            }
            return resolved;
        });
    }

    /**
     * Returns this descriptor as a filename with the given type.
     *
     * @param suffix The type to add to the filename
     * @return The filename
     */
    private FileName mavenFileName(Artifact<?> artifact, String suffix)
    {
        var descriptor = artifact.descriptor();
        return parseFileName(throwingListener(), descriptor.group()
            + "-" + descriptor.version() + suffix);
    }

    /**
     * Returns the sub-folder within the given folder for the given artifact
     *
     * @param root The root folder
     * @param artifact The artifact
     * @return The folder within the root folder for the descriptor
     */
    private ResourceFolder<?> mavenFolder(ResourceFolder<?> root, Artifact<?> artifact)
    {
        return root.folder(mavenPath(artifact).join("/"));
    }

    /**
     * Returns this artifact descriptor as a Maven path (relative to some repository root) such as:
     * <pre>
     * com/telenav/kivakit/kivakit-application/1.9.0</pre>
     *
     * @return The resource path
     */
    private ResourcePath mavenPath(Artifact<?> artifact)
    {
        var descriptor = artifact.descriptor();
        var groupPath = descriptor.group().name().replaceAll("\\.", "/");
        return parseResourcePath(throwingListener(), groupPath
            + "/" + descriptor.artifact()
            + "/" + descriptor.version());
    }

    /**
     * Reads the signature files that go with an artifact, returning an {@link ArtifactContent}.
     *
     * @param attachment The artifact attachment
     * @return The artifact content
     */
    private ArtifactContent mavenReadContent(ArtifactAttachment attachment)
    {
        var resource = mavenResource(rootFolder, attachment, null);
        if (resource.exists())
        {
            var type = attachment.attachmentType();
            var pgp = mavenResource(rootFolder, attachment.withType(type), Extension.PGP).reader().readText();
            var md5 = mavenResource(rootFolder, attachment.withType(type), MD5).reader().readText();
            var sha1 = mavenResource(rootFolder, attachment.withType(type), SHA1).reader().readText();
            var signatures = new ArtifactContentSignatures(pgp, md5, sha1);

            return content()
                .withName(attachment.content().name())
                .withSignatures(signatures)
                .withResource(resource)
                .withLastModified(resource.lastModified())
                .withSize(resource.sizeInBytes());
        }
        return null;
    }

    /**
     * Returns a file under the given target folder for the given descriptor and file type
     *
     * @param target The target folder
     * @param attachment The artifact attachment
     * @param signatureExtension The extension of the type of signature, like ".sha1"
     * @return The file
     */
    private Resource mavenResource(ResourceFolder<?> target,
                                   ArtifactAttachment attachment,
                                   Extension signatureExtension)
    {
        var artifact = attachment.artifact();
        var folder = mavenFolder(target, artifact);
        return folder.resource(mavenFileName(artifact, attachment.attachmentType().fileSuffix() +
            (signatureExtension == null
                ? ""
                : signatureExtension)));
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
     * @param attachment The artifact attachment
     */
    private void mavenWriteContent(@NotNull ArtifactAttachment attachment)
    {
        // If we can write to the folder,
        if (rootFolder instanceof Folder targetFolder)
        {
            // get the target sub-folder,
            targetFolder = (Folder) mavenFolder(targetFolder, attachment.artifact());

            // and the target file in that folder,
            var targetFile = (File) mavenResource(targetFolder, attachment, null);

            // and copy the content to that file.
            attachment.content().resource().safeCopyTo(targetFile, OVERWRITE);

            // Get the signatures for the content,
            var signature = attachment.content().signatures();

            // and write them to the folder.
            var type = attachment.attachmentType();
            ((File) mavenResource(targetFolder, attachment.withType(type), Extension.ASC)).saveText(signature.pgp());
            ((File) mavenResource(targetFolder, attachment.withType(type), MD5)).saveText(signature.md5());
            ((File) mavenResource(targetFolder, attachment.withType(type), SHA1)).saveText(signature.sha1());
        }
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
    private void mavenWritePom(Artifact<?> artifact)
    {
        // If we can write to the folder,
        if (rootFolder instanceof Folder target)
        {
            // get the file to write to
            var file = (File) mavenResource(target, attachment(POM_ATTACHMENT), POM);
            file.saveText(artifact.mavenPom());
        }
        else
        {
            fail("Cannot write pom to: $", rootFolder);
        }
    }
}
