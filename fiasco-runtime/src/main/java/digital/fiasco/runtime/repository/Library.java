//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.repository;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.build.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.artifact.Artifact;
import digital.fiasco.runtime.repository.artifact.ArtifactDescriptor;

import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.interfaces.comparison.Filter.acceptAll;
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
    public static Library library(Artifact artifact)
    {
        return new Library(artifact.descriptor());
    }

    public static Library library(Listener listener, String artifact)
    {
        return new Library(parseArtifactDescriptor(listener, artifact));
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

    @SuppressWarnings("unchecked")
    public <T extends Library> T version(Version version)
    {
        artifactDescriptor = artifactDescriptor.withVersion(version);
        return (T) this;
    }

    public <T extends Library> T version(String version)
    {
        return version(Version.parseVersion(version));
    }
}
