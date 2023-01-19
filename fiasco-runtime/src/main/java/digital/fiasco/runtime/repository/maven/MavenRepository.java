package digital.fiasco.runtime.repository.maven;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.string.FormatProperty;
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
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactContentSignatures;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.maven.resolver.MavenDependency;
import digital.fiasco.runtime.repository.maven.resolver.MavenResolver;
import org.eclipse.aether.repository.LocalRepository;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.resource.Extension.ASC;
import static com.telenav.kivakit.resource.Extension.MD5;
import static com.telenav.kivakit.resource.Extension.SHA1;
import static com.telenav.kivakit.resource.FileName.parseFileName;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;
import static com.telenav.kivakit.resource.WriteMode.OVERWRITE;
import static digital.fiasco.runtime.FiascoRuntime.fiascoCacheFolder;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.attachment;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAR_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.POM_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.SOURCES_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactContent.content;
import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifacts;
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
    public static Folder LOCAL_MAVEN_REPOSITORY_FOLDER = fiascoCacheFolder().folder("maven-repository");

    /** Resolves artifacts from maven repositories */
    private final MavenResolver mavenResolver;

    /** The root folder for this repository */
    @FormatProperty
    private final ResourceFolder<?> rootFolder;

    /** The local Maven repository */
    private final LocalRepository localRepository;

    /** The local Maven repository folder */
    private final Folder localRepositoryFolder;

    /**
     * Creates a maven repository
     */
    public MavenRepository(String name, URI uri, Folder localRepositoryFolder)
    {
        super(name, uri);

        this.rootFolder = new HttpResourceFolder(uri);
        this.localRepositoryFolder = localRepositoryFolder.mkdirs();
        this.localRepository = new LocalRepository(localRepositoryFolder.asJavaFile());

        mavenResolver = new MavenResolver(localRepositoryFolder)
            .withMavenRepository(this)
            .withLocalRepository(localRepository);
    }

    /**
     * Creates a maven repository
     */
    public MavenRepository(String name, Folder localRepositoryFolder)
    {
        super(name, localRepositoryFolder.asUri());

        this.rootFolder = localRepositoryFolder.mkdirs();
        this.localRepositoryFolder = localRepositoryFolder.mkdirs();
        this.localRepository = new LocalRepository(localRepositoryFolder.asJavaFile());

        mavenResolver = new MavenResolver(localRepositoryFolder)
            .withMavenRepository(this)
            .withLocalRepository(localRepository);
    }

    @Override
    public MavenRepository clear()
    {
        if (!isRemote())
        {
            if (rootFolder instanceof Folder root)
            {
                root.clearAllAndDelete();
            }
            return this;
        }
        else
        {
            return unsupported();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installArtifact(Artifact<?> artifact)
    {
        lock().write(() ->
        {
            if (!isRemote())
            {
                var descriptor = artifact.descriptor();
                artifact.attachments().forEach(this::mavenWriteContent);
                mavenWritePom(artifact);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public boolean isRemote()
    {
        return !uri().getScheme().equals("file");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactList resolveArtifacts(ObjectList<ArtifactDescriptor> descriptors)
    {
        return resolveArtifacts(descriptors, artifacts());
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
        return parseFileName(throwingListener(), descriptor.artifact()
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
        var artifactFolder = root.folder(mavenPath(artifact).join("/"));
        if (artifactFolder instanceof Folder folder)
        {
            artifactFolder.mkdirs();
        }
        return artifactFolder;
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
     * @throws RuntimeException Thrown if the attachment resource doesn't exist
     */
    private ArtifactContent mavenReadContent(ArtifactAttachment attachment)
    {
        var resource = mavenResource(rootFolder, attachment, null);
        if (resource.exists())
        {
            var type = attachment.attachmentType();
            var asc = mavenResource(rootFolder, attachment.withType(type), ASC).reader().readText();
            var md5 = mavenResource(rootFolder, attachment.withType(type), MD5).reader().readText();
            var sha1 = mavenResource(rootFolder, attachment.withType(type), SHA1).reader().readText();
            var signatures = new ArtifactContentSignatures(asc, md5, sha1);

            return content()
                .withName(resource.fileName().name())
                .withSignatures(signatures)
                .withResource(resource)
                .withLastModified(resource.lastModified())
                .withSize(resource.sizeInBytes());
        }
        return illegalState("Content does not exist: $", resource);
    }

    /**
     * Returns a file under the given target folder for the given descriptor and file type
     *
     * @param rootFolder The root repository folder
     * @param attachment The artifact attachment
     * @param signatureExtension The extension of the type of signature, like ".sha1"
     * @return The file
     */
    private Resource mavenResource(ResourceFolder<?> rootFolder,
                                   ArtifactAttachment attachment,
                                   Extension signatureExtension)
    {
        var artifact = attachment.artifact();
        var target = mavenFolder(rootFolder, artifact);
        return target.resource(mavenFileName(artifact, attachment.attachmentType().fileSuffix() +
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
        if (rootFolder instanceof Folder folder)
        {
            // and the target file in that folder,
            var targetFile = (File) mavenResource(folder, attachment, null);

            // and copy the content to that file.
            attachment.content().resource().safeCopyTo(targetFile, OVERWRITE);

            // Get the signatures for the content,
            var signature = attachment.content().signatures();

            // and write them to the folder.
            var type = attachment.attachmentType();
            ((File) mavenResource(folder, attachment.withType(type), ASC)).saveText(signature.asc());
            ((File) mavenResource(folder, attachment.withType(type), MD5)).saveText(signature.md5());
            ((File) mavenResource(folder, attachment.withType(type), SHA1)).saveText(signature.sha1());
        }
        else
        {
            illegalState("Cannot write content to non-folder: $", rootFolder);
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
            var attachment = attachment(POM_ATTACHMENT)
                .withArtifact(artifact);
            var file = (File) mavenResource(rootFolder, attachment, null);
            file.saveText(artifact.mavenPom());
        }
        else
        {
            fail("Cannot write pom to non-folder: $", rootFolder);
        }
    }

    private ArtifactContent readAttachment(Artifact<?> artifact, ArtifactAttachmentType type)
    {
        var attachment = attachment(type).withArtifact(artifact);
        return mavenReadContent(attachment);
    }

    private ArtifactList resolveArtifacts(ObjectList<ArtifactDescriptor> descriptors,
                                          ArtifactList resolvedFinal)
    {
        return lock().read(() ->
        {
            var resolved = resolvedFinal;

            // Go through each descriptor,
            for (var descriptor : descriptors)
            {
                // create a new library artifact,
                var artifact = library(descriptor);

                // and if we have not already resolved it,
                if (!resolved.contains(artifact))
                {
                    // read any JAR attachment,
                    var jar = readAttachment(artifact, JAR_ATTACHMENT);
                    if (jar != null)
                    {
                        // and source and javadoc attachments (if they exist),
                        var sources = readAttachment(artifact, SOURCES_ATTACHMENT);
                        var javadoc = readAttachment(artifact, JAVADOC_ATTACHMENT);

                        // then resolve dependencies for the artifact from remote repositories.
                        var dependencyDescriptors = mavenResolver
                            .resolveDependencies(descriptor)
                            .map(MavenDependency::descriptor)
                            .without(descriptors);

                        // If there are dependencies,
                        var children = artifacts();
                        if (dependencyDescriptors.isNonEmpty())
                        {
                            children = resolveArtifacts(dependencyDescriptors, resolved)
                                .without(resolved);
                            resolved = resolved.with(children);
                        }

                        // If the artifact has source code,
                        if (sources != null)
                        {
                            // return it as a library,
                            resolved = resolved.with(artifact
                                .withDependencies(children)
                                .withContent(jar)
                                .withJavadoc(javadoc)
                                .withSources(sources));
                        }
                        else
                        {
                            // otherwise, return it as an asset.
                            resolved = resolved.with(asset(descriptor)
                                .withDependencies(children)
                                .withContent(jar));
                        }
                    }
                    else
                    {
                        illegalState("Cannot resolve JAR attachment for: $", artifact);
                    }
                }
            }
            return resolved;
        });
    }
}
