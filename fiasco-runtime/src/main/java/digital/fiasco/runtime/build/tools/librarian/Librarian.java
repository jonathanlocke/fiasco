//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.tools.librarian;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.version.Version;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.tools.BaseTool;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.Library;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.fiasco.CacheFiascoRepository;
import digital.fiasco.runtime.repository.fiasco.LocalFiascoRepository;
import digital.fiasco.runtime.repository.maven.MavenRepository;

import java.util.Collection;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.version.Version.version;
import static com.telenav.kivakit.resource.Uris.uri;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.Library.library;

/**
 * Manages {@link Library} artifacts and their dependencies. Searches a list of repositories added with
 * {@link #lookIn(Repository)} to resolve libraries and their dependencies.
 *
 * <p><b>Finding Libraries</b></p>
 *
 * <ul>
 *     <li>{@link #resolve(ArtifactDescriptor)} - Resolves the library specified by the given descriptor</li>
 *     <li>{@link #dependencies(Artifact)} - Returns the dependencies for the given library. Dependent libraries are resolved in depth-first order.</li>
 *     <li>{@link #lookIn(Repository)} - Adds a repository to look in when resolving libraries</li>
 *     <li>{@link #repositories()} - The list of repositories to search</li>
 *     <li>{@link #pinVersion(ArtifactDescriptor, Version)} - Pins the given artifact to the specified version</li>
 * </ul>
 *
 * <p><b>Adding Libraries</b></p>
 *
 * <ul>
 *     <li>{@link #install(Repository, Artifact)} - Adds the given library and its attached content to the given repository</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class Librarian extends BaseTool
{
    /** The repositories that this librarian searches */
    private final ObjectList<Repository> repositories = list();

    /** A map from group:artifact-id to version */
    private final ObjectMap<ArtifactDescriptor, Version> pinnedVersions = new ObjectMap<>();

    public Librarian(Builder builder)
    {
        super(builder);

        lookIn(new LocalFiascoRepository("user-repository"));
        lookIn(new CacheFiascoRepository("download-repository"));
        lookIn(new MavenRepository("maven-central", uri("https://repo1.maven.org/maven2/")));
    }

    /**
     * Resolves the given artifact in the repositories managed by this librarian. Resolution of dependent artifacts
     * occurs in depth-first order.
     *
     * @param artifact The artifact
     * @return The artifact and all of its dependencies
     */
    public DependencyList dependencies(Artifact<?> artifact)
    {
        DependencyList dependencies = dependencyList();

        // Go through the library's dependencies,
        for (var dependency : artifact.dependencies().asArtifactList())
        {
            // resolve each dependency,
            for (var resolved : dependencies(dependency).asArtifactList())
            {
                // and if it is not excluded by the library,
                if (resolved != null && artifact.excludes(resolved.descriptor()))
                {
                    // add it to the dependencies list.
                    dependencies = dependencies.withAdditionalDependencies(resolved);
                }
            }
        }

        // For each repository,
        var found = false;
        for (var repository : repositories)
        {
            // resolve the library's descriptor to an artifact,
            var descriptor = resolveArtifactVersion(artifact.descriptor());
            var resolved = repository.resolveArtifacts(list(descriptor));
            if (resolved != null)
            {
                // and if it isn't excluded,
                if (resolved.first().excludes(resolved.first().descriptor()))
                {
                    // add it to the dependencies.
                    dependencies = dependencies.withAdditionalDependencies(library(resolved.first()));
                    found = true;
                }
            }
        }

        if (!found)
        {
            illegalState("Could not resolve: $", artifact);
        }

        return dependencies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description()
    {
        return format("""
            Librarian
              repositories: $
              pinned versions: $
            """, repositories, pinnedVersions);
    }

    /**
     * Installs the given library in the target repository
     *
     * @param target The repository to deploy to
     * @param artifact The library to install
     */
    public Librarian install(Repository target, Artifact<?> artifact)
    {
        var resolved = target.resolveArtifacts(list(artifact.descriptor()));
        target.installArtifact(resolved.first());
        return this;
    }

    /**
     * Adds a repository to the search path of the librarian
     *
     * @param repository The repository to search
     */
    public void lookIn(Repository repository)
    {
        repositories.add(repository);
    }

    /**
     * Globally pins the given artifact descriptor (without a version), to the specified version. All artifacts with the
     * descriptor will be assigned the version.
     *
     * @param descriptor The group and artifact identifier (but without a version)
     * @param version The version to enforce for the descriptor
     */
    public Librarian pinVersion(ArtifactDescriptor descriptor, Version version)
    {
        ensure(descriptor.version() == null);
        pinnedVersions.put(descriptor, version);
        return this;
    }

    /**
     * Globally pins the given artifact descriptor (without a version), to the specified version. All artifacts with the
     * descriptor will be assigned the version.
     *
     * @param artifact The group and artifact identifier (which can be lacking a version)
     * @param version The version to enforce for the descriptor
     */
    public Librarian pinVersion(Artifact<?> artifact, Version version)
    {
        var descriptor = artifact.descriptor();
        pinnedVersions.put(descriptor, version);
        return this;
    }

    /**
     * Globally pins the given artifact descriptor (without a version), to the specified version. All artifacts with the
     * descriptor will be assigned the version.
     *
     * @param artifact The artifact to pin
     * @param version The version to enforce for the descriptor
     */
    public Librarian pinVersion(Artifact<?> artifact, String version)
    {
        pinVersion(artifact.descriptor(), version(version));
        return this;
    }

    /**
     * Globally pins the given artifact descriptor (without a version), to the specified version. All artifacts with the
     * descriptor will be assigned the version.
     *
     * @param descriptor The group and artifact identifier (but without a version)
     * @param version The version to enforce for the descriptor
     */
    public Librarian pinVersion(String descriptor, String version)
    {
        pinVersion(ArtifactDescriptor.artifactDescriptor(descriptor), version(version));
        return this;
    }

    /**
     * Returns a copy of the repository list for this librarian
     */
    public ObjectList<Repository> repositories()
    {
        return repositories.copy();
    }

    /**
     * Resolves the given artifact descriptor to a library
     *
     * @param descriptor The descriptor
     * @return The library
     */
    public Artifact<?> resolve(ArtifactDescriptor descriptor)
    {
        return resolve(list(descriptor)).first();
    }

    /**
     * Resolves the given artifact descriptor to a library
     *
     * @param descriptors The descriptor
     * @return The library
     */
    public ObjectList<Artifact<?>> resolve(Collection<ArtifactDescriptor> descriptors)
    {
        // Go through each repository,
        ObjectList<Artifact<?>> artifacts = list();
        for (var at : repositories())
        {
            // and if we can resolve the artifact,
            artifacts.addAll(at.resolveArtifacts(descriptors));
        }
        return artifacts;
    }

    @Override
    protected void onRun()
    {
        unsupported("Librarian does not need to be started");
    }

    /**
     * Resolves the artifact descriptor's version using any pinned versions added by
     * {@link #pinVersion(ArtifactDescriptor, Version)}
     *
     * @param descriptor The descriptor to resolve
     * @return The descriptor with the correct version resolved
     */
    private ArtifactDescriptor resolveArtifactVersion(ArtifactDescriptor descriptor)
    {
        var version = pinnedVersions.get(descriptor);

        return version == null
            ? descriptor
            : descriptor.withVersion(version);
    }
}
