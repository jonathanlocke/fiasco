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
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.tools.BaseTool;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactResources;
import digital.fiasco.runtime.dependency.library.Library;
import digital.fiasco.runtime.repository.Repository;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.version.Version.parseVersion;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.artifactDescriptor;
import static digital.fiasco.runtime.dependency.library.Library.library;

/**
 * Manages {@link Library} artifacts and their dependencies. Searches a list of repositories added with
 * {@link #lookIn(Repository)} to resolve libraries and their dependencies.
 *
 * <p><b>Finding Libraries</b></p>
 *
 * <ul>
 *     <li>{@link #resolve(ArtifactDescriptor)} - Resolves the library specified by the given descriptor</li>
 *     <li>{@link #dependencies(Library)} - Returns the dependencies for the given library. Dependent libraries are resolved in depth-first order.</li>
 *     <li>{@link #lookIn(Repository)} - Adds a repository to look in when resolving libraries</li>
 *     <li>{@link #repositories()} - The list of repositories to search</li>
 *     <li>{@link #pinVersion(ArtifactDescriptor, Version)} - Pins the given artifact to the specified version</li>
 * </ul>
 *
 * <p><b>Adding Libraries</b></p>
 *
 * <ul>
 *     <li>{@link #add(Repository, Library, ArtifactResources)} - Adds the given library and its attached content to the given repository</li>
 * </ul>
 *
 * @author shibo
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class Librarian extends BaseTool
{
    /** The repositories that this librarian searches */
    private final ObjectList<Repository> repositories = list();

    /** A map from group:artifact-id to version */
    private final ObjectMap<ArtifactDescriptor, Version> pinnedVersions = new ObjectMap<>();

    public Librarian(Build build)
    {
        super(build);
    }

    /**
     * Installs the given library in the target repository
     *
     * @param target The repository to deploy to
     * @param library The library to install
     * @param resources The resource jar attachments
     */
    public Librarian add(Repository target, Library library, ArtifactResources resources)
    {
        var resolved = target.resolve(library.descriptor());
        target.add(resolved, resources);
        return this;
    }

    /**
     * Resolves the given library in the repositories managed by this librarian. Resolution of dependent artifacts
     * occurs in depth-first order.
     *
     * @param library The library to resolve
     * @return The library and all of its dependencies
     */
    public DependencyList<Library> dependencies(Library library)
    {
        DependencyList<Library> dependencies = dependencyList();

        // Go through the library's dependencies,
        for (var dependency : library.dependencies())
        {
            // resolve each dependency,
            for (var resolved : dependencies(dependency))
            {
                // and if it is not excluded by the library,
                if (resolved != null && library.excludes(resolved.descriptor()))
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
            var descriptor = resolveArtifactVersion(library.descriptor());
            var artifact = repository.resolve(descriptor);
            if (artifact != null)
            {
                // and if it isn't excluded,
                if (library.excludes(artifact.descriptor()))
                {
                    // add it to the dependencies.
                    dependencies = dependencies.with(library(artifact));
                    found = true;
                }
            }
        }

        if (!found)
        {
            illegalState("Could not resolve: $", library);
        }

        return dependencies;
    }

    /**
     * Adds a repository to the search path of the librarian
     *
     * @param repository The repository to search
     * @return This librarian for chaining
     */
    public Librarian lookIn(Repository repository)
    {
        repositories.add(repository);
        return this;
    }

    /**
     * Globally pins the given artifact descriptor (without a version), to the specified version. All artifacts with the
     * descriptor will be assigned the version.
     *
     * @param descriptor The group and artifact identifier (but without a version)
     * @param version The version to enforce for the descriptor
     */
    public void pinVersion(ArtifactDescriptor descriptor, Version version)
    {
        ensure(descriptor.version() == null);
        pinnedVersions.put(descriptor, version);
    }


    /**
     * Globally pins the given artifact descriptor (without a version), to the specified version. All artifacts with the
     * descriptor will be assigned the version.
     *
     * @param descriptor The group and artifact identifier (but without a version)
     * @param version The version to enforce for the descriptor
     */
    public void pinVersion(String descriptor, String version)
    {
        pinVersion(artifactDescriptor(descriptor), parseVersion(version));
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
    public Library resolve(ArtifactDescriptor descriptor)
    {
        for (var at : repositories())
        {
            var resolved = at.resolve(resolveArtifactVersion(descriptor));
            if (resolved != null)
            {
                return library(resolved);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String description()
    {
        return format("""
                Librarian
                  repositories: $
                  pinned versions: $
                """, repositories, pinnedVersions);
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
