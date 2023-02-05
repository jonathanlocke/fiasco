//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.librarian;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.object.Copyable;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.local.LocalRepository;
import digital.fiasco.runtime.repository.maven.MavenRepository;
import digital.fiasco.runtime.repository.remote.RemoteRepository;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.core.progress.reporters.BroadcastingProgressReporter.progressReporter;
import static com.telenav.kivakit.core.string.Formatter.format;
import static digital.fiasco.runtime.build.environment.BuildRepositoriesTrait.MAVEN_CENTRAL;

/**
 * Manages {@link Artifact}s and their dependencies. Searches the list of repositories added to this librarian with
 * {@link #withRepository(Repository)} to resolve artifacts. When artifacts are downloaded from a
 * {@link RemoteRepository} or a remote {@link MavenRepository}, they are added to a download cache, which is first in
 * the search order for artifacts.
 *
 * <p><b>Finding Libraries</b></p>
 *
 * <ul>
 *     <li>{@link #resolve(ObjectList)} - Resolves the specified artifacts</li>
 *     <li>{@link #resolve(Artifact)} - Returns the dependencies for the given library. Dependent libraries
 *                                           are resolved in depth-first order.</li>
 *     <li>{@link #withRepository(Repository)} - Adds a repository to look in when resolving libraries</li>
 *     <li>{@link #repositories()} - The list of repositories to search</li>
 *     <li>{@link #withPinnedVersion(ArtifactDescriptor, Version)} - Pins the given artifact to the specified version</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class MultiRepositoryLibrarian extends BaseComponent implements
    Librarian,
    Copyable<MultiRepositoryLibrarian>
{
    /** The repositories that this librarian searches */
    private ObjectList<Repository> repositories = list();

    /** A map from group:artifact-id to version */
    private ObjectMap<ArtifactDescriptor, Version> pinnedVersions = new ObjectMap<>();

    public MultiRepositoryLibrarian(MultiRepositoryLibrarian that)
    {
        this.repositories = that.repositories.copy();
        this.pinnedVersions = that.pinnedVersions.copy();
    }

    public MultiRepositoryLibrarian()
    {
        repositories.add(new LocalRepository("repository"));
        repositories.add(MAVEN_CENTRAL);
    }

    @Override
    public MultiRepositoryLibrarian copy()
    {
        return new MultiRepositoryLibrarian(this);
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
     * Returns a copy of the repository list for this librarian
     */
    public ObjectList<Repository> repositories()
    {
        return repositories.copy();
    }

    /**
     * Resolves the given artifact descriptors using this librarian's repositories
     *
     * @param descriptors The descriptor
     * @return The library
     */
    @Override
    public ArtifactList resolve(ObjectList<ArtifactDescriptor> descriptors)
    {
        var artifacts = ArtifactList.artifacts();

        var progress = progressReporter(this, "dependencies", descriptors.count());
        progress.start("Resolving $", descriptors.count());

        // Go through each repository,
        for (var repository : repositories())
        {
            // and resolve as many descriptors as possible from the repository,
            var resolved = repository.resolveArtifacts(descriptors);

            // adding the resolved artifacts to the result.
            artifacts = artifacts.with(resolved);
            progress.next(resolved.count());
        }
        progress.end();

        return artifacts;
    }

    /**
     * Resolves the given artifact in the repositories managed by this librarian. Resolution of dependent artifacts
     * occurs in depth-first order.
     *
     * @param artifact The artifact
     * @return The artifact and all of its dependencies
     */
    @Override
    public ArtifactList resolve(Artifact<?> artifact)
    {
        var dependencies = ArtifactList.artifacts();

        // Go through the library's dependencies,
        for (var dependency : artifact.artifactDependencies())
        {
            // resolve each dependency,
            for (var resolved : resolve(dependency))
            {
                // and if it is not excluded by the library,
                if (resolved != null && artifact.isExcluded(resolved.descriptor()))
                {
                    // add it to the dependencies list.
                    dependencies = dependencies.with(resolved);
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
                if (resolved.first().isExcluded(resolved.first().descriptor()))
                {
                    // add it to the dependencies.
                    dependencies = dependencies.with(resolved.first());
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
     * Globally pins the given artifact descriptor (without a version), to the specified version. All artifacts with the
     * descriptor will be assigned the version.
     *
     * @param descriptor The group and artifact (but no version)
     * @param version The version to enforce for the descriptor
     */
    @Override
    public Librarian withPinnedVersion(ArtifactDescriptor descriptor, Version version)
    {
        ensure(descriptor.version() == null);

        return mutatedCopy(it -> it.pinnedVersions.put(descriptor, version));
    }

    /**
     * Adds a repository to the search path of the librarian
     *
     * @param repository The repository to search
     */
    public Librarian withRepository(Repository repository)
    {
        return mutatedCopy(it -> it.repositories.add(repository));
    }

    /**
     * Resolves the artifact descriptor's version using any pinned versions added by
     * {@link #withPinnedVersion(ArtifactDescriptor, Version)}
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
