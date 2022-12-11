//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.repository;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.artifact.ArtifactDescriptor;

import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.interfaces.comparison.Filter.acceptAll;

@SuppressWarnings("unused")
public class Library implements Dependency<Library>
{
    public static Library library(Listener listener, String artifact)
    {
        return new Library(ArtifactDescriptor.parseArtifactDescriptor(listener, artifact));
    }

    private ArtifactDescriptor artifact;

    private Filter<Library> exclusions = acceptAll();

    private final DependencyList<Library> dependencies = new DependencyList<>();

    private Version version;

    protected Library(ArtifactDescriptor artifact)
    {
        this.artifact = artifact;
    }

    protected Library(Library that)
    {
        artifact = that.artifact;
        version = that.version;
        exclusions = that.exclusions;
    }

    public ArtifactDescriptor artifact()
    {
        return artifact;
    }

    @Override
    public DependencyList<Library> dependencies()
    {
        return dependencies.copy().without(exclusions);
    }

    public Library excluding(Matcher<Library> pattern)
    {
        exclusions = exclusions.exclude(pattern);
        return this;
    }

    public Library excluding(Library... libraries)
    {
        return excluding(library -> arrayContains(libraries, library));
    }

    @SuppressWarnings("unchecked")
    public <T extends Library> T version(Version version)
    {
        artifact = artifact.withVersion(version);
        return (T) this;
    }

    public <T extends Library> T version(String version)
    {
        return version(Version.parseVersion(version));
    }
}
