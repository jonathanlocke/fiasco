//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package fiasco;

import com.telenav.kivakit.core.string.CaseFormat;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import fiasco.dependency.DependencyList;
import fiasco.repository.artifact.Artifact;

import static com.telenav.kivakit.core.language.Arrays.arrayContains;

@SuppressWarnings("unused")
public class Library implements Dependency<Library>
{
    @SuppressWarnings("ConstantConditions")
    public static Library parse(String descriptor)
    {
        return new Library((Library) null);
    }

    private Artifact artifact;

    private Filter<Library> exclusions = Filter.acceptAll();

    private final DependencyList<Library> dependencies = new DependencyList<>();

    private Version version;

    public static Library library(String specifier)
    {
        return null;
    }

    public Library(Artifact artifact)
    {
        this.artifact = artifact;
    }

    protected Library()
    {
        artifact = Artifact.parse(getClass().getPackageName() + ":" + CaseFormat.camelCaseToHyphenated(getClass().getSimpleName()));
    }

    protected Library(Library that)
    {
        artifact = that.artifact;
        version = that.version;
        exclusions = that.exclusions;
    }

    public Artifact artifact()
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
