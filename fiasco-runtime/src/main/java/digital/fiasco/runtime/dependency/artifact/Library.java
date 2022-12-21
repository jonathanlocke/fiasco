//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.registry.RegistryTrait;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.DependencyList;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.artifactDescriptor;
import static digital.fiasco.runtime.dependency.artifact.ArtifactType.LIBRARY;

/**
 * A library is an artifact with zero or more excluded artifacts
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class Library extends BaseArtifact<Library> implements
        Dependency<Library>,
        RegistryTrait
{
    /**
     * Creates a list of libraries from the given variable-argument list of descriptors
     *
     * @param descriptors The library descriptors
     * @return The library dependency list
     */
    public static DependencyList libraries(String... descriptors)
    {
        var libraries = stringList(descriptors).map(Library::library);
        return libraries(libraries.asArray(Library.class));
    }

    /**
     * Creates a list of libraries from the given variable-argument list of libraries
     *
     * @param libraries The libraries
     * @return The library dependency list
     */
    public static DependencyList libraries(Library... libraries)
    {
        return dependencyList(libraries);
    }

    /**
     * Creates a {@link Library} with the given artifact descriptor
     *
     * @param artifactDescriptor The artifact descriptor
     * @return The library
     */
    public static Library library(String artifactDescriptor)
    {
        return new Library(artifactDescriptor(artifactDescriptor));
    }

    /**
     * Creates a {@link Library} with the given artifact descriptor
     *
     * @param artifactDescriptor The artifact descriptor
     * @return The library
     */
    public static Library library(ArtifactDescriptor artifactDescriptor)
    {
        return new Library(artifactDescriptor).withType(LIBRARY);
    }

    /**
     * Creates a library for the given artifact
     *
     * @param artifact The library artifact
     * @return The library
     */
    public static Library library(Artifact<?> artifact)
    {
        return new Library(artifact.descriptor());
    }

    /** The Javadoc content for this library */
    private ArtifactContent javadoc;

    /** The source code for this library */
    private ArtifactContent source;

    protected Library(ArtifactDescriptor descriptor)
    {
        super(descriptor);
    }

    protected Library(Library that)
    {
        super(that);
        this.javadoc = that.javadoc;
        this.source = that.source;
    }

    @Override
    public Library copy()
    {
        return new Library(this);
    }

    public ArtifactContent javadoc()
    {
        return javadoc;
    }

    public ArtifactContent source()
    {
        return source;
    }

    public Library withJavadoc(ArtifactContent javadoc)
    {
        var copy = copy();
        copy.javadoc = javadoc;
        return copy;
    }

    public Library withSource(ArtifactContent source)
    {
        var copy = copy();
        copy.source = source;
        return copy;
    }
}
