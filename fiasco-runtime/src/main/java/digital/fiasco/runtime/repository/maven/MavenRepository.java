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
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachment;
import digital.fiasco.runtime.dependency.artifact.ArtifactContent;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactSignatures;
import digital.fiasco.runtime.dependency.artifact.Library;
import digital.fiasco.runtime.repository.BaseRepository;
import digital.fiasco.runtime.repository.maven.resolver.MavenDependency;
import digital.fiasco.runtime.repository.maven.resolver.MavenResolver;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.resource.FileName.parseFileName;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;
import static com.telenav.kivakit.resource.WriteMode.OVERWRITE;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.CONTENT_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.JAVADOC_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.SOURCES_SUFFIX;
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
 *     <li>{@link #resolveArtifacts(ObjectList)} - Gets the {@link Artifact}s for the given descriptors</li>
 * </ul>
 *
 * <p><b>Adding and Removing Artifacts</b></p>
 *
 * <ul>
 *     <li>{@link #installArtifact(Artifact)} - Adds the given artifact with the given attached resources to
 *     this repository (if allowed)</li>
 *     <li>{@link #clearArtifacts()} - Removes all data from this repository</li>
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
    public void clearArtifacts()
    {
        lock().write(() ->
        {
            rootFolder.delete();
        });
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
            mavenWriteContent(artifact.attachment(CONTENT_SUFFIX));
            if (artifact instanceof Library library)
            {
                mavenWriteContent(artifact.attachment(JAVADOC_SUFFIX));
                mavenWriteContent(artifact.attachment(SOURCES_SUFFIX));
            }
            mavenWritePom(artifact);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRemote()
    {
        return !(rootFolder instanceof Folder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Artifact<?>> resolveArtifacts(ObjectList<ArtifactDescriptor> descriptors)
    {
        ObjectList<Artifact<?>> resolved = list();

        return lock().read(() ->
        {
            // Go through each descriptor,
            for (var descriptor : descriptors)
            {
                var artifact = artifacts().get(descriptor);

                // read any artifact jar attachment (if the repository does not contain the artifact,
                // the return value assigned to jar will be null),
                var jar = mavenReadContent(new ArtifactAttachment(artifact, CONTENT_SUFFIX));
                if (jar != null)
                {
                    // then read any Javadoc or source code attachments,
                    var javadoc = mavenReadContent(new ArtifactAttachment(artifact, JAVADOC_SUFFIX));
                    var source = mavenReadContent(new ArtifactAttachment(artifact, SOURCES_SUFFIX));

                    // and resolve dependencies for the artifact from remote repositories.
                    var dependencyDescriptors = mavenResolver
                            .resolveDependencies(descriptor)
                            .map(MavenDependency::descriptor);

                    var artifacts = resolveArtifacts(dependencyDescriptors);
                    var dependencies = dependencyList((Dependency<?>) artifacts);

                    // If the artifact has source code,
                    if (source != null)
                    {
                        // return it as a library,
                        resolved.add(library(descriptor)
                                .withDependencies(dependencies)
                                .withContent(jar)
                                .withJavadoc(javadoc)
                                .withSource(source));
                    }
                    else
                    {
                        // otherwise, return it as an asset.
                        resolved.add(asset(descriptor)
                                .withDependencies(dependencies)
                                .withContent(jar));
                    }
                }
            }
            return resolved;
        });
    }

    /**
     * Returns this descriptor as a filename with the given suffix.
     *
     * @param suffix The suffix to add to the filename
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
                + "/" + descriptor.identifier()
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
        var artifact = attachment.artifact();
        var resource = mavenResource(rootFolder, attachment);
        if (resource.exists())
        {
            var suffix = attachment.suffix();
            var pgp = mavenResource(rootFolder, attachment.withSuffix(suffix + "pgp")).reader().readText();
            var md5 = mavenResource(rootFolder, attachment.withSuffix(suffix + "md5")).reader().readText();
            var sha1 = mavenResource(rootFolder, attachment.withSuffix(suffix + "sha1")).reader().readText();
            var signatures = new ArtifactSignatures(pgp, md5, sha1);
            return new ArtifactContent()
                    .withName(attachment.content().name())
                    .withSignatures(signatures)
                    .withResource(resource)
                    .withLastModified(resource.lastModified())
                    .withSize(resource.sizeInBytes());
        }
        return null;
    }

    /**
     * Returns a file under the given target folder for the given descriptor and file suffix
     *
     * @param target The target folder
     * @param attachment The artifact attachment
     * @return The file
     */
    private Resource mavenResource(ResourceFolder<?> target,
                                   ArtifactAttachment attachment)
    {
        var artifact = attachment.artifact();
        var folder = mavenFolder(target, artifact);
        return folder.resource(mavenFileName(artifact, attachment.suffix()));
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
        var artifact = attachment.artifact();

        // If we can write to the folder,
        if (rootFolder instanceof Folder targetFolder)
        {
            // get the target sub-folder,
            targetFolder = (Folder) mavenFolder(targetFolder, artifact);

            // and the target file in that folder,
            var targetFile = (File) mavenResource(targetFolder, attachment);

            // and copy the content to that file.
            attachment.content().resource().safeCopyTo(targetFile, OVERWRITE);

            // Get the signatures for the content,
            var signature = attachment.content().signatures();

            // and write them to the folder.
            var suffix = attachment.suffix();
            ((File) mavenResource(targetFolder, attachment.withSuffix(suffix + ".asc"))).saveText(signature.pgp());
            ((File) mavenResource(targetFolder, attachment.withSuffix(suffix + ".md5"))).saveText(signature.md5());
            ((File) mavenResource(targetFolder, attachment.withSuffix(suffix + ".sha1"))).saveText(signature.sha1());
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
            var file = (File) mavenResource(target, new ArtifactAttachment(artifact, ".pom"));
            file.saveText(artifact.mavenPom());
        }
        else
        {
            fail("Cannot write pom to: $", rootFolder);
        }
    }
}
