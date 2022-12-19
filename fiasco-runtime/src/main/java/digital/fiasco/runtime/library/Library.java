//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.library;

import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.build.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.artifact.Artifact;
import digital.fiasco.runtime.repository.artifact.ArtifactDescriptor;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.version.Version.parseVersion;
import static com.telenav.kivakit.interfaces.comparison.Filter.acceptAll;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.repository.artifact.ArtifactDescriptor.parseArtifactDescriptor;

/**
 * A library is an artifact with zero or more excluded artifacts
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Library implements
        Dependency<Library>,
        RegistryTrait
{
    /**
     * Creates a list of libraries from the given variable-argument list of descriptors
     *
     * @param descriptors The library descriptors
     * @return The library dependency list
     */
    public static DependencyList<Library> libraries(String... descriptors)
    {
        return dependencyList(stringList(descriptors).map(Library::library));
    }

    /**
     * Creates a list of libraries from the given variable-argument list of libraries
     *
     * @param libraries The libraries
     * @return The library dependency list
     */
    public static DependencyList<Library> libraries(Library... libraries)
    {
        return dependencyList(libraries);
    }

    /**
     * Creates a {@link Library} with the given artifact descriptor
     *
     * @param artifactDescriptor The artifact descriptor
     * @return The library The library
     */
    public static Library library(String artifactDescriptor)
    {
        return new Library(parseArtifactDescriptor(throwingListener(), artifactDescriptor));
    }

    /**
     * Creates a library for the given artifact
     *
     * @param artifact The library artifact
     * @return The library
     */
    public static Library library(Artifact artifact)
    {
        return new Library(artifact.descriptor());
    }

    /** This library's artifact */
    private ArtifactDescriptor artifactDescriptor;

    /** Dependency exclusions for this artifact */
    private Filter<ArtifactDescriptor> exclusions = acceptAll();

    protected Library(ArtifactDescriptor artifactDescriptor)
    {
        this.artifactDescriptor = artifactDescriptor;
    }

    protected Library(Library that)
    {
        artifactDescriptor = that.artifactDescriptor;
        exclusions = that.exclusions;
    }

    public ArtifactDescriptor artifactDescriptor()
    {
        return artifactDescriptor;
    }

    public Library copy()
    {
        return new Library(this);
    }

    @Override
    public DependencyList<Library> dependencies()
    {
        return require(Librarian.class).dependencies(this);
    }

    public boolean excludes(ArtifactDescriptor descriptor)
    {
        return exclusions.accepts(descriptor);
    }

    public Library excluding(ArtifactDescriptor... exclude)
    {
        return excluding(library -> arrayContains(exclude, library));
    }

    public Library excluding(Matcher<ArtifactDescriptor> pattern)
    {
        exclusions = exclusions.exclude(pattern);
        return this;
    }

    public Library version(Version version)
    {
        var copy = copy();
        copy.artifactDescriptor = artifactDescriptor.withVersion(version);
        return copy;
    }

    public Library version(String version)
    {
        return version(parseVersion(version));
    }

    public Library withIdentifier(String identifier)
    {
        var copy = copy();
        copy.artifactDescriptor = artifactDescriptor.withIdentifier(identifier);
        return copy;
    }
}
